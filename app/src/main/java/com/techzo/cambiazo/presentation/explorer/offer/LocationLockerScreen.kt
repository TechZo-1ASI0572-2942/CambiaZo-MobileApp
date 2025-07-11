package com.techzo.cambiazo.presentation.explorer.offer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.techzo.cambiazo.common.components.ArticleExchange
import com.techzo.cambiazo.common.components.ArticlesOwn
import com.techzo.cambiazo.common.components.ButtonApp
import com.techzo.cambiazo.common.components.ButtonIconHeaderApp
import com.techzo.cambiazo.common.components.DialogApp
import com.techzo.cambiazo.common.components.MainScaffoldApp
import com.techzo.cambiazo.presentation.articles.ArticlesViewModel
import com.techzo.cambiazo.presentation.navigate.Routes
import kotlinx.coroutines.launch

@Composable
fun LocationLockerScreen(
    navController: NavController,
    viewModel: LocationLockerViewModel = hiltViewModel()
) {
    val desiredProduct by viewModel.desiredProduct.collectAsState()
    val offeredProduct by viewModel.offeredProduct.collectAsState()
    val lockers by viewModel.lockerLocations.collectAsState()


    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    MainScaffoldApp(
        paddingCard = PaddingValues(horizontal = 20.dp, vertical = 15.dp),
        contentsHeader = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                ButtonIconHeaderApp(
                    iconVector = Icons.Filled.ArrowBack,
                    onClick = { navController.popBackStack() },
                    iconSize = 35.dp,
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 30.dp)
                ) {
                    Text(
                        text = "¿Dónde deseas",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "intercambiar?",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        content = {
            Spacer(modifier = Modifier.height(15.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(lockers) { locker ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp))
                            .clickable {
                                coroutineScope.launch {
                                        val desiredId = desiredProduct?.id
                                        val offeredId = offeredProduct?.id
                                        val lockerId = locker.id
                                        if (desiredId != null && offeredId != null) {
                                            navController.navigate(
                                                Routes.ConfirmationOffer.createConfirmationOfferRoute(
                                                    desiredId.toString(),
                                                    offeredId.toString(),
                                                    lockerId.toString()
                                                )
                                            )
                                        } else {
                                            dialogMessage = "Producto deseado u ofrecido no encontrado."
                                            showDialog = true
                                        }
                                    }
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Primera fila: Icono Apartment + nombre
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Apartment,
                                    contentDescription = "Sede",
                                    tint = Color(0xFFFFC107), // Amarillo
                                    modifier = Modifier.size(35.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = locker.name,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF333333)
                                )
                            }

                            // Segunda fila: Icono Location + dirección
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.LocationOn,
                                    contentDescription = "Ubicación",
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = locker.address,
                                    fontSize = 14.sp,
                                    color = Color(0xFF777777)
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}