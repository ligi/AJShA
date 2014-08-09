package org.ligi.ajsha;

import android.app.Application;

public class App extends Application {

    private static App instance;

    public static Settings getSettings() {
        return new Settings(instance);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
