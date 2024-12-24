package com.example.ucp2.ui.view.dokter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ucp2.ui.viewmodel.PenyediaViewModel
import com.example.ucp2.ui.viewmodel.dokter.DokterViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun InsertDokterViewPreview() {
    InsertDokterView(onBack = {}, onNavigate = {})
}

@Composable
fun InsertDokterView(
    onBack: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DokterViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiState = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.snackBarMessage) {
        uiState.snackBarMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.resetSnackBarMessage()
            }
        }
    }

    Scaffold(
        modifier = Modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            TopAppBar(
                onBack = onBack,
                showBackButton = true,
                judul = "Tambah Dokter"
            )
            InsertBodyDok(
                uiState = uiState,
                onValueChange = { updateEvent ->
                    viewModel.updateState(updateEvent)
                },
                onClick = {
                    coroutineScope.launch {
                        viewModel.saveData()
                    }
                    onNavigate()
                }
            )
        }
    }
}

@Composable
@Preview
fun InsertBodyDokPreview() {
    InsertBodyDok(
        uiState = DokUIState(),
        onValueChange = {},
        onClick = {}
    )
}

@Composable
fun InsertBodyDok(
    modifier: Modifier = Modifier,
    onValueChange: (DokterEvent) -> Unit,
    uiState: DokterUIState,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Form Tambah Dokter",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                InputField(
                    label = "Nama",
                    value = uiState.dokterEvent.nama,
                    error = uiState.isEntryValid.nama,
                    onValueChange = { onValueChange(uiState.dokterEvent.copy(nama = it)) }
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Selector(
                    selectedSpesialis = uiState.dokterEvent.spesialis,
                    onSelect = { onValueChange(uiState.dokterEvent.copy(spesialis = it)) }
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                InputField(
                    label = "Klinik",
                    value = uiState.dokterEvent.klinik,
                    error = uiState.isEntryValid.klinik,
                    onValueChange = { onValueChange(uiState.dokterEvent.copy(klinik = it)) }
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                InputField(
                    label = "No Hp",
                    value = uiState.dokterEvent.noHp,
                    error = uiState.isEntryValid.noHp,
                    onValueChange = { onValueChange(uiState.dokterEvent.copy(noHp = it)) }
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                InputField(
                    label = "Jam Kerja",
                    value = uiState.dokterEvent.jamKerja,
                    error = uiState.isEntryValid.jamKerja,
                    onValueChange = { onValueChange(uiState.dokterEvent.copy(jamKerja = it)) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Simpan", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    error: String?,
    onValueChange: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = error != null,
            singleLine = true
        )
        if (error != null) {
            Text(
                text = error,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Selector(
    selectedSpesialis: String?,
    onSelect: (String) -> Unit
) {
    val options = listOf("Bedah Umum", "Bedah Saraf", "Bedah Ortopedi", "Bedah Plastik")
    val expanded = rememberSaveable { mutableStateOf(false) }
    val currentSelection = remember { mutableStateOf(selectedSpesialis ?: options[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value }
    ) {
        OutlinedTextField(
            value = currentSelection.value,
            onValueChange = {},
            readOnly = true,
            label = { Text("Spesialis") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(8.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        currentSelection.value = selectionOption
                        expanded.value = false
                        onSelect(selectionOption)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}