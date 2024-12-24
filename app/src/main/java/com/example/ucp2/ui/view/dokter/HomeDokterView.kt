package com.example.ucp2.ui.view.dokter

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeDokterView(
    viewModel: HomeDokterViewModel = viewModel(factory = PenyediaViewModel.Factory),
    onAddDok: () -> Unit = { },
    onSeeJdl: () -> Unit = { },
    onSeeDok: () -> Unit = { },
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            Column {
                Spacer(modifier = Modifier.height(21.dp))
                Header(
                    namaPerusahaan = "Amanah Sehat",
                    profilResId = R.drawable.sehatkuy
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onSeeDok,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray
                        ),
                        enabled = false
                    ) {
                        Text(
                            text = "Daftar Dokter",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }


                    Button(
                        onClick = onSeeJdl,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Daftar Jadwal",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TopAppBar(
                    judul = "Daftar Dokter",
                    showBackButton = false,
                    onBack = { }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddDok,
                shape = MaterialTheme.shapes.medium,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Dokter",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        val homeUiState by viewModel.homeUiState.collectAsState()

        BodyHomeDokView(
            homeUiStateDok = homeUiState,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun Header(
    namaPerusahaan: String,
    profilResId: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF6200EE),
                        Color(0xFF3700B3)
                    )
                )
            )
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color.White.copy(alpha = 0.3f))
            ) {
                Image(
                    painter = painterResource(profilResId),
                    contentDescription = "Profil Perusahaan",
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(35.dp))
                        .align(Alignment.Center),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = namaPerusahaan,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Ikon Perusahaan",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Layanan Kesehatan Terpercaya",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun BodyHomeDokterView(
    homeUiStateDok: HomeUiStateDokter,
    onClick: (Int) -> Unit = { },
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                )
            )
    ) {
        when {
            homeUiStateDok.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 6.dp
                )
            }

            homeUiStateDok.isError -> {
                Text(
                    text = "Terjadi Kesalahan!",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            homeUiStateDok.listDok.isEmpty() -> {
                Text(
                    text = "Tidak ada data dokter.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp)
                ) {
                    items(homeUiStateDok.listDok) { dok ->
                        CardDok(dok = dok, onClick = { onClick(dok.idDok) })
                    }
                }
            }
        }
    }
}

fun Color(spesialis: String): Color {
    return when (spesialis) {
        "Bedah Umum" -> Color.Green
        "Bedah Saraf" -> Color.Blue
        "Bedah Ortopedi" -> Color.Red
        "Bedah Plastik" -> Color.Magenta
        else -> Color.Black
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDok(
    dok: Dokter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Icon Dokter",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(50.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = dok.nama,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    text = "Spesialis: ${dok.spesialis}",
                    fontSize = 16.sp,
                    color = Color(dok.spesialis),
                    fontWeight = FontWeight.Medium
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = " : ${dok.klinik}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = " : ${dok.jamKerja}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}