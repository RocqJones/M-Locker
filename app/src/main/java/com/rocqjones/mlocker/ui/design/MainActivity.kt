package com.rocqjones.mlocker.ui.design

import android.app.Application
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rocqjones.mlocker.logic.data.source.ActiveUsagePeriodDataSource
import com.rocqjones.mlocker.logic.data.source.CountryIsoCodeDataSource
import com.rocqjones.mlocker.logic.utils.HelperUtil
import com.rocqjones.mlocker.logic.viewmodel.NotificationsViewModel
import com.rocqjones.mlocker.logic.viewmodel.NotificationsViewModelFactory
import com.rocqjones.mlocker.ui.theme.MLockerTheme
import com.rocqjones.mlocker.ui.theme.cardGreen
import com.rocqjones.mlocker.ui.theme.cardOrange
import com.rocqjones.mlocker.ui.theme.cardRed
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.OffsetDateTime
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    /*
    private val viewModel: NotificationsViewModel by viewModels {
        NotificationsViewModelFactory(this.application)
    }
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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
}

@Composable
fun ShowCountDownTimer(
    activeUsagePeriodDataSource: ActiveUsagePeriodDataSource,
    countryIsoCodeDataSource : CountryIsoCodeDataSource,
    modifier: Modifier = Modifier
) {
    // register Vm
    val viewModel: NotificationsViewModel = viewModel(
        factory = NotificationsViewModelFactory(
            LocalContext.current.applicationContext as Application
        )
    )

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CardContent(activeUsagePeriodDataSource, countryIsoCodeDataSource, viewModel)
    }
}

@Composable
fun CardContent(
    activeUsagePeriodDataSource: ActiveUsagePeriodDataSource,
    countryIsoCodeDataSource: CountryIsoCodeDataSource,
    viewModel: NotificationsViewModel,
) {
    val remainingTime: MutableState<String> = remember { mutableStateOf("") }
    val helperUtil = HelperUtil()
    var cardColor: Color by remember { mutableStateOf(cardGreen) }

    // Flag to track if notification is scheduled
    var isNotificationScheduled by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val lockingInfo = activeUsagePeriodDataSource.getLockingInfo()
        val currentTime = OffsetDateTime.now()
        var remainingDuration = Duration.between(currentTime, lockingInfo.lockTime)
        val country = countryIsoCodeDataSource.getCountryIsoCode()

        Log.d(this.javaClass.simpleName, "remainingDuration ${helperUtil.formatDuration(remainingDuration)}")

        while (remainingDuration.isNegative.not()) {
            remainingTime.value = helperUtil.formatDuration(remainingDuration)

            cardColor = when {
                remainingDuration.toHours() <= 3L -> cardOrange
                remainingDuration.toHours() <= 0L -> cardRed
                else -> cardGreen
            }

            when {
                remainingDuration.toHours() == 3L && country.code == "KE" || remainingDuration.toHours() == 2L && country.code == "UG" -> {
                    if (!isNotificationScheduled) {
                        scheduleReminder(5, TimeUnit.SECONDS, viewModel)
                        isNotificationScheduled = true
                    }
                }
                else -> {
                    isNotificationScheduled = false
                }
            }

            delay(1000)
            remainingDuration = remainingDuration.minusSeconds(1)
        }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = cardColor),
        modifier = Modifier.wrapContentSize()
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .border(1.dp, Color.Black, shape = MaterialTheme.shapes.medium)
    ) {
        Text(
            text = remainingTime.value,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(vertical = 32.dp, horizontal = 32.dp)
        )
    }
}

// Function to schedule a reminder notification
fun scheduleReminder(duration: Long, unit: TimeUnit, viewModel: NotificationsViewModel) {
    val executor = Executors.newScheduledThreadPool(1)
    executor.schedule({
        viewModel.scheduleReminder(duration, unit)
    }, duration, unit)
}

@Preview(
    showBackground = true,
    widthDp = 250,
    heightDp = 450,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "DefaultPreviewLight"
)
@Preview(
    showBackground = true,
    widthDp = 250,
    heightDp = 450,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Composable
fun CountDownPreview() {
    MLockerTheme {
        val activeUsagePeriodDataSource = ActiveUsagePeriodDataSource()
        val countryIsoCodeDataSource = CountryIsoCodeDataSource()

        ShowCountDownTimer(activeUsagePeriodDataSource, countryIsoCodeDataSource)
    }
}