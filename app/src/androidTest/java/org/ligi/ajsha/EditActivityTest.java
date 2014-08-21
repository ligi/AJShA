package org.ligi.ajsha;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.squareup.spoon.Spoon;

import org.boundbox.BoundBox;

import java.io.File;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.assertThat;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withChild;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.text.StringEndsWith.endsWith;
import static org.ligi.ajsha.steps.EditSteps.inputAndEvaluateCode;
import static org.ligi.ajsha.steps.EditSteps.loadAndEvaluateCode;
import static org.ligi.ajsha.steps.EditSteps.saveFile;

public class EditActivityTest extends ActivityInstrumentationTestCase2<EditActivity> {
    public EditActivityTest() {
        super(EditActivity.class);
    }

    @BoundBox(boundClass = Settings.class, maxSuperClass = Settings.class)
    private BoundBoxOfSettings boundBoxOfSettings;

    private static final String EXAMPLE_EXTRA_INTENT_CODE = "example extra intent code";

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        boundBoxOfSettings = new BoundBoxOfSettings(App.getSettings());
        boundBoxOfSettings.boundBox_setAjshaPath(new File(getActivity().getCacheDir(), "testing"));

    }

    @Override
    public EditActivity getActivity() {

        Intent intent = new Intent();
        intent.setClassName("org.ligi.ajsha","org.ligi.ajsha.EditActivity");
        intent.putExtra(EditActivity.EXTRA_CODE, EXAMPLE_EXTRA_INTENT_CODE);

        setActivityIntent(intent);

        return super.getActivity();
    }

    public void testInputIsThere() {
        Spoon.screenshot(getActivity(), "main");
        onView(withId(R.id.codeInput)).check(matches(isDisplayed()));
    }

    public void testOnePlusOneIsTwo() {

        inputAndEvaluateCode("1+1");

        onView(withId(R.id.obj_tostring)).check(matches(withText("2")));

        Spoon.screenshot(getActivity(), "one_plus_one");
    }

    public void testAndroidIsResolved() {

        inputAndEvaluateCode("import android.os.*;Build");

        Spoon.screenshot(getActivity(), "eval_build");

        onView(withId(R.id.obj_tostring)).check(matches(withText(endsWith("android.os.Build"))));
        onView(withId(R.id.obj_classinfo)).check(matches(withText(endsWith("android.os.Build"))));

    }

    public void testExceptionsAreCaredOf() {

        inputAndEvaluateCode("1/0");

        onView(withId(R.id.exception_out)).check(matches(withText(containsString("Exception"))));

        Spoon.screenshot(getActivity(), "exception");
    }


    public void testContextIsThere() {

        inputAndEvaluateCode("ctx");

        onView(withId(R.id.obj_classinfo)).check(matches(withText(endsWith("Activity"))));

        Spoon.screenshot(getActivity(), "ctx");
    }

    public void testContainerWorks() {

        loadAndEvaluateCode("examples/","views");

        onView(withId(R.id.linearLayout)).check(matches(withChild(withText(containsString("check")))));

        Spoon.screenshot(getActivity(), "checkbox");
    }

    public void testDialogWorks() {

        loadAndEvaluateCode("examples/","dialog");

        onView(withText(containsString("test"))).check(matches(isDisplayed()));

        Spoon.screenshot(getActivity(), "dialog");
    }


    public void testThatLoadCalcWorks() {

        loadAndEvaluateCode("examples/","calculation");

        onView(withId(R.id.obj_tostring)).check(matches(withText(endsWith("202"))));

        Spoon.screenshot(getActivity(), "load_calc");
    }

    public void testSaveWorks() {
        final String INPUT = "test\nfoobar";
        final String FNAME = "foo_fname2";

        inputAndEvaluateCode(INPUT);

        saveFile(FNAME);
        inputAndEvaluateCode("bar");

        loadAndEvaluateCode(null, FNAME);

        onView(withId(R.id.codeInput)).check(matches(withText(INPUT)));
    }

    public void testStartWithIntent(){

        getActivity();

        onView(withId(R.id.codeInput)).check(matches(withText(EXAMPLE_EXTRA_INTENT_CODE)));

        Spoon.screenshot(getActivity(), "intent_code");
    }
}
