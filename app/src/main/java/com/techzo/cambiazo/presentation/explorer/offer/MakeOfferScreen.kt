package com.techzo.cambiazo.presentation.explorer.offer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.techzo.cambiazo.R
import com.techzo.cambiazo.common.components.ArticlesOwn
import com.techzo.cambiazo.common.components.MainScaffoldApp
import com.techzo.cambiazo.presentation.articles.ArticlesViewModel
import com.techzo.cambiazo.presentation.navigate.Routes
import com.techzo.cambiazo.ui.theme.ScreenBackground

@Composable
fun MakeOfferScreen(
    navController: NavController,
    viewModel: MakeOfferViewModel = hiltViewModel(),
    articlesViewModel: ArticlesViewModel = hiltViewModel()
) {
    val articlesState by articlesViewModel.products.collectAsState()
    val userProducts = articlesState.data?.filter { it.available } ?: emptyList()
    var showLockerDialog by remember { mutableStateOf(false) }


    var showSedesDialog by remember { mutableStateOf(false) }
    val sedesUbicaciones = listOf(
        "Sede Lima - Av. Principal 123" to LatLng(-12.046374, -77.042793),
        "Sede Callao - Av. La Marina 456" to LatLng(-12.056052, -77.116062),
        "Sede Miraflores - Calle Las Flores 789" to LatLng(-12.123456, -77.029123),
        "Sede San Isidro - Jr. Paz 101" to LatLng(-12.092345, -77.036789),
        "Sede Barranco - Malecón 202" to LatLng(-12.140000, -77.020000)
    )
    val lockerCode = "A10"
    val lockerPin = "4342"

    MainScaffoldApp(
        paddingCard = PaddingValues(horizontal = 20.dp, vertical = 15.dp),
        contentsHeader = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(35.dp)
                ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null)
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 30.dp)
                ) {
                    Text(
                        text = "¿Qué ofreces a",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "cambio?",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        content = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(userProducts) { product ->
                    ArticlesOwn(
                        product = product,
                        onClick = { _, _ ->
                            showSedesDialog = true
                        }
                    )
                }
                item {
                    AddProductButton(onPublish = { navController.navigate(Routes.Publish.route) })
                }
            }

            SedesFakeMapDialog(
                sedes = listOf(
                    "Sede Lima - Av. Principal 123",
                    "Sede Callao - Av. La Marina 456",
                    "Sede Miraflores - Calle Las Flores 789",
                    "Sede San Isidro - Jr. Paz 101",
                    "Sede Barranco - Malecón 202"
                ),
                showDialog = showSedesDialog,
                onDismiss = { showSedesDialog = false },
                onSedeSelected = {
                    showSedesDialog = false
                    showLockerDialog = true
                }
            )

            if (showLockerDialog) {
                AlertDialog(
                    onDismissRequest = { showLockerDialog = false },
                    containerColor = Color.White,
                    shape = RoundedCornerShape(20.dp),
                    tonalElevation = 10.dp,
                    title = {
                        Text(
                            text = "Locker asignado",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    text = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Locker No.", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text("A10", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = ScreenBackground)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("PIN de apertura", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text("4342", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = ScreenBackground)
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 24.dp)
                                    .background(Color.Black, RoundedCornerShape(50))
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Esperando confirmación del otro usuario",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showLockerDialog = false
                                navController.navigate("intercambios")
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ScreenBackground,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(10.dp),
                            elevation = ButtonDefaults.buttonElevation(4.dp)
                        ) {
                            Text("Ver mis intercambios", fontWeight = FontWeight.SemiBold)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showLockerDialog = false }) {
                            Text("Cerrar", fontWeight = FontWeight.SemiBold)
                        }
                    }
                )
            }

        }
    )
}

@Composable
fun MapMarker(
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val pinColor = if (selected) ScreenBackground else Color.Gray
        val strokeColor = Color.Black
        val w = size.width
        val h = size.height

        val path = androidx.compose.ui.graphics.Path().apply {
            moveTo(w / 2f, 0f)
            cubicTo(w, 0f, w, h * 0.7f, w / 2f, h)
            cubicTo(0f, h * 0.7f, 0f, 0f, w / 2f, 0f)
            close()
        }
        drawPath(path, pinColor)

        drawCircle(
            color = strokeColor,
            radius = w * 0.18f,
            center = Offset(w / 2f, h * 0.3f)
        )
    }
}

@Composable
fun SedesFakeMapDialog(
    sedes: List<String>,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSedeSelected: (String) -> Unit
) {
    var selectedIndex by remember { mutableStateOf(-1) }
    if (!showDialog) return

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 8.dp,
        titleContentColor = Color.Black,
        textContentColor = Color.Black,
        title = {
            Text("Selecciona una sede", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        },
        text = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .shadow(2.dp, RoundedCornerShape(12.dp))
            ) {
                val bg: Painter = painterResource(R.drawable.map_background)
                Image(
                    painter = bg,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize().clip(RoundedCornerShape(12.dp))
                )
                data class Region(val x: Dp, val y: Dp, val size: Dp)
                val regiones = listOf(
                    Region(16.dp, 16.dp, 32.dp),
                    Region(124.dp, 24.dp, 32.dp),
                    Region(216.dp, 40.dp, 32.dp),
                    Region(72.dp, 144.dp, 32.dp),
                    Region(208.dp, 152.dp, 32.dp)
                )
                regiones.forEachIndexed { i, (x, y, s) ->
                    Box(
                        Modifier
                            .offset(x, y)
                            .size(s)
                            .shadow(4.dp, RoundedCornerShape(4.dp))
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { selectedIndex = i },
                        contentAlignment = Alignment.TopCenter
                    ) {
                        MapMarker(selected = (selectedIndex == i), modifier = Modifier.fillMaxSize())
                    }

                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedIndex >= 0) onSedeSelected(sedes[selectedIndex])
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ScreenBackground,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text("Continuar", fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", fontWeight = FontWeight.SemiBold, color = Color.Black)
            }
        }
    )
}

@Composable
fun AddProductButton(onPublish: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(10.dp)
            .height(120.dp)
            .fillMaxWidth()
            .clickable(onClick = onPublish),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFD146))
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Agregar",
                tint = Color.Black,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}