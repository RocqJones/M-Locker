package com.rocqjones.mlocker

import android.app.NotificationManager
import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.rocqjones.mlocker.logic.data.source.ActiveUsagePeriodDataSource
import com.rocqjones.mlocker.logic.data.source.CountryIsoCodeDataSource
import com.rocqjones.mlocker.logic.viewmodel.NotificationsViewModel
import com.rocqjones.mlocker.logic.viewmodel.NotificationsViewModelFactory
import com.rocqjones.mlocker.ui.design.MainActivity
import com.rocqjones.mlocker.ui.design.ShowCountDownTimer
import com.rocqjones.mlocker.ui.theme.MLockerTheme
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        // Set up your Compose UI here
        composeTestRule.setContent {
            MLockerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val activeUsagePeriodDataSource = ActiveUsagePeriodDataSource()
                    val countryIsoCodeDataSource = CountryIsoCodeDataSource()
                    ShowCountDownTimer(activeUsagePeriodDataSource, countryIsoCodeDataSource)
                }
            }
        }
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.rocqjones.mlocker", appContext.packageName)
    }

    @Test
    fun testComposableShowCountDownTimer() {
        composeTestRule.onNodeWithTag("composeCountdownBox")
    }

    @Test
    fun testComposableCard() {
        composeTestRule.onNodeWithTag("composeCardMain")
    }

    @Test
    fun testComposableCounterText() {
        composeTestRule.onNodeWithTag("composeCounterText")
    }

    @Test
    fun testComposableNotification() {
        val notificationsViewModel = NotificationsViewModelFactory(
            ApplicationProvider.getApplicationContext()
        ).create(NotificationsViewModel::class.java)

        notificationsViewModel.scheduleReminder(5, TimeUnit.SECONDS)

        // Wait for 8 seconds
        Thread.sleep(TimeUnit.SECONDS.toMillis(8))

        // Check if the notification appears
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val notificationExists = device.wait(
            Until.hasObject(By.pkg("com.rocqjones.reminderapp").depth(0)),
            TimeUnit.SECONDS.toMillis(10)
        )
        Assert.assertTrue("Notification should appear", notificationExists != null)

        // Click the notification
        val notification = device.findObject(By.text("Reminder App."))
        notification?.click()

        // Verify that the MainActivity is launched
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.waitForIdleSync()
        val currentActivity = instrumentation.targetContext.packageManager.getLaunchIntentForPackage(
            instrumentation.targetContext.packageName
        )?.component?.className
        Assert.assertEquals(
            "MainActivity should be launched",
            MainActivity::class.java.name,
            currentActivity
        )

        // Clear the notification
        val notificationManager = instrumentation.targetContext.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        notificationManager.cancel(17)
    }
}