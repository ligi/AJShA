package org.ligi.ajsha;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;

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

    public static final String CODE_KEY = "code";
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
            final Object evaledObject = interpreter.eval(getImportString() + codeEditText.getText().toString());
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

                final Spanned html = Html.fromHtml("<a href='" + getLinkForClass(evalClass) + "'>" + evalClass.toString() + "</a>");
                objClassInfo.setText(html);
                objClassInfo.setMovementMethod(LinkMovementMethod.getInstance());

                toStringTV.setText(evaledObject.toString());
            }
        } catch (EvalError evalError) {
            exceptionOut.setText("" + evalError);
            evalError.printStackTrace();
        }
    }

    private String getImportString() {
        return "import android.content.*;" +
                "import android.widget.*;" +
                "import android.os.*;" +
                "import android.app.*;" +
                "import org.ligi.axt.*;";
    }

    private String getLinkForClass(Class inClass) {
        String link = inClass.getCanonicalName();
        link = link.replace(".", "/");
        return "http://developer.android.com/reference/" + link + ".html";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!new File(App.getScriptDir()+"/examples", "help.aj").exists()) {
            new CopyAssetsAsyncTask(this).execute();
            return;
        }

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

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

        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String showedCode = sharedPrefs.getString(CODE_KEY, getString(R.string.hello_world_code));
        codeEditText.setText(showedCode);

        codeEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                PreferenceManager.getDefaultSharedPreferences(this).
                        edit()
                        .putString(CODE_KEY, codeEditText.getText().toString())
                        .commit();
                return true;
            case R.id.action_load:
                showLoadDialogForPath(App.getScriptDir());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLoadDialogForPath(final File path) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(path.toString());
        try {
            final String[] fileNames = path.list();
            final ArrayAdapter<String> scriptsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileNames) {
                @Override
                public String getItem(int position) {
                    return super.getItem(position).replace(".aj","");
                }
            };
            builder.setAdapter(scriptsAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {

                        final File file = new File(path, fileNames[which]);

                        if (file.isDirectory()) {
                            showLoadDialogForPath(file);
                        } else {
                            final InputStream inputStream = new FileInputStream(file);

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
