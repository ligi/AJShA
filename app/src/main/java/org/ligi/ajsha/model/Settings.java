package org.ligi.ajsha.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import org.ligi.ajsha.R;

import java.io.File;

public class Settings {

    private static final String CODE_KEY = "code";
    private static final String FNAME_KEY = "fname";

    final Context ctx;
    final SharedPreferences sharedPrefs;
    private File ajshaPath;

    public Settings(Context ctx) {
        this.ctx = ctx;
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
    }


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

    public File getScriptDir() {
        if (ajshaPath != null) {
            return ajshaPath;
        }

        ajshaPath = tryPath(new File(Environment.getExternalStorageDirectory(), "ajsha"));

        if (ajshaPath != null) {
            return ajshaPath;
        }

        ajshaPath = tryPath(new File(ctx.getFilesDir(), "ajsha"));

        return ajshaPath;
    }

    public String getRecentFileName() {
        return sharedPrefs.getString(FNAME_KEY, "default");
    }

        public void setRecentFileName(String fileName) {
        sharedPrefs.edit().putString(FNAME_KEY,fileName).commit();

    }

    public String getRecentCode() {
        return sharedPrefs.getString(CODE_KEY, ctx.getString(R.string.hello_world_code));
    }

    public void setRecentCode(String code) {
        sharedPrefs.edit().putString(CODE_KEY,code).commit();
    }

    /**
     * TODO REMOVE me - just here for tests - boundbox made too many problems - but do not want stuff
     * just for tests in my project
     */
    public void setAjshaPath(File path) {
        ajshaPath=path;
    }
}
