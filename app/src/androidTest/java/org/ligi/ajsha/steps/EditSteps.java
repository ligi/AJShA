package org.ligi.ajsha.steps;

import org.ligi.ajsha.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
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

        onView(withId(R.id.codeInput)).perform(clearText());
        onView(withId(R.id.codeInput)).perform(typeText(code));

        onView(withId(R.id.execCodeButton)).perform(click());
    }

    public static void saveFile(String filename) {

        onView(withId(R.id.action_save)).perform(click());

        onView(allOf(hasFocus(), withClassName(containsString("EditText")))).perform(clearText());
        onView(allOf(hasFocus(), withClassName(containsString("EditText")))).perform(typeText(filename));

        onView(withText(android.R.string.ok)).perform(click());
    }


}
