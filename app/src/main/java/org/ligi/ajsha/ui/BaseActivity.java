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

public abstract class BaseActivity extends ActionBarActivity {

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

}
