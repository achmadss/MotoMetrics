package com.achmadss.motometrics.ui.components.dialog

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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.achmadss.data.entities.Car
import com.achmadss.data.entities.Motorcycle
import com.achmadss.data.entities.base.VehicleType
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    show: Boolean,
    cars: List<Car>,
    motorcycles: List<Motorcycle>,
    onDismissRequest: () -> Unit,
    onConfirm: (Long, VehicleType) -> Unit,
) {
    if (show) {
        var dropdownVehicleTypeExpanded by remember { mutableStateOf(false) }
        var dropdownVehicleExpanded by remember { mutableStateOf(false) }
        var selectedVehicleType by remember { mutableStateOf(VehicleType.CAR) }
        var selectedVehicleName by remember { mutableStateOf("") }
        var selectedCar by remember { mutableStateOf<Car?>(null) }
        var selectedMotorcycle by remember { mutableStateOf<Motorcycle?>(null) }
        var selectedVehicleId by remember { mutableLongStateOf(0L) }
        var isSelectedVehicleError by remember { mutableStateOf(false) }
        val vehicleTypes = listOf(VehicleType.CAR, VehicleType.MOTORCYCLE)

        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = "Add New Transaction") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = dropdownVehicleTypeExpanded,
                        onExpandedChange = { dropdownVehicleTypeExpanded = !dropdownVehicleTypeExpanded }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.menuAnchor(),
                            value = selectedVehicleType.name,
                            onValueChange = { },
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(dropdownVehicleTypeExpanded)
                            },
                        )
                        ExposedDropdownMenu(
                            modifier = Modifier
                                .heightIn(max = TextFieldDefaults.MinHeight.times(3))
                                .width(TextFieldDefaults.MinWidth)
                                .verticalScroll(rememberScrollState()),
                            expanded = dropdownVehicleTypeExpanded,
                            onDismissRequest = { dropdownVehicleTypeExpanded = false }
                        ) {
                            vehicleTypes.forEach { vehicleType ->
                                DropdownMenuItem(
                                    text = { Text(text = vehicleType.name) },
                                    onClick = {
                                        if (selectedVehicleType != vehicleType) {
                                            selectedCar = null
                                            selectedMotorcycle = null
                                            selectedVehicleName = ""
                                            selectedVehicleId = 0L
                                        }
                                        selectedVehicleType = vehicleType
                                        dropdownVehicleTypeExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    ExposedDropdownMenuBox(
                        expanded = dropdownVehicleExpanded,
                        onExpandedChange = {
                            dropdownVehicleExpanded = !dropdownVehicleExpanded
                            if (dropdownVehicleExpanded) isSelectedVehicleError = false
                        }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.menuAnchor(),
                            value = selectedVehicleName,
                            onValueChange = { },
                            readOnly = true,
                            placeholder = {
                                Text(text = "Select a vehicle")
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(dropdownVehicleExpanded)
                            },
                            supportingText = {
                                if (isSelectedVehicleError) {
                                    Text(
                                        text = "Please select a vehicle",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            isError = isSelectedVehicleError
                        )
                        ExposedDropdownMenu(
                            modifier = Modifier
                                .heightIn(max = TextFieldDefaults.MinHeight.times(3))
                                .width(TextFieldDefaults.MinWidth)
                                .verticalScroll(rememberScrollState()),
                            expanded = dropdownVehicleExpanded,
                            onDismissRequest = { dropdownVehicleExpanded = false }
                        ) {
                            when(selectedVehicleType) {
                                VehicleType.CAR -> {
                                    cars.forEach { car ->
                                        DropdownMenuItem(
                                            text = { Text(text = car.name) },
                                            onClick = {
                                                selectedVehicleId = car.id
                                                selectedVehicleName = car.name
                                                selectedCar = car
                                                dropdownVehicleExpanded = false
                                            }
                                        )
                                    }
                                }
                                VehicleType.MOTORCYCLE -> {
                                    motorcycles.forEach { motorcycle ->
                                        DropdownMenuItem(
                                            text = { Text(text = motorcycle.name) },
                                            onClick = {
                                                selectedVehicleId = motorcycle.id
                                                selectedVehicleName = motorcycle.name
                                                selectedMotorcycle = motorcycle
                                                dropdownVehicleExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                    AddTransactionVehicleDetail(
                        selectedVehicleType = selectedVehicleType,
                        car = selectedCar,
                        motorcycle = selectedMotorcycle
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (selectedVehicleId != 0L) {
                        onConfirm(selectedVehicleId, selectedVehicleType)
                    } else isSelectedVehicleError = true
                }) {
                    Text(text = "Create")
                }
            },
        )

    }
}

@Composable
fun AddTransactionVehicleDetail(
    selectedVehicleType: VehicleType,
    car: Car? = null,
    motorcycle: Motorcycle? = null,
) {
    when(selectedVehicleType) {
        VehicleType.CAR -> {
            if (car != null) {
                AddTransactionCarDetail(car)
            }
        }
        VehicleType.MOTORCYCLE -> {
            if (motorcycle != null) {
                AddTransactionMotorcycleDetail(motorcycle)
            }
        }
    }
}

@Composable
fun AddTransactionCarDetail(car: Car) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        AddTransactionVehicleDetailItem("Name", car.name)
        AddTransactionVehicleDetailItem("Stock", "${car.stock}")
        AddTransactionVehicleDetailItem(
            "Release Date",
            car.releaseDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
        )
        AddTransactionVehicleDetailItem("Color", car.color)
        AddTransactionVehicleDetailItem("Price", "${car.price}")
        AddTransactionVehicleDetailItem("Engine", car.engine)
        AddTransactionVehicleDetailItem("Capacity", "${car.capacity}")
        AddTransactionVehicleDetailItem("Type", car.type)
    }
}

@Composable
fun AddTransactionMotorcycleDetail(motorcycle: Motorcycle) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        AddTransactionVehicleDetailItem("Name", motorcycle.name)
        AddTransactionVehicleDetailItem("Stock", "${motorcycle.stock}")
        AddTransactionVehicleDetailItem(
            "Release Date",
            motorcycle.releaseDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
        )
        AddTransactionVehicleDetailItem("Color", motorcycle.color)
        AddTransactionVehicleDetailItem("Price", "${motorcycle.price}")
        AddTransactionVehicleDetailItem("Engine", motorcycle.engine)
        AddTransactionVehicleDetailItem("Suspension", motorcycle.suspension)
        AddTransactionVehicleDetailItem("Transmission", motorcycle.transmission)
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
