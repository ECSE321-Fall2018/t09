package ca.mcgill.ecse321.rideshare9;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.StringStartsWith.startsWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AdvertisementInstrumentedTest {
    private static final String UNAME_TO_BE_TYPED = "xuxue";
    private static final String PSWD_TO_BE_TYPED = "12345";
    private static final String TITLE_TO_BE_TYPED = "Montreal to NewYork";
    private static final String START_TO_BE_TYPED = "Montreal";


    @Rule
    public IntentsTestRule<FullscreenActivity> mActivityRule = new IntentsTestRule<>(
            FullscreenActivity.class);
    public IntentsTestRule<RideShare9> rActivityRule = new IntentsTestRule<>(
            RideShare9.class);

    @Before
    public void xuxueLogin(){
        // Login Prelude
        onView(withId(R.id.loginnameText)).perform(clearText(), typeText(UNAME_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.loginpasswordText)).perform(clearText(), typeText(PSWD_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.LoginButton)).perform(click());
        try{
            Thread.sleep(2000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onView(withId(R.id.navigation_advertisement)).perform(click());
    }

    /**
     * This test tests Refresh Button implicitly every time when an action was performed
     */
    @Test
    public void addRefreshDeleteTest() {
        // Test Add Advertisment
        onView(withId(R.id.toAddTripButton)).perform(click());
        try{
            Thread.sleep(2000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        intended(hasComponent(addJourneyActivity.class.getName()));
        onView(withId(R.id.trip_title_text)).perform(clearText(), typeText(TITLE_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.startLocationText)).perform(clearText(), typeText(START_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.Btn_addStop)).perform(click());


        try{
            Thread.sleep(2000L);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
