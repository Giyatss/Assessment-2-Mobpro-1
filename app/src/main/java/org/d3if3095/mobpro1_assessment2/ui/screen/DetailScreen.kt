package org.d3if3095.mobpro1_assessment2.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3095.mobpro1_assessment2.R
import org.d3if3095.mobpro1_assessment2.database.MobilDb
import org.d3if3095.mobpro1_assessment2.ui.theme.Mobpro1_assessment2Theme
import org.d3if3095.mobpro1_assessment2.util.ViewModelFactory

const val KEY_ID_MOBIL = "idMobil"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {
    val context = LocalContext.current
    val db = MobilDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var merek by remember { mutableStateOf("") }
    var tipe by remember { mutableStateOf("") }
    var spesifikasi by remember { mutableStateOf("") }
    val selectedOptionsIndex = remember { mutableIntStateOf(-1) }

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getMobil(id) ?: return@LaunchedEffect
        if ( data != null) {
            merek = data.merek
            tipe = data.tipe
            spesifikasi = data.spesifikasi
            selectedOptionsIndex.intValue = getSelectedOptionIndex(spesifikasi)
        }
    }

    Scaffold (
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    if (id == null)
                        Text(text = stringResource(id = R.string.tambah_mobil))
                    else
                        Text(text = stringResource(id = R.string.edit_mobil))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        if (merek == "" || tipe == "" || spesifikasi == "") {
                            Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                            return@IconButton
                        }
                        if (id == null) {
                            viewModel.insert(merek, tipe, spesifikasi)
                        } else {
                            viewModel.update(id, merek, tipe, spesifikasi)
                        }
                        navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(R.string.simpan),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (id != null) {
                        DeleteAction { showDialog = true}
                        DisplayAlertDialog(
                            openDialog = showDialog,
                            onDismissRequest = {showDialog = false }) {
                            showDialog = false
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    ){ padding ->
        FormMobil(
            merek = merek,
            onMerekChange = {merek = it},
            tipe = tipe,
            onTipeChange = {tipe = it},
            spesifikasi = spesifikasi,
            onSpesifikasiChange = { spesifikasi = it },
            modifier = Modifier.padding(padding)
        )
    }
}

private fun getSelectedOptionIndex(spesifikasi: String): Int {
    return when (spesifikasi) {
        "Manual" -> 0
        "Matic" -> 1
        else -> -1
    }
}

@Composable
fun FormMobil(
    merek: String, onMerekChange: (String) -> Unit,
    tipe: String, onTipeChange: (String) -> Unit,
    modifier: Modifier,
    spesifikasi: String,
    onSpesifikasiChange: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = merek,
            onValueChange = { onMerekChange(it) },
            label = { Text(text = stringResource(id = R.string.merek)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = tipe,
            onValueChange = { onTipeChange(it) },
            label = { Text(text = stringResource(id = R.string.tipe)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp)),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            val options = listOf(
                "Manual",
                "Matic"

            )
            options.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = tipe == option,
                        onClick = { onTipeChange(option) },
                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                    )
                    Text(
                        text = option,
                        modifier = Modifier.padding(start = 1.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    var expended by remember {
        mutableStateOf(false)
    }

    IconButton(onClick = { expended = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expended,
            onDismissRequest = { expended = false })
        {
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.hapus)) },
                onClick = {
                    expended = false
                    delete()
                })
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenPreview() {
    Mobpro1_assessment2Theme {
        DetailScreen(rememberNavController())
    }
}