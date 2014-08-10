package org.ligi.ajsha;

import android.os.Bundle;

import org.ligi.ajsha.tasks.LoadCodeFromIntentTask;

public class EditFromIntentActivity extends EditActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null && getIntent().getData() != null && getIntent().getData().toString().endsWith("aj")) {
            new LoadCodeFromIntentTask(this) {
                @Override
                protected void onPostExecute(String s) {
                    codeEditText.setText(s);
                    super.onPostExecute(s);
                }
            }.execute();
        }
    }
}
