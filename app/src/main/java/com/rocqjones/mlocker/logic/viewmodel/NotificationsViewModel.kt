package com.rocqjones.mlocker.logic.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.rocqjones.mlocker.logic.configs.Constants
import com.rocqjones.mlocker.logic.work.NotificationWorker
import java.util.concurrent.TimeUnit

class NotificationsViewModel(application: Application): ViewModel() {

    private val workManager = WorkManager.getInstance(application)

    internal fun scheduleReminder(
        duration: Long,
        unit: TimeUnit
    ) {
        val myWorkRequestBuilder = OneTimeWorkRequestBuilder<NotificationWorker>()
        myWorkRequestBuilder.setInputData(
            workDataOf(
                "NAME" to Constants.Title,
                "MESSAGE" to Constants.MessageBody
            )
        )
        myWorkRequestBuilder.setInitialDelay(duration, unit)
        workManager.enqueue(myWorkRequestBuilder.build())
    }
}

class NotificationsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            NotificationsViewModel(application) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}