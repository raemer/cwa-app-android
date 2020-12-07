package de.rki.coronawarnapp.notification

import android.content.Context
import androidx.navigation.NavDeepLinkBuilder
import de.rki.coronawarnapp.R
import de.rki.coronawarnapp.ui.main.MainActivity
import de.rki.coronawarnapp.util.ForegroundState
import de.rki.coronawarnapp.util.di.AppContext
import de.rki.coronawarnapp.util.formatter.TestResult
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Provider

class TestResultAvailableNotification @Inject constructor(
    @AppContext private val context: Context,
    private val foregroundState: ForegroundState,
    private val navDeepLinkBuilderProvider: Provider<NavDeepLinkBuilder>,
    private val notificationHelper: NotificationHelper
) {

    suspend fun showTestResultNotification(testResult: TestResult) {
        if (foregroundState.isInForeground.first()) return

        val pendingIntent = navDeepLinkBuilderProvider.get().apply {
            setGraph(R.navigation.nav_graph)
            setComponentName(MainActivity::class.java)
            setDestination(getNotificationDestination(testResult))
        }.createPendingIntent()

        notificationHelper.sendNotification(
            title = context.getString(R.string.notification_headline_test_result_ready),
            content = context.getString(R.string.notification_body_test_result_ready),
            notificationId = NotificationConstants.TEST_RESULT_AVAILABLE_NOTIFICATION_ID,
            pendingIntent = pendingIntent
        )
    }

    fun getNotificationDestination(testResult: TestResult): Int {
        return if (testResult == TestResult.POSITIVE) {
            R.id.submissionTestResultAvailableFragment
        } else {
            R.id.submissionResultFragment
        }
    }
}
