package com.achmadss.motometrics.ui.screens.main.transaction_tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.achmadss.data.entities.TransactionWithVehicle
import com.achmadss.motometrics.utils.formatPattern
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TransactionTabScreen(
    modifier: Modifier = Modifier,
    transactionsWithVehicles: List<TransactionWithVehicle>,
    loading: Boolean,
    contentPadding: PaddingValues,
    onRefresh: () -> Unit,
) {
    val state = rememberPullRefreshState(loading, onRefresh)
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 8.dp),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(state),
            verticalArrangement = if (transactionsWithVehicles.isEmpty())
                Arrangement.Center else Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (transactionsWithVehicles.isEmpty()) {
                item { Text(text = "No Transaction found") }
            } else {
                items(transactionsWithVehicles) {
                    TransactionTabItem(transactionWithVehicle = it)
                }
            }
        }
        PullRefreshIndicator(loading, state, Modifier.align(Alignment.TopCenter))
    }

}

@Composable
fun TransactionTabItem(transactionWithVehicle: TransactionWithVehicle) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = transactionWithVehicle.vehicle?.name ?: "Unknown",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = transactionWithVehicle.transaction.createdAt.formatPattern(
                    "dd MMMM yyyy HH:mm:ss"
                ),
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "ID",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "${transactionWithVehicle.transaction.id}",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}