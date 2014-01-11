package pl.lukok.ACheckers;

import android.test.ActivityInstrumentationTestCase2;
import pl.lukok.acheckers.activity.GameActivity;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class pl.lukok.acheckers.activity.GameActivityTest \
 * pl.lukok.ACheckers.tests/android.test.InstrumentationTestRunner
 */
public class GameActivityTest extends ActivityInstrumentationTestCase2<GameActivity> {

    public GameActivityTest() {
        super("pl.lukok.ACheckers", GameActivity.class);
    }

}
