package org.ligi.ajsha;

import android.test.ApplicationTestCase;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationTest extends ApplicationTestCase<App> {
    public ApplicationTest() {
        super(App.class);
    }


    public void testThatDirIsThere() {
        getApplication();

        final boolean isDirectory = App.getSettings().getScriptDir().isDirectory();

        assertThat(isDirectory).isTrue();
    }

}