package org.ligi.ajsha.tasks;

import android.app.Activity;

import org.ligi.ajsha.App;
import org.ligi.axt.AXT;

import java.io.File;
import java.io.IOException;

import bsh.EvalError;
import bsh.Interpreter;

public class ExecutePluginsAsyncTask extends BaseAsyncTask {

    final Interpreter interpreter;

    public ExecutePluginsAsyncTask(Activity context, Interpreter interpreter) {
        super(context);
        this.interpreter = interpreter;
    }

    @Override
    protected Void doInBackground(Void... params) {
        final File[] plugins = new File(App.getSettings().getScriptDir(), "plugins").listFiles();
        for (File plugin : plugins) {
            publishProgress(plugin.getAbsolutePath());
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

        progressDialog.setMessage(values[0]);

        try {
            final String codeString = AXT.at(new File(values[0])).readToString();
            interpreter.eval(codeString);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EvalError evalError) {
            evalError.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
