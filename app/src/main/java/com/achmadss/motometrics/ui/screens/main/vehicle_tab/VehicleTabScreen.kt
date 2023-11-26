package com.achmadss.motometrics.ui.screens.main.vehicle_tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.achmadss.data.entities.base.VehicleInfo
import com.achmadss.data.entities.base.VehicleType
import com.achmadss.motometrics.ui.theme.MotoMetricsTheme
import java.time.LocalDateTime
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VehicleTabScreen(
    modifier: Modifier = Modifier,
    vehicles: List<VehicleInfo>,
    loading: Boolean,
    contentPadding: PaddingValues,
    onVehicleClick: (Long) -> Unit,
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
            verticalArrangement = if (vehicles.isEmpty())
                Arrangement.Center else Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (vehicles.isEmpty()) {
                item { Text(text = "No Vehicles found") }
            } else {
                items(vehicles) {
                    VehicleTabItem(vehicle = it, onVehicleClick = onVehicleClick)
                }
            }
        }
        PullRefreshIndicator(loading, state, Modifier.align(Alignment.TopCenter))

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleTabItem(
    vehicle: VehicleInfo,
    onVehicleClick: (Long) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        onClick = { onVehicleClick(vehicle.id) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = vehicle.vehicleType.name.lowercase()
                        .replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                            else it.toString()
                        },
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = vehicle.name,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Stock",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = "${vehicle.stock}",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewVehicleTabScreen() {
    MotoMetricsTheme {
        VehicleTabScreen(
            modifier = Modifier.background(Color.White),
            vehicles = listOf(
                VehicleInfo(1L, VehicleType.CAR, "Avanza", 10, LocalDateTime.now()),
                VehicleInfo(1L, VehicleType.CAR, "Avanza", 10, LocalDateTime.now()),
                VehicleInfo(1L, VehicleType.CAR, "Avanza", 10, LocalDateTime.now()),
                VehicleInfo(1L, VehicleType.CAR, "Avanza", 10, LocalDateTime.now()),
            ),
            loading = false,
            contentPadding = ScaffoldDefaults.contentWindowInsets.asPaddingValues(),
            onVehicleClick = { },
            onRefresh = { },
        )
    }
}


@Preview
@Composable
fun PreviewVehicleTabItem() {
    MotoMetricsTheme {
        VehicleTabItem(vehicle = VehicleInfo(1L, VehicleType.CAR,"Avanza", 10, LocalDateTime.now())) {}
    }
}