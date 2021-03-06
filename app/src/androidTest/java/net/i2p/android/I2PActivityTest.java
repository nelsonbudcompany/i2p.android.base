package net.i2p.android;

import android.test.ActivityInstrumentationTestCase2;

import net.i2p.android.router.R;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

public class I2PActivityTest extends ActivityInstrumentationTestCase2<I2PActivity> {
    public I2PActivityTest() {
        super(I2PActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // For each test method invocation, the Activity will not actually be created
        // until the first time this method is called.
        getActivity();
    }

    public void testMainTabs() {
        onView(withId(R.id.router_onoff_button)).check(matches(isDisplayed()));

        // Press "Tunnels" tab
        onView(allOf(withText(R.string.label_tunnels),
                not(isDescendantOfA(withId(R.id.main_scrollview))))).perform(click());
        onView(withId(R.id.router_onoff_button)).check(matches(not(isDisplayed())));
        onView(withText(R.string.label_i2ptunnel_client)).check(matches(isDisplayed()));

        // Press "Addresses" tab
        onView(withText(R.string.label_addresses)).perform(click());
        onView(withText(R.string.label_i2ptunnel_client)).check(matches(not(isDisplayed())));
        onView(withText(R.string.label_router)).check(matches(isDisplayed()));

        // Press "Console" tab
        onView(withText(R.string.label_console)).perform(click());
        // Addressbook fragment should have been destroyed
        onView(withText(R.string.label_router)).check(doesNotExist());
        onView(withId(R.id.router_onoff_button)).check(matches(isDisplayed()));
    }

    public void testMainSwipe() {
        onView(withId(R.id.router_onoff_button)).check(matches(isDisplayed()));

        onView(allOf(withId(R.id.pager), hasSibling(withId(R.id.main_toolbar)))).perform(swipeLeft());
        onView(withId(R.id.router_onoff_button)).check(matches(not(isDisplayed())));
        onView(withText(R.string.label_i2ptunnel_client)).check(matches(isDisplayed()));

        onView(allOf(withId(R.id.pager), hasSibling(withId(R.id.main_toolbar)))).perform(swipeLeft());
        // TODO: test tunnels ViewPager
        onView(allOf(withId(R.id.pager), hasSibling(withId(R.id.main_toolbar)))).perform(swipeLeft());
        onView(withText(R.string.label_i2ptunnel_client)).check(matches(not(isDisplayed())));
        onView(withText(R.string.label_router)).check(matches(isDisplayed()));
        // TODO: test addressbook ViewPager
    }

    public void testSettingsNavigation() {
        // Open settings menu
        openActionBarOverflowOrOptionsMenu(getActivity());
        onView(withText(R.string.menu_settings)).perform(click());

        // Open bandwidth page
        onView(withText(R.string.settings_label_bandwidth_net)).perform(click());
        onView(withText(R.string.settings_label_startOnBoot)).check(matches(isDisplayed()));
        pressBack();

        // Open graphs page
        onView(withText(R.string.label_graphs)).perform(click());
        onView(withText(R.string.router_not_running)).check(matches(isDisplayed()));
        pressBack();

        // Open logging page
        onView(withText(R.string.settings_label_logging)).perform(click());
        onView(withText(R.string.settings_label_default_log_level)).check(matches(isDisplayed()));
        pressBack();

        // Open addressbook page
        onView(withText(R.string.label_addressbook)).perform(click());
        onView(withText("Subscriptions")).check(matches(isDisplayed()));
        closeSoftKeyboard();
        pressBack();

        // Open graphs page
        onView(withText(R.string.settings_label_advanced)).perform(click());
        onView(withText(R.string.settings_label_transports)).check(matches(isDisplayed()));
        pressBack();

        // Check back exits settings
        onView(withText(R.string.settings_label_advanced)).check(matches(isDisplayed()));
        pressBack();
        onView(withText(R.string.settings_label_advanced)).check(doesNotExist());
    }
}
