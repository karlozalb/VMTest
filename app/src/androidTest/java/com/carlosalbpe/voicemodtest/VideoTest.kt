package com.carlosalbpe.voicemodtest

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.carlosalbpe.voicemodtest.testutils.CustomAction
import com.carlosalbpe.voicemodtest.testutils.waitFor
import com.carlosalbpe.voicemodtest.ui.videolistfragment.VideoAdapter
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class VideoTest {

    @get:Rule
    var intentsRule: IntentsTestRule<MainActivity> = IntentsTestRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.carlosalbpe.voicemodtest", appContext.packageName)
    }

    @Test
    fun recordVideoWithFrontCamera(){
        recordVideoWithCamera(true)
    }

    @Test
    fun recordVideoWithBackCamera(){
        recordVideoWithCamera(false)
    }

    fun recordVideoWithCamera(isFront : Boolean){
        Espresso.onView(isRoot()).perform(waitFor(2000))
        Espresso.onView(withId(R.id.videos_rv))

        var previousCount = countRecycleViewItems()

        Espresso.onView(withId(R.id.fab)).perform(ViewActions.click())

        //Front camera.
        if (isFront) {
            Espresso.onView(withId(R.id.frontCameraBtn)).perform(ViewActions.click())
        }else{  //Back camera.
            Espresso.onView(withId(R.id.backCameraBtn)).perform(ViewActions.click())
        }

        //To check if CameraFragment is displayed
        Espresso.onView(withId(R.id.view_finder))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        //Start recording
        Espresso.onView(withId(R.id.recording_button)).perform(ViewActions.click())
        Espresso.onView(isRoot()).perform(waitFor(5000))
        Espresso.onView(withId(R.id.recording_button)).perform(ViewActions.click())

        Espresso.onView(isRoot()).perform(waitFor(2000))
        //Check if we're back in VideoListFragment
        Espresso.onView(withId(R.id.videos_rv))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        var currentCount = countRecycleViewItems()

        assertEquals(previousCount + 1, currentCount)
    }

    @Test
    fun deleteVideoTest(){
        Espresso.onView(isRoot()).perform(waitFor(2000))
        Espresso.onView(withId(R.id.videos_rv))

        var previousCount = countRecycleViewItems()

        Espresso.onView(withId(R.id.videos_rv))
            .perform(RecyclerViewActions.actionOnItemAtPosition<VideoAdapter.VideoViewHolder>(0, CustomAction.clickChildViewWithId(R.id.delete_video)));

        Espresso.onView(isRoot()).perform(waitFor(2000))

        var currentCount = countRecycleViewItems()

        assertEquals(previousCount - 1, currentCount)
    }

    fun countRecycleViewItems() : Int{
        var recyclerView = intentsRule.activity.findViewById(R.id.videos_rv) as RecyclerView
        return recyclerView.adapter!!.itemCount
    }


}


