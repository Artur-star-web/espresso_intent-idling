package ru.kkuzmichev.simpleappforespresso;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EspressoTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {
        Intents.init();
        IdlingRegistry.getInstance().register(SimpleIdlingResource.getIdlingResource());
    }

    @After
    public void cleanup() {
        Intents.release();
        IdlingRegistry.getInstance().unregister(SimpleIdlingResource.getIdlingResource());
    }

    @Test
    public void testHomeFragmentText() {
        ViewInteraction mainText = onView(
                withId(R.id.text_home)
        );

        mainText.check(
                matches(
                        withText("This is home fragment")
                )
        );
    }

    @Test
    public void testSettingsIntent() {
        onView(withContentDescription("More options"))
                .perform(click());

        onView(withText("Settings"))
                .check(matches(isDisplayed()));

        onView(withText("Settings"))
                .perform(click());

        intended(hasAction(Intent.ACTION_VIEW));
    }

    @Test
    public void testGalleryWithIdlingResource() {
        onView(withContentDescription("Open navigation drawer"))
                .perform(click());

        onView(withId(R.id.nav_gallery))
                .perform(click());


        onView(withId(R.id.recycle_view))
                .check(matches(isDisplayed()));

        onView(withText("7"))
                .check(matches(isDisplayed()));
    }
}