package com.achmadss.motometrics.ui.components.topbar

import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onSearch: (query: String) -> Unit,
    actions: @Composable () -> Unit,
) {

    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(isSearching) {
        if (isSearching) focusRequester.requestFocus()
    }

    BackHandler(enabled = isSearching) {
        if (isSearching) isSearching = false
    }

    TopAppBar(
        modifier = modifier,
        title = {
            if (!isSearching) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )
                return@TopAppBar
            }
            OutlinedTextField(
                modifier = Modifier.focusRequester(focusRequester),
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    onSearch(searchQuery)
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                placeholder = {
                    Text(
                        text = "Search...",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            searchQuery = ""
                            onSearch(searchQuery)
                        }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "")
                        }
                    }
                },
                singleLine = true
            )
        },
        navigationIcon = {
            if (isSearching) {
                IconButton(onClick = { isSearching = false }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                }
            }
        },
        actions = {
            if (!isSearching) {
                IconButton(onClick = { isSearching = true }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "")
                }
            }
            actions.invoke()
        }
    )
}