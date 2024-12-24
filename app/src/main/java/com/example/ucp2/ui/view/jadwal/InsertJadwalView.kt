package com.example.ucp2.ui.view.jadwal

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
import com.example.pamucp2.data.entity.Dokter
import com.example.pamucp2.ui.costumwidget.TopAppBar
import com.example.pamucp2.ui.viewmodel.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun InsertJdlViewPreview() {
    InsertJdlView(onBack = {}, onNavigate = {})
}

@Composable
fun InsertJdlView(
    onBack: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: JadwalViewModel = viewModel(factory = PenyediaViewModel.Factory)
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
                judul = "Tambah Jadwal"
            )
            InsertBodyJdl(
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
fun InsertBodyJdlPreview() {
    InsertBodyJdl(
        uiState = JdlUIState(),
        onValueChange = {},
        onClick = {}
    )
}

@Composable
fun InsertBodyJdl(
    modifier: Modifier = Modifier,
    onValueChange: (JadwalEvent) -> Unit,
    uiState: JdlUIState,
    onClick: () -> Unit,
    dokList: List<Dokter> = uiState.dokterList
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
                    text = "Form Tambah Jadwal",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                InputField(
                    label = "Nama Pasien",
                    value = uiState.jadwalEvent.namaPasien,
                    error = uiState.isEntryValid.namaPasien,
                    onValueChange = { onValueChange(uiState.jadwalEvent.copy(namaPasien = it)) }
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Selector(
                    selected = uiState.jadwalEvent.namaDokter,
                    onSelect = { selectedDokter ->
                        onValueChange(uiState.jadwalEvent.copy(namaDokter = selectedDokter))
                    },
                    dokterList = dokList
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                InputField(
                    label = "No Hp",
                    value = uiState.jadwalEvent.noHp,
                    error = uiState.isEntryValid.noHp,
                    onValueChange = { onValueChange(uiState.jadwalEvent.copy(noHp = it)) }
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                InputField(
                    label = "Tanggal Konsul",
                    value = uiState.jadwalEvent.tanggalKonsul,
                    error = uiState.isEntryValid.tanggalKonsul,
                    onValueChange = { onValueChange(uiState.jadwalEvent.copy(tanggalKonsul = it)) }
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                InputField(
                    label = "Status",
                    value = uiState.jadwalEvent.status,
                    error = uiState.isEntryValid.status,
                    onValueChange = { onValueChange(uiState.jadwalEvent.copy(status = it)) }
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
    selected: String?,
    onSelect: (String) -> Unit,
    dokterList: List<Dokter>
) {
    val options = dokterList.map { it.nama }
    val expanded = rememberSaveable { mutableStateOf(false) }
    val currentSelection = remember { mutableStateOf(selected ?: options.getOrNull(0)) }

    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value }
    ) {
        OutlinedTextField(
            value = currentSelection.value ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Nama Dokter") },
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