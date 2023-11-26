package com.achmadss.motometrics.ui.components.dialog

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.achmadss.data.entities.Car
import com.achmadss.data.entities.Motorcycle
import com.achmadss.data.entities.base.Vehicle
import com.achmadss.data.entities.base.VehicleType
import com.achmadss.motometrics.ui.theme.MotoMetricsTheme
import java.time.format.DateTimeFormatter

@Composable
fun AddTransactionDialog(
    show: Boolean,
    vehicles: List<Vehicle>,
    onDismissRequest: () -> Unit,
    onConfirm: (Long, VehicleType) -> Unit,
) {
    if (show) {
        var selectedVehicleType by remember { mutableStateOf(VehicleType.CAR) }
        var selectedVehicle by remember { mutableStateOf<Vehicle?>(null) }
        var isSelectedVehicleError by remember { mutableStateOf(false) }
        val filteredVehicles = vehicles.filter { it.vehicleType == selectedVehicleType }

        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = "Add New Transaction") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VehicleTypeDropdown(selectedVehicleType) { vehicleType ->
                        selectedVehicleType = vehicleType
                        selectedVehicle = null
                    }
                    VehicleDropdown(
                        vehicles = filteredVehicles,
                        selectedVehicle = selectedVehicle,
                        onVehicleSelected = { vehicle ->
                            selectedVehicle = vehicle
                        },
                        isError = isSelectedVehicleError,
                        onDismissError = { isSelectedVehicleError = false }
                    )
                    AddTransactionVehicleDetail(selectedVehicle)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (selectedVehicle != null) {
                        if (selectedVehicle is Car)
                            onConfirm((selectedVehicle as Car).id, selectedVehicleType)
                        else if (selectedVehicle is Motorcycle)
                            onConfirm((selectedVehicle as Motorcycle).id, selectedVehicleType)
                    } else {
                        isSelectedVehicleError = true
                    }
                }) {
                    Text(text = "Create")
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleTypeDropdown(
    selectedVehicleType: VehicleType,
    onVehicleTypeSelected: (VehicleType) -> Unit
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    val vehicleTypes = listOf(VehicleType.CAR, VehicleType.MOTORCYCLE)

    ExposedDropdownMenuBox(
        expanded = dropdownExpanded,
        onExpandedChange = { dropdownExpanded = !dropdownExpanded }
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(),
            value = selectedVehicleType.name,
            onValueChange = { },
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(dropdownExpanded)
            },
        )
        ExposedDropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { dropdownExpanded = false }
        ) {
            vehicleTypes.forEach { vehicleType ->
                DropdownMenuItem(
                    text = { Text(text = vehicleType.name) },
                    onClick = {
                        onVehicleTypeSelected(vehicleType)
                        dropdownExpanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDropdown(
    vehicles: List<Vehicle>,
    selectedVehicle: Vehicle?,
    onVehicleSelected: (Vehicle) -> Unit,
    isError: Boolean,
    onDismissError: () -> Unit
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    val selectedVehicleName = selectedVehicle?.name ?: ""

    ExposedDropdownMenuBox(
        expanded = dropdownExpanded,
        onExpandedChange = {
            dropdownExpanded = !dropdownExpanded
            if (dropdownExpanded) onDismissError()
        }
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(),
            value = selectedVehicleName,
            onValueChange = { },
            readOnly = true,
            placeholder = { Text(text = "Select a vehicle") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(dropdownExpanded)
            },
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(
                        text = "Please select a vehicle",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
        ExposedDropdownMenu(
            modifier = Modifier
                .heightIn(max = TextFieldDefaults.MinHeight.times(3))
                .width(TextFieldDefaults.MinWidth)
                .verticalScroll(rememberScrollState()),
            expanded = dropdownExpanded,
            onDismissRequest = { dropdownExpanded = false }
        ) {
            vehicles.forEach { vehicle ->
                DropdownMenuItem(
                    text = { Text(text = vehicle.name) },
                    onClick = {
                        onVehicleSelected(vehicle)
                        dropdownExpanded = false
                    }
                )
            }
        }
    }
}
@Composable
fun AddTransactionVehicleDetail(
    vehicle: Vehicle?,
) {
    if (vehicle != null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            AddTransactionVehicleDetailItem("Name", vehicle.name)
            AddTransactionVehicleDetailItem("Stock", "${vehicle.stock}")
            AddTransactionVehicleDetailItem(
                "Release Date",
                vehicle.releaseDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
            )
            AddTransactionVehicleDetailItem("Color", vehicle.color)
            AddTransactionVehicleDetailItem("Price", "${vehicle.price}")
            when(vehicle) {
                is Car -> {
                    AddTransactionVehicleDetailItem("Engine", vehicle.engine)
                    AddTransactionVehicleDetailItem("Capacity", "${vehicle.capacity}")
                    AddTransactionVehicleDetailItem("Type", vehicle.type)
                }
                is Motorcycle -> {
                    AddTransactionVehicleDetailItem("Engine", vehicle.engine)
                    AddTransactionVehicleDetailItem("Suspension", vehicle.suspension)
                    AddTransactionVehicleDetailItem("Transmission", vehicle.transmission)
                }
            }
        }
    }
}
@Composable
fun AddTransactionVehicleDetailItem(
    label: String, value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview
@Composable
fun PreviewAddTransactionDialog() {
    MotoMetricsTheme {
        AddTransactionDialog(
            show = true,
            vehicles = listOf(),
            onDismissRequest = {  },
            onConfirm = { _, _ -> }
        )
    }
}