package com.achmadss.motometrics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.achmadss.motometrics.ui.screens.add_vehicle.routeAddVehicle
import com.achmadss.motometrics.ui.screens.main.routeMain
import com.achmadss.motometrics.ui.screens.vehicle_detail.routeVehicleDetail
import com.achmadss.motometrics.ui.theme.MotoMetricsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MotoMetricsTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Routes.MAIN
                ) {
                    routeMain(navController)
                    routeAddVehicle(navController)
                    routeVehicleDetail(navController)
                }
            }
        }
    }
}