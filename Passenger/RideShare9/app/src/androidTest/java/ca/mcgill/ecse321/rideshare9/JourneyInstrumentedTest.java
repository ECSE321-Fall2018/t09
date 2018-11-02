package ca.mcgill.ecse321.rideshare9;


import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import ca.mcgill.ecse321.rideshare9.user.HomeFragment;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasEntry;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class JourneyInstrumentedTest {
    private static final String UNAME_TO_BE_TYPED = "xuxue666";
    private static final String PSWD_TO_BE_TYPED = "12345";
    private static final String STOP_TO_BE_TYPED = "Boston";
    @Rule
    public IntentsTestRule<FullscreenActivity_login> mActivityRule = new IntentsTestRule<>(
            FullscreenActivity_login.class);
    @Before
    public void xuxueLogin(){
        onView(withId(R.id.username)).perform(clearText(), typeText(UNAME_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(clearText(), typeText(PSWD_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        try{
            Thread.sleep(1500L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onView(withId(R.id.navigation_Advertisements)).perform(click());
    }

    @Test
    public void addRefreshDeleteTest() {
        // Test search by stop
        onView(withId(R.id.searchText)).perform(clearText(), typeText(STOP_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.sortByPrice)).perform(click());
        onView(withId(R.id.searchIcon)).perform(click());
        try{
            Thread.sleep(2000L);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
