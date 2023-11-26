package com.achmadss.motometrics.ui.screens.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.achmadss.motometrics.Routes
import com.achmadss.motometrics.ui.components.topbar.DefaultTabTopBar
import com.achmadss.motometrics.ui.screens.main.vehicle_tab.VehicleTabScreen

data class AppNavigationBarItem(
    val title: String = "Vehicles",
    val icon: ImageVector = Icons.Default.DirectionsCar,
    val tabType: TabType = TabType.Vehicle,
    val onClick: () -> Unit,
)

fun NavGraphBuilder.routeMain(
    navController: NavController,
) {
    composable(
        route = Routes.MAIN
    ) {
        val viewModel = viewModel<MainScreenViewModel>()
        val currentTab by viewModel.currentTab.collectAsState()
        val vehicleTabUIState by viewModel.vehicleTabUIState.collectAsState()
        val navigationBarItems = listOf(
            AppNavigationBarItem { viewModel.changeTab(TabType.Vehicle) },
            AppNavigationBarItem(
                "Transactions", Icons.Default.Receipt, TabType.Transaction
            ) { viewModel.changeTab(TabType.Transaction) },
        )
        LaunchedEffect(Unit) {
            viewModel.getAllVehicles()
        }
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                 when(currentTab) {
                     TabType.Vehicle -> {
                         DefaultTabTopBar(
                             title = "Vehicles",
                             onSearch = { }
                         ) {
                             IconButton(onClick = { navController.navigate(Routes.ADD_VEHICLE) }) {
                                 Icon(imageVector = Icons.Default.Add, contentDescription = "Add Vehicle")
                             }
                         }
                     }
                     TabType.Transaction -> TODO()
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
                        vehicles = vehicleTabUIState.vehicles,
                        loading = vehicleTabUIState.loading,
                        contentPadding = it,
                        onVehicleClick = {

                        },
                        onRefresh = { viewModel.getAllVehicles() }
                    )
                }
                TabType.Transaction -> { /*TODO*/ }
            }
        }
    }
}