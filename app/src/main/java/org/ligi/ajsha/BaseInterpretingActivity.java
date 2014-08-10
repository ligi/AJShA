package org.ligi.ajsha;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.LinkMovementMethod;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import bsh.EvalError;
import bsh.Interpreter;
import butterknife.ButterKnife;
import butterknife.InjectView;

public abstract class BaseInterpretingActivity extends ActionBarActivity {

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
                onPostEcecute(evaledObject);
            }
        } catch (EvalError evalError) {
            exceptionOut.setText("" + evalError);
            evalError.printStackTrace();
        }
    }

    protected void onPostEcecute(Object evaledObject) {
        toStringTV.setText(evaledObject.toString());

    }
    abstract int getLayoutRes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutRes());
        ButterKnife.inject(this);

        initInterpreter();

        objClassInfo.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    protected void onStart() {
        super.onStart();
        Tracker.get().activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Tracker.get().activityStop(this);
    }

    protected void initInterpreter() {
        interpreter = new Interpreter();

        try {
            interpreter.set("ctx", this);
            interpreter.set("container", linearLayout);

        } catch (EvalError evalError) {
            evalError.printStackTrace();
        }

        OutputStream streamedOutStream = new OutputStream() {
            @Override
            public void write(int oneByte) throws IOException {
                streamedOutString += (char) oneByte;
                streamedOutTV.setText(BaseInterpretingActivity.this.streamedOutString);
            }
        };

        interpreter.setOut(new PrintStream(streamedOutStream));
        interpreter.setErr(new PrintStream(streamedOutStream));
    }

}
