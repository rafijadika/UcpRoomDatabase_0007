package com.example.ucp2.ui.view.jadwal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ucp2.data.entity.Jadwal

@Composable
fun DetailJadwalView(
    modifier: Modifier = Modifier,
    viewModel: DetailJadwalViewModel = viewModel(factory = PenyediaViewModel.Factory),
    onBack: () -> Unit = {},
    onEditClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit = {}
){
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                judul = "Detail Jadwal",
                showBackButton = true,
                onBack = onBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEditClick(viewModel.detailUiState.value.detailUiEvent.idJdl.toString()) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Jadwal"
                )
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(top = 16.dp) // Adds extra space at the top
            ) {
                val detailUiState by viewModel.detailUiState.collectAsState()

                BodyDetailJdl(
                    modifier = Modifier.fillMaxSize(),
                    detailUiState = detailUiState,
                    onDeleteClick = {
                        viewModel.deteleJdl()
                        onDeleteClick()
                    }
                )
            }
        }
    )
}

@Composable
fun BodyDetailJadwal(
    modifier: Modifier = Modifier,
    detailUiState: DetailUiState = DetailUiState(),
    onDeleteClick: () -> Unit = {}
){
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    when{
        detailUiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        }

        detailUiState.isUiEventNotEmpty -> {
            Column (modifier = modifier.fillMaxWidth().padding(16.dp))
            {
                ItemDetailJdl(
                    jadwal = detailUiState.detailUiEvent.toJadwalEntity(),
                    modifier = Modifier
                )
                Spacer(modifier =  Modifier.padding(8.dp))
                Button(onClick = {
                    deleteConfirmationRequired = true
                }, modifier = Modifier.fillMaxWidth())
                {
                    Text(text = "Delete", fontSize = 18.sp)
                }

                if (deleteConfirmationRequired) {
                    DeleteConfirmationDialog(
                        onDeleteConfirm = {
                            deleteConfirmationRequired = false
                            onDeleteClick()
                        },
                        onDeleteCancel =  {
                            deleteConfirmationRequired = false
                        }, modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }

        detailUiState.isUiEventEmpty -> {
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "Data tidak ditemukan",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun ItemDetailJadwal(
    modifier: Modifier = Modifier,
    jadwal: Jadwal
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            ComponentDetailJdl(judul = "ID Jadwal", isinya = jadwal.idJadwal.toString())
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            ComponentDetailJdl(judul = "Nama Pasien", isinya = jadwal.namaPasien)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            ComponentDetailJdl(judul = "Nama Dokter", isinya = jadwal.namaDokter)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            ComponentDetailJdl(judul = "No Hp", isinya = jadwal.noHp)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            ComponentDetailJdl(judul = "Tanggal Konsul", isinya = jadwal.tanggalKonsul)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            ComponentDetailJdl(judul = "Status", isinya = jadwal.status)
        }
    }
}

@Composable
fun ComponentDetailJdl(
    modifier: Modifier = Modifier,
    judul: String,
    isinya: String
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = judul,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = isinya,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDeleteCancel,
        title = {
            Text(
                "Hapus Data",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.error
            )
        },
        text = {
            Text(
                "Apakah Anda yakin ingin menghapus data ini? Tindakan ini tidak dapat dibatalkan.",
                fontSize = 16.sp
            )
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text("Batal", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        confirmButton = {
            Button(
                onClick = onDeleteConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Hapus", color = Color.White)
            }
        }
    )
}