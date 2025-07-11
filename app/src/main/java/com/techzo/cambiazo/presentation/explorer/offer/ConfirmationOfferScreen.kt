package com.techzo.cambiazo.presentation.explorer.offer

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.techzo.cambiazo.common.components.ArticleExchange
import com.techzo.cambiazo.common.components.ButtonApp
import com.techzo.cambiazo.common.components.ButtonIconHeaderApp
import com.techzo.cambiazo.common.components.DialogApp
import com.techzo.cambiazo.common.components.MainScaffoldApp
import com.techzo.cambiazo.presentation.navigate.Routes

@Composable
fun ConfirmationOfferScreen(
    navController: NavController,
    viewModel: ConfirmationOfferViewModel = hiltViewModel()
) {
    val desiredProduct by viewModel.desiredProduct.collectAsState()
    val offeredProduct by viewModel.offeredProduct.collectAsState()
    val locationLocker by viewModel.locationLocker.collectAsState()
    val offerSuccess by viewModel.offerSuccess.collectAsState()
    val offerFailure by viewModel.offerFailure.collectAsState()

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
                        text = "¿Listo para enviar",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "tu oferta?",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                desiredProduct?.let { productLeft ->
                    offeredProduct?.let { productRight ->
                        // Título antes de mostrar la ubicación
                        Text(
                            text = "Lugar del Intercambio:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Apartment,
                                    contentDescription = "Ubicación de la Sede",
                                    tint = Color(0xFFFFD146)
                                )
                                if (locationLocker != null) {
                                    Text(
                                        text = locationLocker?.name ?: "Sede no disponible",
                                        color = Color(0xFF9F9C9C),
                                        modifier = Modifier.padding(start = 6.dp)
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.LocationOn,
                                    contentDescription = "Ubicación de casillero",
                                    tint = Color(0xFFFFD146)
                                )
                                if (locationLocker != null) {
                                    Text(
                                        text = locationLocker?.address ?: "Ubicación no disponible",
                                        color = Color(0xFF9F9C9C),
                                        modifier = Modifier.padding(start = 6.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Título antes de mostrar la card
                        Text(
                            text = "Resumen de la Oferta:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )
                        ArticleExchange(
                            productLeft = productLeft,
                            productRight = productRight,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        ButtonApp(
                            text = "Listo",
                            onClick = {
                                viewModel.makeOffer()
                            }
                        )

                    }
                }

                offerSuccess.takeIf { it }?.let {
                    var showDialog by remember { mutableStateOf(true) }

                    showDialog.takeIf { it }?.let {
                        DialogApp(
                            message = "¡Oferta Enviada!",
                            description = "Te notificaremos el estado de tu solicitud. Ya sea que el otro usuario acepte o decline tu oferta.",
                            labelButton1 = "Volver",
                            onClickButton1 = {
                                navController.popBackStack(Routes.ProductDetails.route, inclusive = false)
                                showDialog = false
                            }
                        )
                    }
                }

                offerFailure.takeIf { it }?.let {
                    var showDialog by remember { mutableStateOf(true) }

                    showDialog.takeIf { it }?.let {
                        DialogApp(
                            message = "Oferta No Realizada",
                            description = "Esta oferta ya ha sido rechazada anteriormente.",
                            labelButton1 = "Volver",
                            onClickButton1 = {
                                navController.popBackStack(Routes.ProductDetails.route, inclusive = false)
                                showDialog = false
                            }
                        )
                    }
                }
            }
        }
    )
}
