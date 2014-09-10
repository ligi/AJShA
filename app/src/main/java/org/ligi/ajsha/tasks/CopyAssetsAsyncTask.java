package org.ligi.ajsha.tasks;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import org.ligi.ajsha.App;
import org.ligi.axt.AXT;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CopyAssetsAsyncTask extends AsyncTask<Void, String, Void> {

    private final Context activity;
    private final Runnable onFinishRunnable;

    public CopyAssetsAsyncTask(Activity activity, Runnable onFinishRunnable) {
        this.activity = activity;
        this.onFinishRunnable = onFinishRunnable;
    }

    private void copyAssets(String path) {
        final AssetManager assetManager = activity.getAssets();
        try {
            final String toName = AXT.at(path.split("/")).last();
            final File toPath = new File(App.getSettings().getScriptDir() + "/" + toName);
            if (!toPath.exists()) {
                toPath.mkdirs();
            }
            final String[] files = assetManager.list(path);
            for (String filename : files) {
                publishProgress(filename);
                final InputStream in = assetManager.open(path + "/" + filename);
                final File outFile = new File(toPath, filename);
                if (!outFile.exists()) {
                    if (!outFile.createNewFile()) {
                        continue;
                    }
                    final OutputStream out = new FileOutputStream(outFile);
                    copyFile(in, out);
                    in.close();
                    out.flush();
                    out.close();
                }
            }
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }

    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        final byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        App.getSettings().getScriptDir().mkdirs();
        try {
            for (String path : activity.getAssets().list("env")) {
                copyAssets("env/"+path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        onFinishRunnable.run();

    }
}
