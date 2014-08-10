package org.ligi.ajsha;

import android.test.ActivityInstrumentationTestCase2;

import com.google.android.apps.common.testing.ui.espresso.action.ClearTextAction;
import com.squareup.spoon.Spoon;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withChild;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.text.StringEndsWith.endsWith;

public class EditActivityTest extends ActivityInstrumentationTestCase2<EditActivity> {
    public EditActivityTest() {
        super(EditActivity.class);
    }

    public void testInputIsThere() {
        Spoon.screenshot(getActivity(), "main");
        onView(withId(R.id.codeInput)).check(matches(isDisplayed()));
    }

    public void testOnePlusOneIsTwo() {

        final EditActivity activity = uEvalCode("1+1");

        onView(withId(R.id.obj_tostring)).check(matches(withText("2")));

        Spoon.screenshot(activity, "one_plus_one");
    }

    public void testAndroidIsResolved() {

        final EditActivity activity = uEvalCode("import android.os.*;Build");

        Spoon.screenshot(activity, "eval_build");

        onView(withId(R.id.obj_tostring)).check(matches(withText(endsWith("android.os.Build"))));
        onView(withId(R.id.obj_classinfo)).check(matches(withText(endsWith("android.os.Build"))));


    }

    public void testExceptionsAreCaredOf() {

        final EditActivity activity = uEvalCode("1/0");

        onView(withId(R.id.exception_out)).check(matches(withText(containsString("Exception"))));

        Spoon.screenshot(activity, "exception");
    }


    public void testContextIsThere() {

        final EditActivity activity = uEvalCode("ctx");

        onView(withId(R.id.obj_classinfo)).check(matches(withText(endsWith("Activity"))));

        Spoon.screenshot(activity, "ctx");
    }

    public void testContainerWorks() {

        final EditActivity activity = loadEvalCode("views");

        onView(withId(R.id.linearLayout)).check(matches(withChild(withText(containsString("check")))));

        Spoon.screenshot(activity, "checkbox");
    }

    public void testDialogWorks() {

        final EditActivity activity = loadEvalCode("dialog");

        onView(withText(containsString("test"))).check(matches(isDisplayed()));

        Spoon.screenshot(activity, "dialog");
    }


    public void testThatLoadCalcWorks() {

        final EditActivity activity = loadEvalCode("calculation");

        onView(withId(R.id.obj_tostring)).check(matches(withText(endsWith("202"))));

        Spoon.screenshot(activity, "load_calc");
    }

    /**
     * steps *
     */
    private EditActivity loadEvalCode(String code) {
        final EditActivity activity = getActivity();

        onView(withId(R.id.action_load)).perform(click());
        onView(withText("examples/")).perform(click());
        onView(withText("calculation")).check(matches(isDisplayed())); // kind of a wait
        Spoon.screenshot(activity, "load");

        onView(withText(code)).perform(click());

        onView(withId(R.id.execCodeButton)).perform(click());

        return activity;
    }

    private EditActivity uEvalCode(String code) {

        final EditActivity activity = getActivity();

        onView(withId(R.id.codeInput)).perform(new ClearTextAction());
        onView(withId(R.id.codeInput)).perform(typeText(code));

        onView(withId(R.id.execCodeButton)).perform(click());

        return activity;
    }

}
