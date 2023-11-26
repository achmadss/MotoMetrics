package com.achmadss.motometrics.ui.screens.vehicle_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.achmadss.data.entities.Car
import com.achmadss.data.entities.Motorcycle
import com.achmadss.data.entities.Transaction
import com.achmadss.data.entities.base.Vehicle
import com.achmadss.data.entities.base.VehicleType
import com.achmadss.motometrics.Routes
import com.achmadss.motometrics.ui.components.topbar.DefaultDetailTopBar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun NavGraphBuilder.routeVehicleDetail(
    navController: NavController
) {
    composable(
        route = "${Routes.VEHICLE}/{id}?vehicleType={type}",
        arguments = listOf(
            navArgument("id") { NavType.StringType },
            navArgument("type") { NavType.StringType },
        )
    ) {
        val id = it.arguments?.getString("id")?.toLongOrNull()
        val type = VehicleType.valueOf(it.arguments?.getString("type").orEmpty())
        val viewModel = viewModel<VehicleDetailViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        LaunchedEffect(Unit) {
            viewModel.getVehicleWithTransactions(id, type)
        }
        VehicleDetailScreen(
            loading = uiState.loading,
            vehicle = uiState.vehicle,
            errorMessage = uiState.errorMessage,
            transactions = uiState.transactions
        ) {
            navController.popBackStack()
        }
    }
}

@Composable
fun VehicleDetailScreen(
    loading: Boolean,
    vehicle: Vehicle?,
    errorMessage: String,
    transactions: List<Transaction>,
    onBackPress: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DefaultDetailTopBar(title = "Car Detail", onBackPress = onBackPress) {}
        }
    ) {
        if (errorMessage.isNotBlank()) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .wrapContentSize(Alignment.Center)
            ) {
                Text(text = errorMessage)
            }
        }
        if (loading) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator()
            }
        } else if (vehicle != null) {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(it)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                VehicleDetailItem(label = "Name", value = vehicle.name)
                VehicleDetailItem(label = "Vehicle Type", value = vehicle.vehicleType.name)
                VehicleDetailItem(label = "Stock", value = vehicle.stock.toString())
                VehicleDetailItem(label = "Release Date", value = vehicle.releaseDate.format(
                    DateTimeFormatter.ofPattern("dd MMMM yyyy")))
                VehicleDetailItem(label = "Color", value = vehicle.color)
                if (vehicle is Car) {
                    VehicleDetailItem(label = "Engine", value = vehicle.engine)
                    VehicleDetailItem(label = "Capacity", value = vehicle.capacity.toString())
                    VehicleDetailItem(label = "Type", value = vehicle.type)
                }
                if (vehicle is Motorcycle) {
                    VehicleDetailItem(label = "Engine", value = vehicle.engine)
                    VehicleDetailItem(label = "Suspension", value = vehicle.suspension)
                    VehicleDetailItem(label = "Transmission", value = vehicle.transmission)
                }
                VehicleDetailItem(label = "Name", value = vehicle.name)
                VehicleDetailItem(label = "Price", value = vehicle.price.toString())
                VehicleDetailTransactions(transactions, vehicle.price)
            }
        }
    }
}

@Composable
fun VehicleDetailItem(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun VehicleDetailTransactions(transactions: List<Transaction>, price: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Transactions",
            style = MaterialTheme.typography.bodyMedium
        )
        Box(
            modifier = Modifier
                .heightIn(max = 200.dp, min = 150.dp)
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.inverseOnSurface)
                .padding(vertical = 4.dp)
        ) {
            if (transactions.isEmpty()) {
                Text(text = "No Transactions found", modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(4.dp)) }
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(text = "ID", style = MaterialTheme.typography.labelSmall)
                            Text(text = "Created At", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    items(transactions) {
                        VehicleDetailTransactionItem(it.id, it.createdAt)
                    }
                    item { Spacer(modifier = Modifier.height(4.dp)) }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total Sold",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = transactions.size.toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total Sales",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = transactions.size.times(price).toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun VehicleDetailTransactionItem(id: Long, createdAt: LocalDateTime) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = id.toString())
        Text(text = createdAt.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")))
    }
}