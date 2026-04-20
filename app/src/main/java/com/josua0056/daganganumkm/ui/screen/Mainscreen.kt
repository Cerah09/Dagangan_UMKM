package com.josua0056.daganganumkm.ui.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.josua0056.daganganumkm.R
import com.josua0056.daganganumkm.model.Gerobak

enum class Screen { Main, About }

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Main.name) {
        composable(Screen.Main.name) { MainScreen(navController) }
        composable(Screen.About.name) { AboutScreen(navController) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val data = listOf(
        Gerobak("Kupat Tahu", R.drawable.kupattahu),
        Gerobak("Lontong", R.drawable.lontong),
        Gerobak("Nasi Kuning", R.drawable.nasikuning),
    )
    var index by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.About.name) }) {
                        Icon(Icons.Default.Info, contentDescription = "Tentang Aplikasi")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FoodSection(gerobak = data[index]) {
                index = if (index == data.size - 1) 0 else index + 1
            }

            HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant)

            // FITUR BARU: CUACA
            WeatherSection()
        }
    }
}

@Composable
fun WeatherSection() {
    val context = LocalContext.current
    var suhu by remember { mutableStateOf("") }
    var kelembapan by remember { mutableStateOf("") }
    var angin by remember { mutableStateOf("") }

    var errorState by remember { mutableStateOf(false) }

    var indeksCuaca by remember { mutableFloatStateOf(0f) }
    var kategoriCuaca by remember { mutableStateOf("") }
    var warnaKategori by remember { mutableStateOf(Color.Gray) }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Cek Kondisi Cuaca Lapak", style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(
            value = suhu,
            onValueChange = { suhu = it },
            label = { Text("Suhu Udara (°C)") },
            trailingIcon = { if (errorState && suhu.isEmpty()) Icon(Icons.Filled.Warning, null) },
            isError = errorState && suhu.isEmpty(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = kelembapan,
            onValueChange = { kelembapan = it },
            label = { Text("Kelembapan (%)") },
            trailingIcon = { if (errorState && kelembapan.isEmpty()) Icon(Icons.Filled.Warning, null) },
            isError = errorState && kelembapan.isEmpty(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = angin,
            onValueChange = { angin = it },
            label = { Text("Kecepatan Angin (km/jam)") },
            trailingIcon = { if (errorState && angin.isEmpty()) Icon(Icons.Filled.Warning, null) },
            isError = errorState && angin.isEmpty(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (suhu.isEmpty() || kelembapan.isEmpty() || angin.isEmpty()) {
                    errorState = true
                    return@Button
                }
                errorState = false

                val s = suhu.toFloat()
                val k = kelembapan.toFloat()
                val a = angin.toFloat()


                val indeks = s + (0.1f * k) - (0.05f * a)
                indeksCuaca = indeks

                if (indeks < 27) {
                    kategoriCuaca = "NYAMAN"
                    warnaKategori = Color(0xFF2E7D32)
                } else if (indeks in 27.0..32.0) {
                    kategoriCuaca = "GERAH"
                    warnaKategori = Color(0xFFF9A825)
                } else {
                    kategoriCuaca = "WASPADA PANAS"
                    warnaKategori = Color.Red
                }
            },
            modifier = Modifier.padding(top = 8.dp),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp)
        ) {
            Text(text = "Cek Cuaca")
        }

        if (kategoriCuaca.isNotEmpty()) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp)
            Text(text = "Indeks Cuaca: ${"%.1f".format(indeksCuaca)}", style = MaterialTheme.typography.titleLarge)
            Text(
                text = kategoriCuaca,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = warnaKategori
            )

            Button(
                onClick = { shareWeather(context, kategoriCuaca, suhu) },
                modifier = Modifier.padding(top = 16.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp)
            ) {
                Text(text = "Bagikan")
            }
        }
    }
}

@Composable
fun AboutScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(title = { Text("Tentang Aplikasi") })
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.nasikuning),
                contentDescription = null,
                modifier = Modifier.size(120.dp).clip(CircleShape).border(2.dp, Color.Gray, CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Josua Natanael Panjaitan", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text("607062330056", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(24.dp))
            Text("Aplikasi Dagangan UMKM membantu Anda melihat menu lezat sekaligus memantau kondisi cuaca untuk berjualan.", textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { navController.popBackStack() }) { Text("Kembali") }
        }
    }
}

private fun shareWeather(context: Context, status: String, suhu: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "Info Cuaca Lapak UMKM: Suhu $suhu°C, Status: $status. Yuk mampir belanja!")
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(sendIntent, "Bagikan"))
}

@Composable
fun FoodSection(gerobak: Gerobak, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = gerobak.imageResId), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.size(132.dp))
        Text(text = gerobak.nama, style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(top = 16.dp))
        Button(onClick = onClick, modifier = Modifier.fillMaxWidth(0.5f).padding(top = 16.dp)) { Text(text = "Lanjut") }
    }
}