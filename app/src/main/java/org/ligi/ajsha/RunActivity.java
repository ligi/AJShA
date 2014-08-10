package org.ligi.ajsha;

import android.os.AsyncTask;
import android.os.Bundle;

import org.ligi.axt.AXT;
import org.ligi.tracedroid.logging.Log;

import java.io.IOException;
import java.io.InputStream;

public class RunActivity extends BaseInterpretingActivity {

    @Override
    int getLayoutRes() {
        return R.layout.result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LoadAsyncTask().execute();
    }

    class LoadAsyncTask extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                final InputStream inputStream = InputStreamProvider.fromUri(RunActivity.this, getIntent().getData());
                return AXT.at(inputStream).readToString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s!=null) {
                execCode(s);
            }
            super.onPostExecute(s);
        }
    }

    @Override
    protected void onPostEcecute(Object evaledObject) {
        super.onPostEcecute(evaledObject);
        Log.w("obj result from eval " + evaledObject);
    }
}
