package com.techzo.cambiazo.presentation.auth.changepassword.otpcodeverificationscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.techzo.cambiazo.common.components.ButtonApp
import com.techzo.cambiazo.common.components.CustomInput
import com.techzo.cambiazo.common.components.MainScaffoldApp
import com.techzo.cambiazo.presentation.auth.changepassword.ChangePasswordViewModel

@Composable
fun OtpCodeVerificationScreen(
    goBack: () -> Unit,
    goNewPassword: () -> Unit,
    changePasswordViewModel: ChangePasswordViewModel= hiltViewModel()
) {
    var firstDigit by remember { mutableStateOf("") }
    var secondDigit by remember { mutableStateOf("") }
    var thirdDigit by remember { mutableStateOf("") }
    var fourthDigit by remember { mutableStateOf("") }

    MainScaffoldApp(
        paddingCard = PaddingValues(top = 10.dp),
        contentsHeader = {
            Column(
                modifier = Modifier.padding(bottom = 50.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 0.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { goBack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 0.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Código de\nVerificación",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 40.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 20.dp)
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 30.dp, horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Ingrese el código enviado a su correo electrónico",
                textAlign = TextAlign.Center,
                fontSize = 19.sp,
                letterSpacing = 1.2.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (i in 1..4) {
                    CustomInput(
                        value = when (i) {
                            1 -> firstDigit
                            2 -> secondDigit
                            3 -> thirdDigit
                            4 -> fourthDigit
                            else -> ""
                        },
                        placeHolder = "-",
                        type = "Number",
                        onValueChange = { newValue ->
                            when (i) {
                                1 -> firstDigit = newValue
                                2 -> secondDigit = newValue
                                3 -> thirdDigit = newValue
                                4 -> fourthDigit = newValue
                                else -> {}
                            }
                        },
                        modifier = Modifier
                            .width(50.dp) // Ajusta el ancho de cada input
                            .padding(horizontal = 4.dp)
                            .height(56.dp) // Ajusta la altura si es necesario
                    )
                }
            }

            Text(
                text = "Ingrese el código de 4 dígitos",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                letterSpacing = 1.2.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            )
            
            ButtonApp(
                text = "Verificar",
                onClick = {
                }
            )
        }
    }
}