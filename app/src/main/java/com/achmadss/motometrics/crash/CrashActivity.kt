package com.achmadss.motometrics.crash

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.achmadss.motometrics.R
import com.achmadss.motometrics.ui.theme.MotoMetricsTheme

class CrashActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val exception = GlobalExceptionHandler.getThrowableFromIntent(intent)
        setContent {
            MotoMetricsTheme {
                CrashScreen(
                    icon = Icons.Outlined.BugReport,
                    headingText = "Whoops!",
                    subtitleText = "${stringResource(R.string.app_name)} ran into an unexpected error.",
                    acceptText = "Restart the application",
                    onAcceptClick = ::restartApp,
                    rejectText = "Close",
                    onRejectClick = ::closeApp
                ) {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .clip(MaterialTheme.shapes.small)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                    ) {
                        Text(
                            text = exception.toString(),
                            modifier = Modifier
                                .padding(all = 8.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }

    private fun restartApp() {
        val intent = packageManager.getLaunchIntentForPackage(packageName)?.apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(intent)
        finish()
    }

    private fun closeApp() {
        finish()
    }

}