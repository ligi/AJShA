package org.ligi.ajsha;

import android.test.ActivityInstrumentationTestCase2;

import com.google.android.apps.common.testing.ui.espresso.action.ClearTextAction;
import com.squareup.spoon.Spoon;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.hasSibling;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;

import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public MainActivityTest() {
        super(MainActivity.class);
    }

    public void testInputIsThere() {
        Spoon.screenshot(getActivity(), "main");

        onView(withId(R.id.codeInput)).check(matches(isDisplayed()));
    }

    public void testOnePlusOneIsTwo() {

        final MainActivity activity = uiEvalCode("1+1");

        onView(withId(R.id.obj_tostring)).check(matches(withText("2")));

        Spoon.screenshot(activity, "one_plus_one");
    }

    public void testAndroidIsResolved() {

        final MainActivity activity = uiEvalCode("Build");

        Spoon.screenshot(activity, "eval_build");

        onView(withId(R.id.obj_tostring)).check(matches(withText(endsWith("android.os.Build"))));
        onView(withId(R.id.obj_classinfo)).check(matches(withText(endsWith("android.os.Build"))));


    }

    public void testExceptionsAreCaredOf() {

        final MainActivity activity = uiEvalCode("1/0");

        onView(withId(R.id.exception_out)).check(matches(withText(containsString("Exception"))));

        Spoon.screenshot(activity, "exception");
    }


    public void testContextIsThere() {

        final MainActivity activity = uiEvalCode("ctx");

        onView(withId(R.id.obj_classinfo)).check(matches(withText(endsWith("Activity"))));

        Spoon.screenshot(activity, "ctx");
    }

    public void testContainerWorks() {

        final MainActivity activity = getActivity();

        onView(withId(R.id.action_load)).perform(click());
        onView(withText("views")).perform(click());

        onView(withId(R.id.execCodeButton)).perform(click());

        onView(withId(R.id.linearLayout)).check(matches(hasSibling(withText(containsString("check")))));

        Spoon.screenshot(activity, "checkbox");
    }


    public void testThatLoadCalcWorks() {
        final MainActivity activity = getActivity();

        onView(withId(R.id.action_load)).perform(click());

        onView(withText("calculation")).check(matches(isDisplayed())); // kind of a wait
        Spoon.screenshot(activity, "load");

        onView(withText("calculation")).perform(click());


        onView(withId(R.id.execCodeButton)).perform(click());

        onView(withId(R.id.obj_tostring)).check(matches(withText(endsWith("202"))));

        Spoon.screenshot(activity, "load_calc");
    }


    private MainActivity uiEvalCode(String code) {

        final MainActivity activity = getActivity();

        onView(withId(R.id.codeInput)).perform(new ClearTextAction());
        onView(withId(R.id.codeInput)).perform(typeText(code));

        onView(withId(R.id.execCodeButton)).perform(click());

        return activity;
    }

}
