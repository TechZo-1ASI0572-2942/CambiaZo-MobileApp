package com.techzo.cambiazo.presentation.articles


import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import coil.compose.AsyncImage
import com.skydoves.landscapist.glide.GlideImage
import com.techzo.cambiazo.common.components.ArticlesOwn
import com.techzo.cambiazo.common.components.DialogApp
import com.techzo.cambiazo.common.components.MainScaffoldApp
import com.techzo.cambiazo.common.components.TextTitleHeaderApp
import com.techzo.cambiazo.domain.Product

@Composable
fun ArticlesScreen(
    viewModel: ArticlesViewModel = hiltViewModel(),
    bottomBar : @Composable () -> Unit = {},
    onPublish: () -> Unit = {}
){

    val products = viewModel.products.value

    MainScaffoldApp(
        bottomBar = bottomBar,
        paddingCard = PaddingValues(start=15.dp,end=15.dp,top=25.dp),
        contentsHeader = {
            Spacer(modifier = Modifier.height(30.dp))
            TextTitleHeaderApp(text ="Artículos")
            Spacer(modifier = Modifier.height(30.dp))
        }
    ) {

        Box {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                items(products.data?.chunked(2) ?: emptyList()) { rowItems ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White),
                        horizontalArrangement = Arrangement.SpaceBetween){

                            rowItems.forEach {
                                ArticlesOwn(
                                    product = it,Modifier.weight(1f),
                                    iconActions = true,
                                    deleteProduct = {productId->viewModel.deleteProduct(productId)},
                                    editProduct = {},
                                )
                            }
                            if (rowItems.size == 1) {
                                Spacer(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                )
                            }
                    }
                }
                item{Spacer(modifier = Modifier.height(70.dp))}
            }

            FloatingButtonApp( modifier = Modifier.align(Alignment.BottomCenter)){
                onPublish()
            }

        }
    }
}



@Composable
fun FloatingButtonApp(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .height(65.dp)
            .padding(bottom = 10.dp, top = 10.dp)
            .border(1.5.dp, color = Color(0xFFFFD146), RoundedCornerShape(10.dp)),
        containerColor = Color(0xFFFFD146),
        contentColor = Color.Black,
        shape = RoundedCornerShape(10.dp),
    ) {
        Text(
            text = "+ Publicar",
            fontSize = 18.sp,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif)
        )
    }
}
