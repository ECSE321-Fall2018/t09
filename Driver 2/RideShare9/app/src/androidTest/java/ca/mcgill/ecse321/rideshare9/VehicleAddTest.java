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
public class VehicleAddTest {
    private static final String UNAME_TO_BE_TYPED = "xuxue";
    private static final String PSWD_TO_BE_TYPED = "12345";
    private static final String VMODEL_TO_BE_TYPED = "T80-BV";
    private static final String VLICENCE_TO_BE_TYPED = "XUXUE999";
    private static final String VCOLOR_TO_BE_TYPED = "Green";
    private static final String VCOLOR_CHANGED_TO_BE_TYPED = "Black";
    private static final String VLIMIT_TO_BE_TYPED = "3";
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
        onView(withId(R.id.navigation_vehicle)).perform(click());
    }

    /**
     * This test tests Refresh Button implicitly every time when an action was performed
     */
    @Test
    public void addRefreshDeleteTest() {
        // Test Add Vehicle
        onView(withId(R.id.add_vehicle)).perform(click());
        try{
            Thread.sleep(2000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        intended(hasComponent(AddVehicleActivity.class.getName()));
        onView(withId(R.id.vehicle_model)).perform(clearText(), typeText(VMODEL_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.vehicle_licence)).perform(clearText(), typeText(VLICENCE_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.vehicle_color)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(VCOLOR_TO_BE_TYPED))).perform(click());
        onView(withId(R.id.vehicle_color)).check(matches(withSpinnerText(containsString(VCOLOR_TO_BE_TYPED))));
        onView(withId(R.id.vehicle_number_of_seats)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(VLIMIT_TO_BE_TYPED))).perform(click());
        onView(withId(R.id.vehicle_number_of_seats)).check(matches(withSpinnerText(containsString(VLIMIT_TO_BE_TYPED))));
        onView(withId(R.id.to_add_vehicle)).perform(click());
        try{
            Thread.sleep(2000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onView(withId(R.id.refresh_vehicle)).perform(click());
        try{
            Thread.sleep(2000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onData(anything())
                .inAdapterView(withId(R.id.listview_vehicle))
                .atPosition(0)
                .onChildView(withId(R.id.grand_content_layout))
                .onChildView(withId(R.id.id_seat_model_layout))
                .onChildView(withId(R.id.list_item_title))
                .check(matches(withText(startsWith(VMODEL_TO_BE_TYPED))));
        onData(anything())
                .inAdapterView(withId(R.id.listview_vehicle))
                .atPosition(0)
                .onChildView(withId(R.id.grand_content_layout))
                .onChildView(withId(R.id.id_seat_model_layout))
                .onChildView(withId(R.id.list_item_max_seat))
                .check(matches(withText("Seats: " + VLIMIT_TO_BE_TYPED)));
        onData(anything())
                .inAdapterView(withId(R.id.listview_vehicle))
                .atPosition(0)
                .onChildView(withId(R.id.grand_content_layout))
                .onChildView(withId(R.id.licence_color_layout))
                .onChildView(withId(R.id.list_item_licence))
                .check(matches(withText(startsWith(VLICENCE_TO_BE_TYPED))));
        onData(anything())
                .inAdapterView(withId(R.id.listview_vehicle))
                .atPosition(0)
                .onChildView(withId(R.id.grand_content_layout))
                .onChildView(withId(R.id.licence_color_layout))
                .onChildView(withId(R.id.list_item_color))
                .check(matches(withText(startsWith(VCOLOR_TO_BE_TYPED))));

        // Test Modify
        onView(withId(R.id.modify_btn)).perform(click());
        onView(withId(R.id.change_vehicle_color)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(VCOLOR_CHANGED_TO_BE_TYPED))).perform(click());
        onView(withId(R.id.change_vehicle_color)).check(matches(withSpinnerText(containsString(VCOLOR_CHANGED_TO_BE_TYPED))));
        onView(withId(R.id.to_change_vehicle)).perform(click());
        try{
            Thread.sleep(2000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onView(withId(R.id.refresh_vehicle)).perform(click());
        try{
            Thread.sleep(2000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onData(anything())
                .inAdapterView(withId(R.id.listview_vehicle))
                .atPosition(0)
                .onChildView(withId(R.id.grand_content_layout))
                .onChildView(withId(R.id.licence_color_layout))
                .onChildView(withId(R.id.list_item_color))
                .check(matches(withText(startsWith(VCOLOR_CHANGED_TO_BE_TYPED))));
        onData(anything())
                .inAdapterView(withId(R.id.listview_vehicle))
                .atPosition(0)
                .onChildView(withId(R.id.grand_content_layout))
                .onChildView(withId(R.id.licence_color_layout))
                .onChildView(withId(R.id.list_item_licence))
                .check(matches(withText(startsWith(VLICENCE_TO_BE_TYPED))));
        onData(anything())
                .inAdapterView(withId(R.id.listview_vehicle))
                .atPosition(0)
                .onChildView(withId(R.id.grand_content_layout))
                .onChildView(withId(R.id.id_seat_model_layout))
                .onChildView(withId(R.id.delete_btn))
                .perform(click());
        try{
            Thread.sleep(2000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onView(withId(R.id.refresh_vehicle)).perform(click());
        try{
            Thread.sleep(2000L);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Test Delete
        onView(withId(R.id.listview_vehicle))
                .check(matches(not(hasDescendant(withText(VMODEL_TO_BE_TYPED)))));
    }
}
