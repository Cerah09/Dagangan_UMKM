package com.josua0056.daganganumkm.ui.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.semantics.Role
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
import kotlin.math.pow

// --- NAVIGASI ---
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

            BmiSection()
        }
    }
}

@Composable
fun BmiSection() {
    val context = LocalContext.current
    var berat by remember { mutableStateOf("") }
    var beratError by remember { mutableStateOf(false) }
    var tinggi by remember { mutableStateOf("") }
    var tinggiError by remember { mutableStateOf(false) }

    val radioOptions = listOf(stringResource(id = R.string.pria), stringResource(id = R.string.wanita))
    var gender by remember { mutableStateOf(radioOptions[0]) }
    var bmi by remember { mutableFloatStateOf(0f) }
    var kategori by remember { mutableIntStateOf(0) }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.bmi_intro), style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(
            value = berat,
            onValueChange = { berat = it },
            label = { Text(text = stringResource(R.string.berat_badan)) },
            trailingIcon = { IconPicker(beratError, "kg") },
            supportingText = { ErrorHint(beratError) },
            isError = beratError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = tinggi,
            onValueChange = { tinggi = it },
            label = { Text(text = stringResource(R.string.Tinggi_badan)) },
            trailingIcon = { IconPicker(tinggiError, "cm") },
            supportingText = { ErrorHint(tinggiError) },
            isError = tinggiError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.padding(top = 6.dp).border(1.dp, Color.Gray, RoundedCornerShape(4.dp))) {
            radioOptions.forEach { text ->
                GenderOption(label = text, isSelected = gender == text, modifier = Modifier.selectable(selected = gender == text, onClick = { gender = text }, role = Role.RadioButton).weight(1f).padding(8.dp))
            }
        }

        Button(
            onClick = {
                beratError = (berat == "" || berat == "0"); tinggiError = (tinggi == "" || tinggi == "0")
                if (beratError || tinggiError) return@Button
                bmi = hitungBmi(berat.toFloat(), tinggi.toFloat())
                kategori = getKategori(bmi, gender == radioOptions[0])
            },
            modifier = Modifier.padding(top = 8.dp),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp)
        ) {
            Text(text = stringResource(R.string.Hitung))
        }

        if (bmi != 0f) {
            val kategoriText = stringResource(kategori)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp)
            Text(text = "BMI: ${"%.2f".format(bmi)}", style = MaterialTheme.typography.titleLarge)
            Text(text = kategoriText.uppercase(), style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

            // TOMBOL BAGIKAN SESUAI REQUEST
            Button(
                onClick = { shareBmi(context, bmi, kategoriText) },
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
                painter = painterResource(id = R.drawable.nasikuning), // Ganti dengan foto profilmu
                contentDescription = null,
                modifier = Modifier.size(120.dp).clip(CircleShape).border(2.dp, Color.Gray, CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Josua", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("josua@email.com", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(24.dp))
            Text("Aplikasi Dagangan UMKM membantu Anda melihat menu lezat sekaligus memantau kesehatan dengan fitur BMI.", textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { navController.popBackStack() }) { Text("Kembali") }
        }
    }
}

// --- FUNGSI BAGIKAN ---
private fun shareBmi(context: Context, bmi: Float, kategori: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "Hasil BMI saya: ${"%.2f".format(bmi)} ($kategori). Cek kesehatanmu di Dagangan UMKM!")
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(sendIntent, "Bagikan"))
}

// --- Komponen FoodSection & Helper tetap sama ---
@Composable
fun FoodSection(gerobak: Gerobak, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = gerobak.imageResId), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.size(132.dp))
        Text(text = gerobak.nama, style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(top = 16.dp))
        Button(onClick = onClick, modifier = Modifier.fillMaxWidth(0.5f).padding(top = 16.dp)) { Text(text = stringResource(R.string.lanjut)) }
    }
}

@Composable
fun IconPicker(isError: Boolean, unit: String) { if (isError) Icon(Icons.Filled.Warning, null) else Text(unit) }

@Composable
fun ErrorHint(isError: Boolean) { if (isError) Text(stringResource(R.string.input_invalid)) }

@Composable
fun GenderOption(label: String, isSelected: Boolean, modifier: Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = isSelected, onClick = null)
        Text(text = label, modifier = Modifier.padding(start = 8.dp))
    }
}

private fun hitungBmi(berat: Float, tinggi: Float): Float = berat / (tinggi / 100).pow(2)

private fun getKategori(bmi: Float, isMale: Boolean): Int {
    return if (isMale) {
        if (bmi < 20.5) R.string.kurus else if (bmi >= 27.0) R.string.gemuk else R.string.ideal
    } else {
        if (bmi < 18.5) R.string.kurus else if (bmi >= 25.0) R.string.gemuk else R.string.ideal
    }
}