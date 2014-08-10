package org.ligi.ajsha.tasks;

import android.app.Activity;
import android.os.AsyncTask;

import org.ligi.ajsha.InputStreamProvider;
import org.ligi.axt.AXT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoadCodeFromIntentTask extends AsyncTask<Void, Void, String> {

    private final Activity activity;

    public LoadCodeFromIntentTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            final InputStream inputStream = InputStreamProvider.fromUri(activity, activity.getIntent().getData());
            return convertStreamToString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }
}
