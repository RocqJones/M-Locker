# M-Locker
Let's build a device Locking countdown timer app with Jetpack Compose.

### Features.
- **Locking Count Down Timer** that can survive device configuration changes (Restarts and Screen rotations).
- **Notification:** Schedules device locking notification when the remaining time is 3 hours (Kenya) or 2 hours (Uganda).

### Technologies & Libraries used.
- Android Jetpack Compose.
- **ViewModels:** In our case it is responsible for preparing and scheduling (from our Activity) the notification reminder by linking our activity to the Work Manager.
- **Coroutines:** To prevent our app from becoming unresponsive I've used Coroutines to manage long-running tasks that might otherwise block the main thread. For example `LaunchedEffect` allows us to launch a coroutine in response to a composition.
- **WorkManager:** Used this to make sure we schedule asynchronous notifications that need to run even when the app is not in the foreground. We also do not have to worry about managing compatibility.
- State Management: Used `remember` API to store mutable objects of `remainingTime`, `cardColor` and `isNotificationScheduled` to ensure that their value  is stored during initial composition, and the stored value is returned during recomposition.

## Android CICD
Used `Android CI` from the GitHub marketplace for Continuous Integration and Continuous Deployment
- Configured to run Unit Tests.
- We can easily track actions [from GitHub Actions of this repo](https://github.com/RocqJones/M-Locker/actions)

### Android UI Test Results
![image](https://github.com/RocqJones/M-Locker/assets/32324500/005b0832-2ef1-4867-971e-8cd3d02fa3ec)
