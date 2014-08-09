package org.ligi.ajsha.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import org.ligi.ajsha.App;
import org.ligi.ajsha.MainActivity;
import org.ligi.axt.AXT;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    protected void onPostExecute(Void aVoid) {
        if (!context.isFinishing() && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onPostExecute(aVoid);
    }
}
