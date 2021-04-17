package com.main.climbingdiary.fragments;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.main.climbingdiary.R;
import com.main.climbingdiary.activities.MainActivity;
import com.main.climbingdiary.testhelper.RecyclerViewMatcher;
import com.main.climbingdiary.testhelper.TestHelper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;
import java.util.UUID;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4ClassRunner.class)
public class RouteDoneFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    private TestHelper testHelper;

    private void init() {
        testHelper = new TestHelper();
        testHelper.clearDb();
        testHelper.createTestSources();
    }

    private void clean() {
        testHelper.clearDb();
    }

    private RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Test
    public void clickDeleteRouteButton() {
        final int recyclerViewId = R.id.rvRoutes;
        final int number = new Random().nextInt(5);

        init();

        onView(withId(R.id.nav_drawer_layout)).perform(swipeLeft());
        // Click item
        onView(withRecyclerView(recyclerViewId)
                .atPosition(number)).perform(click());

        onView(allOf(withId(R.id.route_delete), isDisplayed())).perform(click());

        onView(allOf(withId(R.id.confirm_button), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.confirm_button), isDisplayed())).perform(click());

        clean();
    }

    @Test
    public void clickEditRouteBotton() throws InterruptedException {

        final int recyclerViewId = R.id.rvRoutes;
        final int number = new Random().nextInt(5);
        final String changeRouteName = UUID.randomUUID().toString();

        init();

        onView(withId(R.id.nav_drawer_layout)).perform(swipeLeft());

        // Click item
        onView(withRecyclerView(recyclerViewId)
                .atPosition(number)).perform(click());

        //open edit route item
        onView(allOf(withId(R.id.route_edit), isDisplayed())).perform(click());

        onView(withId(R.id.input_route_name)).perform(replaceText(changeRouteName));

        onView(withId(R.id.input_route_save)).perform(click());

        assertTrue(testHelper.checkIfRouteNameExistsInList(changeRouteName));

        clean();
    }
}
