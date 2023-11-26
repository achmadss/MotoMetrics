package com.achmadss.motometrics.ui.screens.add_vehicle

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.achmadss.data.entities.Car
import com.achmadss.data.entities.Motorcycle
import com.achmadss.data.entities.base.Vehicle
import com.achmadss.data.entities.base.VehicleType
import com.achmadss.motometrics.Routes
import com.achmadss.motometrics.ui.components.dialog.DefaultDetailTopBar
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun NavGraphBuilder.routeAddVehicle(
    navController: NavController,
) {
    composable(
        route = Routes.ADD_VEHICLE
    ) {
        val viewModel = viewModel<AddVehicleViewModel>()
        val context = LocalContext.current
        AddVehicleScreen(
            onBackPress = { navController.popBackStack() },
            onDone = {
                viewModel.addNewVehicle(it, context) {
                    navController.popBackStack()
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVehicleScreen(
    onBackPress: () -> Unit,
    onDone: (Vehicle) -> Unit,
) {
    var selectedVehicleType by remember { mutableStateOf(VehicleType.CAR) }

    var name by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var engine by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var suspension by remember { mutableStateOf("") }
    var transmission by remember { mutableStateOf("") }

    var isNameError by remember { mutableStateOf(false) }
    var isStockError by remember { mutableStateOf(false) }
    var isColorError by remember { mutableStateOf(false) }
    var isPriceError by remember { mutableStateOf(false) }
    var isEngineError by remember { mutableStateOf(false) }
    var isCapacityError by remember { mutableStateOf(false) }
    var isTypeError by remember { mutableStateOf(false) }
    var isSuspensionError by remember { mutableStateOf(false) }
    var isTransmissionError by remember { mutableStateOf(false) }

    var showDatePickerDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli()
    )

    if (showDatePickerDialog) {
        val confirmEnabled = remember {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }
        DatePickerDialog(
            onDismissRequest = {
                showDatePickerDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePickerDialog = false
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePickerDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    fun validate(): Boolean {
        var isValid = true

        isNameError = name.isBlank().also { if (it) isValid = false }
        isStockError = stock.isNotBlank().and(stock.isDigitsOnly().not()).also { if (it) isValid = false }
        isEngineError = engine.isBlank().also { if (it) isValid = false }
        isColorError = color.isBlank().also { if (it) isValid = false }
        isPriceError = price.isNotBlank().and(price.isDigitsOnly().not()).also { if (it) isValid = false }

        when (selectedVehicleType) {
            VehicleType.CAR -> {
                isTypeError = type.isBlank().also { if (it) isValid = false }
                isCapacityError = capacity.isNotBlank().and(capacity.isDigitsOnly().not()).also { if (it) isValid = false }
            }
            VehicleType.MOTORCYCLE -> {
                isSuspensionError = suspension.isBlank().also { if (it) isValid = false }
                isTransmissionError = transmission.isBlank().also { if (it) isValid = false }
            }
        }

        return isValid
    }


    fun createCar(): Car {
        return Car(
            name = name,
            stock = if (stock.isBlank()) 0 else stock.toInt(),
            releaseDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(datePickerState.selectedDateMillis!!),
                ZoneId.systemDefault()
            ),
            color = color,
            price = if (price.isBlank()) 0 else price.toInt(),
            engine = engine,
            capacity = if (capacity.isBlank()) 0 else capacity.toInt(),
            type = type,
        )
    }

    fun createMotorcycle(): Motorcycle {
        return Motorcycle(
            name = name,
            stock = if (stock.isBlank()) 0 else stock.toInt(),
            releaseDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(datePickerState.selectedDateMillis!!),
                ZoneId.systemDefault()
            ),
            color = color,
            price = if (price.isBlank()) 0 else price.toInt(),
            engine = engine,
            suspension = suspension,
            transmission = transmission
        )
    }

    fun createVehicleAndFinish() {
        if (validate()) {
            val vehicle = when(selectedVehicleType) {
                VehicleType.CAR -> createCar()
                VehicleType.MOTORCYCLE -> createMotorcycle()
            }
            onDone(vehicle)
        }
    }
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DefaultDetailTopBar("Add Vehicle", onBackPress) {
                IconButton(onClick = ::createVehicleAndFinish) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = "Done")
                }
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(
                    top = it.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VehicleTypeDropdown(selectedVehicleType = selectedVehicleType) {
                selectedVehicleType = it
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Name") },
                value = name,
                onValueChange = { name = it },
                supportingText = {
                    if (isNameError) {
                        Text(text = "Name cannot be empty!")
                    }
                },
                isError = isNameError
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Stock") },
                value = stock,
                onValueChange = {
                    stock = it
                },
                placeholder = {
                    Text(text = "0")
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                supportingText = {
                    if (isStockError) {
                        Text(text = "Must only contain digits")
                    }
                },
                isError = isStockError
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePickerDialog = true },
                label = { Text(text = "Release Date") },
                readOnly = true,
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                value = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(datePickerState.selectedDateMillis!!),
                    ZoneId.systemDefault()
                ).format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                onValueChange = { },
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Color") },
                value = color,
                onValueChange = { color = it },
                supportingText = {
                    if (isColorError) {
                        Text(text = "Color cannot be empty!")
                    }
                },
                isError = isColorError
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Price") },
                value = price,
                onValueChange = {
                    price = it
                },
                placeholder = { Text(text = "0") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                supportingText = {
                    if (isPriceError) {
                        Text(text = "Must only contain digits")
                    }
                },
                isError = isPriceError
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Engine") },
                value = engine,
                onValueChange = { engine = it },
                supportingText = {
                    if (isEngineError) {
                        Text(text = "Engine cannot be empty!")
                    }
                },
                isError = isEngineError
            )
            if (selectedVehicleType == VehicleType.CAR) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Capacity") },
                    value = capacity,
                    onValueChange = {
                        capacity = it
                    },
                    placeholder = { Text(text = "0") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    supportingText = {
                        if (isCapacityError) {
                            Text(text = "Must only contain digits")
                        }
                    },
                    isError = isCapacityError
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Type") },
                    value = type,
                    onValueChange = { type = it },
                    supportingText = {
                        if (isTypeError) {
                            Text(text = "Type cannot be empty!")
                        }
                    },
                    isError = isTypeError
                )
            }
            if (selectedVehicleType == VehicleType.MOTORCYCLE) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Suspension") },
                    value = suspension,
                    onValueChange = { suspension = it },
                    supportingText = {
                        if (isSuspensionError) {
                            Text(text = "Suspension cannot be empty!")
                        }
                    },
                    isError = isSuspensionError
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Transmission") },
                    value = transmission,
                    onValueChange = { transmission = it },
                    supportingText = {
                        if (isTransmissionError) {
                            Text(text = "Transmission cannot be empty!")
                        }
                    },
                    isError = isTransmissionError
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleTypeDropdown(
    selectedVehicleType: VehicleType,
    onVehicleTypeSelected: (VehicleType) -> Unit,
) {
    val vehicleTypes = listOf(VehicleType.CAR, VehicleType.MOTORCYCLE)
    var dropdownExpanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = Modifier
            .fillMaxWidth(),
        expanded = dropdownExpanded,
        onExpandedChange = { dropdownExpanded = !dropdownExpanded }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            label = {
                Text(text = "Vehicle Type")
            },
            value = selectedVehicleType.name,
            onValueChange = { },
            readOnly = true,
            placeholder = {
                Text(text = "Select vehicle type")
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(dropdownExpanded)
            }
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