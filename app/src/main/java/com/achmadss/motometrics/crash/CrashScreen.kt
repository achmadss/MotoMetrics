package com.achmadss.motometrics.crash


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CrashScreen(
    icon: ImageVector,
    headingText: String,
    subtitleText: String,
    acceptText: String,
    onAcceptClick: () -> Unit,
    rejectText: String,
    onRejectClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Scaffold(
        bottomBar = {
            val strokeWidth = Dp.Hairline
            val borderColor = MaterialTheme.colorScheme.outline
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .drawBehind {
                        drawLine(
                            borderColor,
                            Offset(0f, 0f),
                            Offset(size.width, 0f),
                            strokeWidth.value,
                        )
                    }
                    .windowInsetsPadding(NavigationBarDefaults.windowInsets)
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp,
                    ),
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onAcceptClick,
                ) {
                    Text(text = acceptText)
                }
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onRejectClick,
                ) {
                    Text(text = rejectText)
                }
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .size(48.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = headingText,
                style = MaterialTheme.typography.headlineLarge,
            )
            Text(
                text = subtitleText,
                modifier = Modifier
                    .alpha(.78f)
                    .padding(vertical = 8.dp),
                style = MaterialTheme.typography.titleSmall,
            )

            content()
        }
    }
}

@Preview
@Composable
private fun CrashScaffoldPreview() {
    CrashScreen(
        icon = Icons.Outlined.Info,
        headingText = "Heading",
        subtitleText = "Subtitle",
        acceptText = "Accept",
        onAcceptClick = {},
        rejectText = "Reject",
        onRejectClick = {},
    ) {
        Text("Hello world")
    }
}