package org.ligi.ajsha;

import android.app.Application;
import android.os.Environment;

import java.io.File;

public class App extends Application {

    private static App instance;

    private static File tryPath(File path) {
        try {
            if (!path.exists()) {
                path.mkdirs();
            }
            if (path.exists()) {
                return path;
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    public static File getScriptDir() {
        File ajshaPath = tryPath(new File(Environment.getExternalStorageDirectory(), "ajsha"));
        if (ajshaPath!=null) {
            return ajshaPath;
        }

        ajshaPath = tryPath(new File(instance.getFilesDir(), "ajsha"));

        return ajshaPath;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
