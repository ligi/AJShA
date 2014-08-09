package org.ligi.ajsha.tasks;

import android.app.Activity;
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

public class CopyAssetsAsyncTask extends BaseAsyncTask {

    public CopyAssetsAsyncTask(Activity context) {
        super(context);
    }

    private void copyAssets(String path) {
        final AssetManager assetManager = context.getAssets();
        try {
            final File toPath = new File(App.getSettings().getScriptDir() +"/"+ path);
            if (!toPath.exists()) {
                toPath.mkdirs();
            }
            final String[] files = assetManager.list(path);
            for (String filename : files) {
                progressDialog.setMessage(filename);
                final InputStream in = assetManager.open(path+"/" +filename);
                final File outFile = new File(toPath, filename);
                outFile.createNewFile();
                final OutputStream out = new FileOutputStream(outFile);
                copyFile(in, out);
                in.close();
                out.flush();
                out.close();
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
        copyAssets("examples");
        copyAssets("plugins");
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        AXT.at(context).startCommonIntent().activityFromClass(MainActivity.class);
        super.onPostExecute(aVoid);
    }
}
