package com.main.climbingdiary.fragments;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.main.climbingdiary.R;
import com.main.climbingdiary.activities.MainActivity;
import com.main.climbingdiary.common.preferences.AppPreferenceManager;
import com.main.climbingdiary.models.SportType;
import com.main.climbingdiary.testhelper.TestHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;

public class StatisticFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityRule =
            new ActivityScenarioRule<>(MainActivity.class);
    private TestHelper testHelper;

    @Before
    public void init() {
        testHelper = new TestHelper();
        assertTrue(testHelper.createTestSources());
    }

    @After
    public void clean() {
        assertTrue(testHelper.clearDb());
    }


    @Test
    public void onClickChartButtonOk() {
        for (SportType type : SportType.values()) {
            AppPreferenceManager.setSportType(type);
            onView(withId(R.id.btn_dev)).perform(click());
            onView(withId(R.id.btn_stat)).perform(click());
            onView(withId(R.id.btn_table)).perform(click());
        }
    }
}
