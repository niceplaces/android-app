package com.niceplaces.niceplaces.activities

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.niceplaces.niceplaces.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MenuActivityTest {

    @Test
    fun goToActivity(){
        launchActivity<MenuActivity>().use {
            Intents.init()
            onView(withId(R.id.imageview_info)).perform(click())
            intended(hasComponent(InfoActivity::class.java.name))
            pressBack()
            onView(withId(R.id.btn_places_near_you)).perform(click())
            intended(hasComponent(MapsActivity::class.java.name))
            pressBack()
            onView(withId(R.id.btn_explore)).perform(click())
            intended(hasComponent(ExploreActivity::class.java.name))
        }
    }
}