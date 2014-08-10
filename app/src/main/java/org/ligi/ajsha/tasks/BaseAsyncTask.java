package org.ligi.ajsha.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public abstract class BaseAsyncTask extends AsyncTask<Void, String, Void> {

    final Activity context;
    final ProgressDialog progressDialog;

    public BaseAsyncTask(Activity context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        progressDialog.setMessage(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (!context.isFinishing() && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onPostExecute(aVoid);
    }
}
