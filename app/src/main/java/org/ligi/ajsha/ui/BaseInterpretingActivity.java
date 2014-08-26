package org.ligi.ajsha.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.LinkMovementMethod;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ligi.ajsha.R;
import org.ligi.ajsha.Tracker;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import bsh.EvalError;
import bsh.Interpreter;
import butterknife.ButterKnife;
import butterknife.InjectView;

public abstract class BaseInterpretingActivity extends BaseActivity {

    protected Interpreter interpreter;

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

    @InjectView(R.id.linearLayout)
    LinearLayout linearLayout;

    private String streamedOutString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutRes());
        ButterKnife.inject(this);

        initInterpreter();

        objClassInfo.setMovementMethod(LinkMovementMethod.getInstance());
    }

    protected void initInterpreter() {
        interpreter = new Interpreter();

        streamSetup();
        passObjects();

    }

    private void passObjects() {
        try {
            interpreter.set("ctx", this);
            interpreter.set("container", linearLayout);

        } catch (EvalError evalError) {
            evalError.printStackTrace();
        }
    }

    private void streamSetup() {
        final OutputStream streamedOutStream = new OutputStream() {
            @Override
            public void write(int oneByte) throws IOException {
                streamedOutString += (char) oneByte;
                streamedOutTV.setText(streamedOutString);
            }
        };

        interpreter.setOut(new PrintStream(streamedOutStream));
        interpreter.setErr(new PrintStream(streamedOutStream));
    }

    protected void execCode(String code) {
        try {
            streamedOutString = "";
            streamedOutTV.setText(streamedOutString);

            final long startTime = System.currentTimeMillis();
            final Object evaledObject = interpreter.eval(code);
            final long execTime = System.currentTimeMillis() - startTime;
            timeTV.setText("" + execTime + "ms");
            exceptionOut.setText("");
            if (evaledObject == null) {
                objClassInfo.setText("VOID");
                toStringTV.setText("");
            } else {
                onPostExecute(evaledObject);
            }
        } catch (EvalError evalError) {
            exceptionOut.setText("" + evalError);
            evalError.printStackTrace();
        }
    }

    protected void onPostExecute(Object evaledObject) {
        toStringTV.setText(evaledObject.toString());
    }
    abstract int getLayoutRes();


}
