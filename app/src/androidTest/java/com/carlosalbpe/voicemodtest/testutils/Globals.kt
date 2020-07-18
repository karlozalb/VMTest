package com.carlosalbpe.voicemodtest.testutils

import android.view.View
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

fun waitFor(millis: Long): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return ViewMatchers.isRoot()
        }

        override fun perform(uiController: androidx.test.espresso.UiController, view: View?) {
            uiController.loopMainThreadForAtLeast(millis)
        }

        override fun getDescription(): String {
            return "Wait for $millis milliseconds."
        }

    }
}