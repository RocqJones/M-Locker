package com.rocqjones.mlocker.ui.design

import android.content.res.Configuration
import android.os.Bundle
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rocqjones.mlocker.logic.utils.HelperUtil
import com.rocqjones.mlocker.logic.data.source.ActiveUsagePeriodDataSource
import com.rocqjones.mlocker.ui.theme.MLockerTheme
import com.rocqjones.mlocker.ui.theme.cardGreen
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.OffsetDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MLockerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val activeUsagePeriodDataSource = ActiveUsagePeriodDataSource()
                    ShowCountDownTimer(activeUsagePeriodDataSource)
                }
            }
        }
    }
}

@Composable
fun ShowCountDownTimer(
    activeUsagePeriodDataSource: ActiveUsagePeriodDataSource,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = cardGreen
            ),
            modifier = Modifier.wrapContentSize()
                .padding(vertical = 4.dp, horizontal = 4.dp)
                .border(1.dp, Color.Black, shape = MaterialTheme.shapes.medium)
        ) {
            CardContent(activeUsagePeriodDataSource)
        }
    }
}

@Composable
fun CardContent(activeUsagePeriodDataSource: ActiveUsagePeriodDataSource) {
    val remainingTime: MutableState<String> = remember { mutableStateOf("") }
    val helperUtil = HelperUtil()

    LaunchedEffect(Unit) {
        val lockingInfo = activeUsagePeriodDataSource.getLockingInfo()
        val currentTime = OffsetDateTime.now()
        var remainingDuration = Duration.between(currentTime, lockingInfo.lockTime)

        while (remainingDuration.isNegative.not()) {
            remainingTime.value = helperUtil.formatDuration(remainingDuration)
            delay(1000)
            remainingDuration = remainingDuration.minusSeconds(1) // Assign the updated duration back to remainingDuration
        }
    }

    Text(
        text = remainingTime.value,
        style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.SemiBold
        ),
        modifier = Modifier.padding(vertical = 32.dp, horizontal = 32.dp)
    )
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
        ShowCountDownTimer(activeUsagePeriodDataSource)
    }
}