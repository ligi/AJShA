package org.ligi.ajsha.ui;

import android.support.v7.app.ActionBarActivity;

import org.ligi.ajsha.Tracker;

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
