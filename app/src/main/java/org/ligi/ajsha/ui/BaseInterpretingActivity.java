package org.ligi.ajsha.ui;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import org.ligi.ajsha.R;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import bsh.EvalError;
import bsh.Interpreter;
import butterknife.ButterKnife;

public abstract class BaseInterpretingActivity extends BaseActivity {

    protected Interpreter interpreter;

    @Bind(R.id.exception_out)
    TextView exceptionOut;

    @Bind(R.id.obj_classinfo)
    TextView objClassInfo;

    @Bind(R.id.time)
    TextView timeTV;

    @Bind(R.id.out_stream)
    TextView streamedOutTV;

    @Bind(R.id.obj_tostring)
    TextView toStringTV;

    @Bind(R.id.linearLayout)
    LinearLayout linearLayout;

    private String streamedOutString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutRes());
        ButterKnife.bind(this);

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
