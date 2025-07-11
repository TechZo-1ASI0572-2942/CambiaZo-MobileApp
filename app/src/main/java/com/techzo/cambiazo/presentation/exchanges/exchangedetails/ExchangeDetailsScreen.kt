package com.techzo.cambiazo.presentation.exchanges.exchangedetails

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.skydoves.landscapist.glide.GlideImage
import com.techzo.cambiazo.R
import com.techzo.cambiazo.common.Constants
import com.techzo.cambiazo.common.components.ButtonApp
import com.techzo.cambiazo.common.components.CustomTabs
import com.techzo.cambiazo.common.components.DialogApp
import com.techzo.cambiazo.common.components.MainScaffoldApp
import com.techzo.cambiazo.domain.Exchange
import com.techzo.cambiazo.domain.ExchangeLocker
import com.techzo.cambiazo.presentation.exchanges.ExchangeViewModel
import com.techzo.cambiazo.presentation.explorer.review.ReviewViewModel
import com.techzo.cambiazo.ui.theme.ScreenBackground
import kotlinx.coroutines.launch


@Composable
fun ExchangeDetailsScreen(
    goBack: (Int) -> Unit,
    goToReviewScreen: (Int) -> Unit,
    viewModel: ExchangeViewModel = hiltViewModel(),
    exchangeId: Int,
    page: Int
) {

    LaunchedEffect(Unit) {
        viewModel.getExchangeById(exchangeId)
        viewModel.getExchangeLocker(exchangeId)
    }

    var showDialog by remember { mutableStateOf(false) }

    val exchange = viewModel.exchange.value
    val boolean = exchange.data?.userOwn?.id == Constants.user?.id

    MainScaffoldApp(
        paddingCard = PaddingValues(top = 10.dp),
        contentsHeader = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { goBack(page) }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", modifier = Modifier.size(35.dp))                }

                Text(
                    text = "Detalle del\nIntercambio",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 20.dp)
                )
                Spacer(modifier = Modifier.width(50.dp))
            }
        }
    ) {
        exchange.data?.let{ exchange ->
            val  profilePicture= when (page) {
                0 -> exchange.userChange.profilePicture
                1 -> exchange.userOwn.profilePicture
                else -> if(boolean) exchange.userChange.profilePicture else exchange.userOwn.profilePicture
            }

            val userName= when (page) {
                0 -> exchange.userChange.name
                1 -> exchange.userOwn.name
                else -> if(boolean) exchange.userChange.name else exchange.userOwn.name
            }

            val productName= when (page) {
                0 -> exchange.productChange.name
                1 -> exchange.productOwn.name
                else ->if(boolean) exchange.productChange.name else exchange.productOwn.name
            }

            val productName2= when (page) {
                0 -> exchange.productOwn.name
                1 -> exchange.productChange.name
                else -> if(boolean) exchange.productOwn.name else exchange.productChange.name
            }

            val price= when (page) {
                0 -> exchange.productChange.price
                1 -> exchange.productOwn.price
                else -> if(boolean) exchange.productChange.price else exchange.productOwn.price
            }

            val price2= when (page) {
                0 -> exchange.productOwn.price
                1 -> exchange.productChange.price
                else -> if(boolean) exchange.productOwn.price else exchange.productChange.price
            }

            val status= if (page == 0 && exchange.status == "Pendiente") "Enviado"
            else if(page==1) exchange.status
            else "WhatsApp"

            val description= when (page) {
                0 -> exchange.productChange.description
                1 -> exchange.productOwn.description
                else -> if(boolean) exchange.productChange.description else exchange.productOwn.description
            }

            val location= when (page) {
                0 -> viewModel.getLocationString(exchange.productChange.districtId)
                1 -> viewModel.getLocationString(exchange.productOwn.districtId)
                else -> viewModel.getLocationString(exchange.productChange.districtId)
            }

            val productImage= when (page) {
                0 -> exchange.productChange.image
                1 -> exchange.productOwn.image
                else -> if(boolean) exchange.productChange.image else exchange.productOwn.image
            }

            val productImage2= when (page) {
                0 -> exchange.productOwn.image
                1 -> exchange.productChange.image
                else -> if(boolean) exchange.productOwn.image else exchange.productChange.image
            }

            val textUnderImage = when (page) {
                0 -> "Quieres"
                1 -> "Ofrece"
                else -> null
            }

            val textUnderImage2 = when (page) {
                0 -> "Ofreces"
                1 -> "Quiere"
                else -> "Hiciste cambio por:"
            }

            val phoneNumber =when (page) {
                0 -> "+51${exchange.userChange.phoneNumber}"
                1 -> "+51${exchange.userOwn.phoneNumber}"
                else -> if(boolean) "+51${exchange.userChange.phoneNumber}" else "+51${exchange.userOwn.phoneNumber}"
            }

            val textColorStatus= when (page) {
                0 -> if (exchange.status == "Pendiente") Color.Gray else Color(0xFF38B000)
                1 -> Color(0xFFFFD146)
                else -> Color.White
            }

            val textBackgroundColor= when (page) {
                0 -> if (exchange.status == "Pendiente") Color(0xFFE8E8E8) else Color(0xFF38B000)
                1 -> Color.Black
                else -> Color(0xFF38B000)
            }

            val context = LocalContext.current

            val authorId= if(boolean) exchange.userOwn.id else exchange.userChange.id
            val receptorId = if(!boolean) exchange.userChange.id else exchange.userOwn.id

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ){
                Column(modifier = Modifier.padding(start = 15.dp, end = 15.dp)) {
                    val userId = if (!boolean) exchange.userOwn.id else exchange.userChange.id
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable(
                                onClick = { goToReviewScreen(userId) },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            )
                        ) {
                            GlideImage(
                                imageModel = { profilePicture},
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = userName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = status,
                            color = textColorStatus,
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(textBackgroundColor)
                                .padding(horizontal = 20.dp, vertical = 3.dp)
                                .clickable {
                                    if(page==2){
                                        val formattedNumber = phoneNumber.replace("+", "").replace(" ", "")
                                        val url = "https://wa.me/$formattedNumber"
                                        val intent = Intent(Intent.ACTION_VIEW).apply {
                                            data = Uri.parse(url)
                                        }
                                        context.startActivity(intent)
                                    }
                                }
                            ,
                            fontWeight = FontWeight.Bold, fontSize = 14.sp,
                        )
                    }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        GlideImage(
                            imageModel = {productImage},
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(0.2.dp, Color(0xFFDCDCDC), RoundedCornerShape(10.dp))
                                .height(260.dp)
                        )

                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(20.dp)
                                .background(
                                    Color.Black.copy(alpha = 0.7f), RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "S/${price} valor aprox.",
                                color = Color(0xFFFFD146),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                    Box(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp)){
                        Column(verticalArrangement = Arrangement.spacedBy(5.dp)){
                            if(textUnderImage!=null){
                                Text(textUnderImage, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF6D6D6D))
                            }
                            Text(productName, fontWeight = FontWeight.Bold, fontSize = 26.sp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.LocationOn,
                                    contentDescription = "Ubicación",
                                    tint = Color(0xFFFFD146)
                                )
                                Text(
                                    text = location,
                                    color = Color(0xFF9F9C9C),
                                    modifier = Modifier.padding(start = 1.dp)
                                )
                            }
                            Text(text = description, fontSize = 16.sp, lineHeight = 20.sp)

                            MyPopupDialog(
                                showDialog = showDialog,
                                onDismiss = { showDialog = false },
                                locker = viewModel.exchangeLocker.value.data,
                                exchange = exchange,
                                initialPage = page,
                                exchangeId = exchange.id,
                                viewModel = viewModel
                            )
                        }
                    }

                }

                Spacer(modifier = Modifier.weight(1f))

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = Color(0xFFDCDCDC),
                    thickness = 1.dp
                )

                Column(modifier = Modifier.padding(start = 15.dp, end = 15.dp)) {
                    BoxUnderExchange(textUnderImage2,productImage2, productName2, price2.toString(), page, exchangeId = exchange.id, goBack = {goBack(page)}, userAuthor = authorId, userReceptor = receptorId)
                    ButtonApp(
                        text = "Ver Locker",
                        onClick = {
                            viewModel.getExchangeLocker(exchangeId)
                            showDialog = true },
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    }
}


@Composable
fun MyPopupDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    locker: ExchangeLocker?,
    exchange: Exchange,
    initialPage: Int = 0,
    exchangeId: Int,
    viewModel: ExchangeViewModel
) {
    var selectedTab by remember { mutableStateOf("Locker de Envío") }

    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .background(Color.White, shape = RoundedCornerShape(25.dp))
                    .padding(30.dp),
            ) {
                LockerInfoSection(
                    locker = locker,
                    exchange = exchange,
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    onTabChange = { viewModel.loadExchangeLocker(exchangeId) } // Llamada al método
                )
            }
        }
    }
}


@Composable
fun LockerInfoSection(
    locker: ExchangeLocker?,
    exchange: Exchange,
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    onTabChange: (String) -> Unit // Callback to reload data
) {
    val tabs = listOf("Locker de Envío", "Locker de Recojo")
    val selectedIndex = tabs.indexOf(selectedTab)

    Column(modifier = Modifier.fillMaxWidth()) {
        ScrollableTabRow(
            selectedTabIndex = selectedIndex,
            containerColor = Color.Transparent,
            edgePadding = 0.dp,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                    color = Color(0xFFFFC107)
                )
            },
            divider = {}
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTab == tab,
                    onClick = {
                        onTabSelected(tab)
                        onTabChange(tab) // Reload data when switching tabs
                    },
                    selectedContentColor = Color(0xFFFFC107),
                    unselectedContentColor = Color.Gray
                ) {
                    Text(
                        text = tab,
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        if (selectedTab == "Locker de Envío") {
            LockerEnvioSection(locker, exchange)
        } else {
            LockerRecogerSection(locker, exchange)
        }
    }
}


@Composable
fun LockerEnvioSection(locker: ExchangeLocker?, exchange: Exchange) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 0.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            LockerInfoItem(title = "Locker No.", value = locker?.lockerDepositId?.toString() ?: "--")
            LockerInfoItem(
                title = "PIN de apertura",
                value = if (locker?.stateExchangeLockerDeposit == "EMPTY") locker.pinDeposit ?: "--" else "----"
            )
        }

        Spacer(Modifier.height(16.dp))

        InstructionsEnvio()

        Spacer(Modifier.height(16.dp))

        // Imagen y estado
        if (locker?.stateExchangeLockerDeposit == "EMPTY") {
            LockerImageWithOut(
                imageUrl = null,
                statusText = "Esperando el deposito del objeto"
            )
        } else if (locker?.stateExchangeLockerDeposit == "FULL" && exchange != null) {
            LockerImageWithIn(
                imageUrl = exchange.productChange.image,
                statusText = "Evidencia confirmada del deposito"
            )
        } else if (locker?.stateExchangeLockerDeposit == "DELIVERED" && exchange != null) {
            LockerImageWithIn(
                imageUrl = exchange.productChange.image,
                statusText = "Objeto retirado exitosamente"
            )
        }
    }
}

@Composable
fun LockerRecogerSection(locker: ExchangeLocker?, exchange: Exchange) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 0.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            LockerInfoItem(title = "Locker No.", value = locker?.lockerRetrieveId?.toString() ?: "--")
            LockerInfoItem(
                title = "PIN de apertura",
                value = if (locker?.stateExchangeLockerRetrieve == "FULL") locker.pinRetrieve ?: "--" else "----"
            )
        }

        Spacer(Modifier.height(16.dp))

        InstructionsRecoger()

        Spacer(Modifier.height(16.dp))

        if (locker?.stateExchangeLockerRetrieve == "EMPTY") {
            LockerImageWithOut(
                imageUrl = null,
                statusText = "Esperando el deposito del objeto"
            )
        } else if (locker?.stateExchangeLockerRetrieve == "FULL" && exchange != null) {
            LockerImageWithIn(
                imageUrl = exchange.productOwn.image,
                statusText = "Objeto depositado, recógelo"
            )
        } else if (locker?.stateExchangeLockerRetrieve == "DELIVERED" && exchange != null) {
            LockerImageWithIn(
                imageUrl = exchange.productOwn.image,
                statusText = "Objeto retirado exitosamente"
            )
        }
    }
}
@Composable
fun LockerInfoItem(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Color.Black
        )
        Text(
            text = value,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFC107) // Amarillo
        )
    }
}



@Composable
fun InstructionsEnvio() {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = 5.dp)
        ) {
            Text(
                text = "Instrucciones de envío",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = "Expandir instrucciones"
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    val steps = listOf(
                        "Acércate al locker asignado.",
                        "Ingresa el PIN en el panel del locker.",
                        "El locker se abrirá automáticamente.",
                        "Coloca el objeto y cierra bien la puerta.",
                    )
                    steps.forEachIndexed { index, step ->
                        Row(modifier = Modifier.padding(vertical = 2.dp)) {
                            Text(
                                text = "${index + 1}.",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                modifier = Modifier.width(18.dp)
                            )
                            Text(text = step, fontSize = 15.sp,)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InstructionsRecoger() {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = 5.dp)
        ) {
            Text(
                text = "Instrucciones de recogida",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = "Expandir instrucciones"
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    val steps = listOf(
                        "Espera la confirmación del objeto.",
                        "Se te enviará un PIN de apertura.",
                        "Ve al locker indicado.",
                        "Ingresa el PIN asignado.",
                        "El locker se abrirá automáticamente.",
                        "Recoge tu objeto y cierra el locker."
                    )
                    steps.forEachIndexed { index, step ->
                        Row(modifier = Modifier.padding(vertical = 2.dp)) {
                            Text(
                                text = "${index + 1}.",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                modifier = Modifier.width(18.dp)
                            )
                            Text(text = step, fontSize = 15.sp)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun LockerImageWithOut(imageUrl: String?, statusText: String) {
    val (backgroundColor, textColor) = when (statusText) {
        "Objeto depositado, recógelo",
        "Evidencia confirmada del deposito",
        "Objeto retirado exitosamente"-> Color(0xFFDFF0D8) to Color(0xFF2E7D32)
        "Esperando el deposito del objeto" -> Color(0xFFE0E0E0) to Color.Black
        else -> Color.LightGray to Color.DarkGray
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color.White,
            shadowElevation = 4.dp
        ) {
            Image(
                painter = painterResource(id = R.drawable.waiting_package),
                contentDescription = "Locker icon",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(10.dp)),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(8.dp))
                .padding(vertical = 10.dp, horizontal = 15.dp)
        ) {
            Text(
                text = statusText,
                fontSize = 14.sp,
                color = textColor
            )
        }
    }
}


@Composable
fun LockerImageWithIn(imageUrl: String?, statusText: String) {
    val (backgroundColor, textColor) = when (statusText) {
        "Objeto depositado, recógelo",
        "Objeto retirado exitosamente",
        "Evidencia confirmada del deposito" -> Color(0xFFDFF0D8) to Color(0xFF2E7D32)
        "Esperando el deposito del objeto" -> Color(0xFFE0E0E0) to Color.Black
        else -> Color.LightGray to Color.DarkGray
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color.White,
            shadowElevation = 4.dp
        ) {
            GlideImage(
                imageModel = { imageUrl },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(10.dp)),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(8.dp))
                .padding(vertical = 10.dp, horizontal = 15.dp)
        ) {
            Text(
                text = statusText,
                fontSize = 14.sp,
                color = textColor
            )
        }
    }
}


@Composable
fun BoxUnderExchange(textUnderImage:String, image:String, productName: String, price: String, page: Int, viewModel: ExchangeViewModel = hiltViewModel(), reviewViewModel: ReviewViewModel = hiltViewModel(), exchangeId: Int, goBack: () -> Unit, userAuthor:Int, userReceptor: Int) {
    var showDialog by remember { mutableStateOf(false) }
    var showDialog2 by remember { mutableStateOf(false) }
    var showDialog3 by remember { mutableStateOf(false) }
    var showDialog4 by remember { mutableStateOf(false) }

    if(page == 0){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .padding(bottom = 20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(textUnderImage, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF6D6D6D))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlideImage(
                        imageModel = { image },
                        modifier = Modifier
                            .fillMaxWidth(0.45f)
                            .height(140.dp)
                            .border(0.2.dp, Color(0xFFDCDCDC), RoundedCornerShape(10.dp))
                            .clip(RoundedCornerShape(10.dp))
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Column(
                        modifier = Modifier.padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            productName,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            "S/${price} valor aprox.",
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            color = Color(0xFFFFD146),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                    }
                }
            }
        }

    }
    if(page == 1){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(textUnderImage, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF6D6D6D))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlideImage(
                        imageModel = { image },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(100.dp)
                            .border(0.2.dp, Color(0xFFDCDCDC), RoundedCornerShape(10.dp))
                            .clip(RoundedCornerShape(10.dp))
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Column(modifier = Modifier.fillMaxWidth(1f).padding(horizontal = 5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(productName, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("S/${price} valor aprox.", textAlign = TextAlign.Center, fontSize = 16.sp, color = Color(0xFFFFD146), fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.width(10.dp).height(20.dp))
            }
        }
    }

}
