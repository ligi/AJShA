package org.ligi.ajsha;

import android.os.Bundle;

import org.ligi.ajsha.tasks.LoadCodeFromIntentTask;
import org.ligi.tracedroid.logging.Log;

public class RunActivity extends BaseInterpretingActivity {

    @Override
    int getLayoutRes() {
        return R.layout.result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LoadCodeFromIntentTask(this) {
            @Override
            protected void onPostExecute(String s) {
                if (s!=null) {
                    execCode(s);
                }
                super.onPostExecute(s);
            }
        }.execute();
    }

    @Override
    protected void onPostEcecute(Object evaledObject) {
        super.onPostEcecute(evaledObject);
        Log.w("obj result from eval " + evaledObject);
    }
}
