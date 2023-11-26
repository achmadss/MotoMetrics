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
import androidx.compose.material.icons.filled.CalendarToday
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.achmadss.data.entities.Car
import com.achmadss.data.entities.Motorcycle
import com.achmadss.data.entities.base.Vehicle
import com.achmadss.data.entities.base.VehicleType
import com.achmadss.motometrics.Routes
import com.achmadss.motometrics.ui.components.topbar.DefaultDetailTopBar
import com.achmadss.motometrics.utils.formatPattern
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun NavGraphBuilder.routeAddVehicle(
    navController: NavController,
) {
    composable(
        route = Routes.VEHICLE
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

data class VehicleDetails(
    val name: String = "",
    val stock: Int = 0,
    val color: String = "",
    val releaseDate: LocalDateTime = LocalDateTime.now(),
    val price: Int = 0,
    val engine: String = "",
    val capacity: Int = 0,
    val type: String = "",
    val suspension: String = "",
    val transmission: String = "",
    val vehicleType: VehicleType = VehicleType.CAR
) {
    fun toCar(): Car {
        return Car(
            name = name,
            stock = stock,
            color = color,
            price = price,
            engine = engine,
            capacity = capacity,
            type = type,
            vehicleType = vehicleType,
            releaseDate = releaseDate
        )
    }

    fun toMotorcycle(): Motorcycle {
        return Motorcycle(
            name = name,
            stock = stock,
            color = color,
            price = price,
            engine = engine,
            suspension = suspension,
            transmission = transmission,
            vehicleType = VehicleType.MOTORCYCLE,
            releaseDate = releaseDate
        )
    }
}

fun validateVehicleDetails(
    details: VehicleDetails,
    vehicleType: VehicleType,
    isNameError: MutableState<Boolean>,
    isColorError: MutableState<Boolean>,
    isEngineError: MutableState<Boolean>,
    isTypeError: MutableState<Boolean>,
    isSuspensionError: MutableState<Boolean>,
    isTransmissionError: MutableState<Boolean>
): Boolean {
    var isValid = true

    isNameError.value = details.name.isBlank().also { if (it) isValid = false }
    isColorError.value = details.color.isBlank().also { if (it) isValid = false }
    isEngineError.value = details.engine.isBlank().also { if (it) isValid = false }

    when (vehicleType) {
        VehicleType.CAR -> {
            isTypeError.value = details.type.isBlank().also { if (it) isValid = false }
        }
        VehicleType.MOTORCYCLE -> {
            isSuspensionError.value = details.suspension.isBlank().also { if (it) isValid = false }
            isTransmissionError.value = details.transmission.isBlank().also { if (it) isValid = false }
        }
    }

    return isValid
}

fun createVehicleAndFinish(
    vehicleType: VehicleType,
    vehicleDetails: VehicleDetails,
    isNameError: MutableState<Boolean>,
    isColorError: MutableState<Boolean>,
    isEngineError: MutableState<Boolean>,
    isTypeError: MutableState<Boolean>,
    isSuspensionError: MutableState<Boolean>,
    isTransmissionError: MutableState<Boolean>,
    onDone: (Vehicle) -> Unit
) {
    if (validateVehicleDetails(
            vehicleDetails, vehicleType, isNameError,
            isColorError, isEngineError, isTypeError,
            isSuspensionError, isTransmissionError)
    ) {
        val vehicle = when (vehicleType) {
            VehicleType.CAR -> vehicleDetails.toCar()
            VehicleType.MOTORCYCLE -> vehicleDetails.toMotorcycle()
        }
        onDone(vehicle)
    }
}

@Composable
fun AddVehicleScreen(
    onBackPress: () -> Unit,
    onDone: (Vehicle) -> Unit,
) {
    var selectedVehicleType by remember { mutableStateOf(VehicleType.CAR) }
    var vehicleDetails by remember { mutableStateOf(VehicleDetails()) }
    val isNameError = remember { mutableStateOf(false) }
    val isColorError = remember { mutableStateOf(false) }
    val isEngineError = remember { mutableStateOf(false) }
    val isTypeError = remember { mutableStateOf(false) }
    val isSuspensionError = remember { mutableStateOf(false) }
    val isTransmissionError = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DefaultDetailTopBar("Add Vehicle", onBackPress) {
                IconButton(onClick = {
                    createVehicleAndFinish(
                        selectedVehicleType,
                        vehicleDetails,
                        isNameError,
                        isColorError,
                        isEngineError,
                        isTypeError,
                        isSuspensionError,
                        isTransmissionError,
                        onDone
                    )
                }) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = "Done")
                }
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VehicleTypeDropdown(selectedVehicleType = selectedVehicleType) { newType ->
                selectedVehicleType = newType
                vehicleDetails = VehicleDetails(vehicleType = newType)
            }
            VehicleDetailsForm(
                vehicleDetails,
                isNameError.value,
                isColorError.value,
                isEngineError.value,
                isTypeError.value,
                isSuspensionError.value,
                isTransmissionError.value
            ) { newDetails ->
                vehicleDetails = newDetails
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDetailsForm(
    vehicleDetails: VehicleDetails,
    isNameError: Boolean,
    isColorError: Boolean,
    isEngineError: Boolean,
    isTypeError: Boolean,
    isSuspensionError: Boolean,
    isTransmissionError: Boolean,
    onDetailsChanged: (VehicleDetails) -> Unit,
) {
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = vehicleDetails.releaseDate
            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    )

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = vehicleDetails.name,
        onValueChange = {
            onDetailsChanged(vehicleDetails.copy(name = it))
        },
        label = { Text("Name") },
        isError = isNameError,
        supportingText = {
            if (isNameError) {
                Text("Name cannot be empty")
            }
        }
    )
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = vehicleDetails.stock.toString(),
        onValueChange = {
            onDetailsChanged(vehicleDetails.copy(stock = it.toIntOrNull() ?: 0))
        },
        placeholder = { Text(text = "0") },
        label = { Text("Stock") },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
    )
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = vehicleDetails.color,
        onValueChange = {
            onDetailsChanged(vehicleDetails.copy(color = it))
        },
        label = { Text("Color") },
        isError = isColorError,
        supportingText = {
            if (isColorError) {
                Text("Color cannot be empty")
            }
        }
    )
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = vehicleDetails.price.toString(),
        onValueChange = {
            onDetailsChanged(vehicleDetails.copy(price = it.toIntOrNull() ?: 0))
        },
        label = { Text("Price") },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
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
        trailingIcon = {
            Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null)
        },
        value = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(datePickerState.selectedDateMillis!!),
            ZoneId.systemDefault()
        ).formatPattern("dd MMMM yyyy"),
        onValueChange = { },
    )
    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePickerDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDate = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(datePickerState.selectedDateMillis!!),
                            ZoneId.systemDefault()
                        )
                        onDetailsChanged(vehicleDetails.copy(releaseDate = selectedDate))
                        showDatePickerDialog = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerDialog = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = vehicleDetails.engine,
        onValueChange = {
            onDetailsChanged(vehicleDetails.copy(engine = it))
        },
        label = { Text("Engine") },
        isError = isEngineError,
        supportingText = {
            if (isEngineError) {
                Text("Engine cannot be empty")
            }
        }
    )
    when (vehicleDetails.vehicleType) {
        VehicleType.CAR -> {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = vehicleDetails.capacity.toString(),
                onValueChange = {
                    onDetailsChanged(vehicleDetails.copy(capacity = it.toIntOrNull() ?: 0))
                },
                label = { Text("Capacity") },
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = vehicleDetails.type,
                onValueChange = {
                    onDetailsChanged(vehicleDetails.copy(type = it))
                },
                label = { Text("Type") },
                isError = isTypeError,
                supportingText = {
                    if (isTypeError) {
                        Text("Type cannot be empty")
                    }
                }
            )
        }
        VehicleType.MOTORCYCLE -> {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = vehicleDetails.suspension,
                onValueChange = {
                    onDetailsChanged(vehicleDetails.copy(suspension = it))
                },
                label = { Text("Suspension") },
                isError = isSuspensionError,
                supportingText = {
                    if (isSuspensionError) {
                        Text("Suspension cannot be empty")
                    }
                }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = vehicleDetails.transmission,
                onValueChange = {
                    onDetailsChanged(vehicleDetails.copy(transmission = it))
                },
                label = { Text("Transmission") },
                isError = isTransmissionError,
                supportingText = {
                    if (isTransmissionError) {
                        Text("Transmission cannot be empty")
                    }
                }
            )
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
            modifier = Modifier
                .fillMaxWidth(),
            expanded = dropdownExpanded,
            onDismissRequest = { dropdownExpanded = false }
        ) {
            vehicleTypes.forEach { vehicleType ->
                DropdownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
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