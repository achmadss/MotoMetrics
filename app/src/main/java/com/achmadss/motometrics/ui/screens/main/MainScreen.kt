package com.achmadss.motometrics.ui.screens.main

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.achmadss.motometrics.Routes
import com.achmadss.motometrics.ui.components.dialog.AddTransactionDialog
import com.achmadss.motometrics.ui.components.topbar.SearchTopBar
import com.achmadss.motometrics.ui.screens.main.transaction_tab.TransactionTabScreen
import com.achmadss.motometrics.ui.screens.main.vehicle_tab.VehicleTabScreen

data class AppNavigationBarItem(
    val title: String = "Vehicles",
    val icon: ImageVector = Icons.Default.DirectionsCar,
    val tabType: TabType = TabType.Vehicle,
    val onClick: () -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.routeMain(
    navController: NavController,
) {
    composable(
        route = Routes.MAIN
    ) {
        val viewModel = viewModel<MainScreenViewModel>()
        val currentTab by viewModel.currentTab.collectAsState()
        val vehicleTabUIState by viewModel.vehicleTabUIState.collectAsState()
        val transactionTabUIState by viewModel.transactionTabUIState.collectAsState()
        val context = LocalContext.current
        val navigationBarItems = listOf(
            AppNavigationBarItem { viewModel.changeTab(TabType.Vehicle) },
            AppNavigationBarItem(
                "Transactions", Icons.Default.Receipt, TabType.Transaction
            ) { viewModel.changeTab(TabType.Transaction) },
        )
        LaunchedEffect(Unit) {
            viewModel.getAllVehicles()
            viewModel.getAllTransactionsWithVehicles()
        }
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                 when(currentTab) {
                     TabType.Vehicle -> {
                         SearchTopBar(
                             title = "Vehicles",
                             onSearch = { }
                         ) {
                             IconButton(onClick = { navController.navigate(Routes.ADD_VEHICLE) }) {
                                 Icon(imageVector = Icons.Default.Add, contentDescription = "Add Vehicle")
                             }
                         }
                     }
                     TabType.Transaction -> {
                         TopAppBar(
                             modifier = Modifier,
                             title = { Text(text = "Transactions") },
                             actions = {
                                 IconButton(onClick = { viewModel.changeAddTransactionDialogVisibility(true) }) {
                                     Icon(imageVector = Icons.Default.Add, contentDescription = "")
                                 }
                             }
                         )
                     }
                 }
            },
            bottomBar = {
                NavigationBar {
                    navigationBarItems.forEach {
                        NavigationBarItem(
                            selected = it.tabType == currentTab,
                            onClick = it.onClick,
                            icon = {
                                Icon(imageVector = it.icon, contentDescription = it.title)
                            },
                            label = {
                                Text(text = it.title)
                            }
                        )
                    }
                }
            }
        ) {
            when(currentTab) {
                TabType.Vehicle -> {
                    VehicleTabScreen(
                        vehicles = vehicleTabUIState.vehicleInfos,
                        loading = vehicleTabUIState.loading,
                        contentPadding = it,
                        onVehicleClick = {

                        },
                        onRefresh = { viewModel.getAllVehicles() }
                    )
                }
                TabType.Transaction -> {
                    AddTransactionDialog(
                        show = transactionTabUIState.showAddTransactionDialog,
                        cars = transactionTabUIState.cars,
                        motorcycles = transactionTabUIState.motorcycles,
                        onDismissRequest = { viewModel.changeAddTransactionDialogVisibility(false) },
                        onConfirm = { vehicleId, vehicleType ->
                            viewModel.createNewTransaction(vehicleId, vehicleType) { success, message ->
                                if (!success) Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                else viewModel.changeAddTransactionDialogVisibility(false)
                            }
                        }
                    )
                    TransactionTabScreen(
                        transactionsWithVehicles = transactionTabUIState.transactionsWithVehicles,
                        loading = transactionTabUIState.loading,
                        contentPadding = it,
                        onRefresh = { viewModel.getAllTransactionsWithVehicles() },
                    )
                }
            }
        }
    }
}