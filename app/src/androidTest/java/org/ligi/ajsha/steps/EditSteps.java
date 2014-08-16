package org.ligi.ajsha.steps;

import com.google.android.apps.common.testing.ui.espresso.action.ClearTextAction;

import org.ligi.ajsha.R;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.hasFocus;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withClassName;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

public class EditSteps {
    public static void loadAndEvaluateCode(String codePath, String codeFilename) {
        onView(withId(R.id.action_load)).perform(click());

        if (codePath != null) {
            onView(withText(codePath)).perform(click());
        }

        onView(withText(codeFilename)).check(matches(isDisplayed())); // kind of a wait

        onView(withText(codeFilename)).perform(click());

        onView(withId(R.id.execCodeButton)).perform(click());
    }


    public static void inputAndEvaluateCode(String code) {

        onView(withId(R.id.codeInput)).perform(new ClearTextAction());
        onView(withId(R.id.codeInput)).perform(typeText(code));

        onView(withId(R.id.execCodeButton)).perform(click());
    }

    public static void saveFile(String filename) {

        onView(withId(R.id.action_save)).perform(click());

        onView(allOf(hasFocus(), withClassName(containsString("EditText")))).perform(new ClearTextAction());
        onView(allOf(hasFocus(), withClassName(containsString("EditText")))).perform(typeText(filename));

        onView(withText(android.R.string.ok)).perform(click());
    }


}
