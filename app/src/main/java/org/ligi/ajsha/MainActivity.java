package org.ligi.ajsha;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.ligi.ajsha.tasks.CopyAssetsAsyncTask;
import org.ligi.ajsha.tasks.ExecutePluginsAsyncTask;
import org.ligi.axt.AXT;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;

import bsh.ClassIdentifier;
import bsh.EvalError;
import bsh.Interpreter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends ActionBarActivity {

    private Interpreter interpreter;

    @InjectView(R.id.exception_out)
    TextView exceptionOut;

    @InjectView(R.id.obj_classinfo)
    TextView objClassInfo;

    @InjectView(R.id.time)
    TextView timeTV;

    @InjectView(R.id.out_stream)
    TextView streamedOutTV;

    @InjectView(R.id.obj_tostring)
    TextView toStringTV;

    @InjectView(R.id.codeInput)
    EditText codeEditText;

    @InjectView(R.id.buttonContainer)
    LinearLayout buttonContainer;

    @InjectView(R.id.linearLayout)
    LinearLayout linearLayout;

    private String streamedOutString;

    @OnClick(R.id.execCodeButton)
    void execCodeonClick() {
        try {
            streamedOutString = "";
            streamedOutTV.setText(streamedOutString);

            final long startTime = System.currentTimeMillis();
            final Object evaledObject = interpreter.eval(codeEditText.getText().toString());
            final long execTime = System.currentTimeMillis() - startTime;
            timeTV.setText("" + execTime + "ms");
            exceptionOut.setText("");
            if (evaledObject == null) {
                objClassInfo.setText("VOID");
                toStringTV.setText("");
            } else {

                final Class evalClass;
                if (evaledObject instanceof ClassIdentifier) {
                    evalClass = ((ClassIdentifier) evaledObject).getTargetClass();
                } else {
                    evalClass = evaledObject.getClass();
                }

                if (evalClass.getCanonicalName().startsWith("android") || evalClass.getCanonicalName().startsWith("java")) {
                    final Spanned html = Html.fromHtml("<a href='" + getLinkForClass(evalClass) + "'>" + evalClass.getCanonicalName() + "</a>");
                    objClassInfo.setText(html);
                } else {
                    objClassInfo.setText(evalClass.getCanonicalName());
                }

                toStringTV.setText(evaledObject.toString());
            }
        } catch (EvalError evalError) {
            exceptionOut.setText("" + evalError);
            evalError.printStackTrace();
        }
    }


    private String getLinkForClass(Class inClass) {
        String link = inClass.getCanonicalName();
        link = link.replace(".", "/");
        return "http://developer.android.com/reference/" + link + ".html";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        if (!new File(App.getSettings().getScriptDir()+"/examples", "help.aj").exists()) {
            new CopyAssetsAsyncTask(this).execute();
            return; // we come back later with an new intent
        }

        initInterpreter();

        new ExecutePluginsAsyncTask(this, interpreter).execute();

        objClassInfo.setMovementMethod(LinkMovementMethod.getInstance());
        codeEditText.setText(App.getSettings().getRecentCode());
    }

    private void initInterpreter() {
        interpreter = new Interpreter();

        try {
            interpreter.set("ctx", this);
            interpreter.set("codeEditText", codeEditText);
            interpreter.set("container", linearLayout);
            interpreter.set("buttonContainer", buttonContainer);

        } catch (EvalError evalError) {
            evalError.printStackTrace();
        }

        OutputStream streamedOutStream = new OutputStream() {
            @Override
            public void write(int oneByte) throws IOException {
                streamedOutString += (char) oneByte;
                streamedOutTV.setText(MainActivity.this.streamedOutString);
            }
        };

        interpreter.setOut(new PrintStream(streamedOutStream));
        interpreter.setErr(new PrintStream(streamedOutStream));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        App.getSettings().setRecentCode(codeEditText.getText().toString());
        super.onPause();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                final EditText fileNameET = new EditText(this);
                fileNameET.setText(App.getSettings().getRecentFileName());
                new AlertDialog.Builder(this)
                        .setView(fileNameET)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final File outFile = new File(App.getSettings().getScriptDir(), fileNameET.getText().toString());
                                AXT.at(outFile).writeString(codeEditText.getText().toString() + ".aj");
                                App.getSettings().setRecentFileName(fileNameET.getText().toString());
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();

                return true;
            case R.id.action_load:
                showLoadDialogForPath(App.getSettings().getScriptDir());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLoadDialogForPath(final File path) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(path.toString());
        try {
            final File[] files = path.listFiles();

            final ArrayAdapter<File> scriptsAdapter = new ArrayAdapter<File>(this, android.R.layout.simple_list_item_1, files) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    final TextView view = (TextView) super.getView(position, convertView, parent);
                    final File item = getItem(position);
                    if (item.isDirectory()) {
                        view.setText(AXT.at(item.toString().split("/")).last() + "/");
                    } else {
                        view.setText(item.getName().replace(".aj", ""));
                    }
                    return view;
                }
            };
            builder.setAdapter(scriptsAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {

                        final File file = files[which];

                        if (file.isDirectory()) {
                            showLoadDialogForPath(file);
                        } else {
                            final InputStream inputStream = new FileInputStream(file);
                            App.getSettings().setRecentFileName(file.toString().replace(App.getSettings().getScriptDir().toString() + "/", ""));
                            StringWriter writer = new StringWriter();
                            IOUtils.copy(inputStream, writer, "UTF-8");
                            String theString = writer.toString();

                            codeEditText.setText(theString);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
