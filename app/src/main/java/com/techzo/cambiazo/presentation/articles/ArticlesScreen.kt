package com.techzo.cambiazo.presentation.articles


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.techzo.cambiazo.common.components.ArticlesOwn
import com.techzo.cambiazo.common.components.FloatingButtonApp
import com.techzo.cambiazo.common.components.MainScaffoldApp
import com.techzo.cambiazo.common.components.TextTitleHeaderApp
import com.techzo.cambiazo.domain.Product

@Composable
fun ArticlesScreen(
    viewModel: ArticlesViewModel = hiltViewModel(),
    bottomBar: @Composable () -> Unit = {},
    onPublish: (Product?) -> Unit = {},
    onProductClick: (Int, Int) -> Unit,
) {
    val productsState = viewModel.products.collectAsState()
    val products = productsState.value.data?.filter { it.available } ?: emptyList()

    MainScaffoldApp(
        bottomBar = bottomBar,
        paddingCard = PaddingValues(start = 15.dp, end = 15.dp, top = 25.dp),
        contentsHeader = {
            Spacer(modifier = Modifier.height(30.dp))
            TextTitleHeaderApp(text = "Artículos")
            Spacer(modifier = Modifier.height(30.dp))
        }
    ) {
        Box {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                items(products.chunked(2) ?: emptyList()) { rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        rowItems.forEach {product->
                            ArticlesOwn(
                                product = product,
                                Modifier.weight(1f),
                                iconActions = true,
                                deleteProduct = { productId ->
                                    viewModel.deleteProduct( productId, product.image)
                                },
                                editProduct = {onPublish(it)},
                                onClick = onProductClick
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
                item { Spacer(modifier = Modifier.height(70.dp)) }
            }

            FloatingButtonApp(
                text = "+ Publicar",
                modifier = Modifier.align(Alignment.BottomCenter)
            ) { onPublish(null) }
        }
    }
}
