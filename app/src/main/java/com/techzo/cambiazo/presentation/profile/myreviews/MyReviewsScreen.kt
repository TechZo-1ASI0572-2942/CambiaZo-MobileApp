package com.techzo.cambiazo.presentation.profile.myreviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.techzo.cambiazo.common.components.ButtonIconHeaderApp
import com.techzo.cambiazo.common.components.EmptyStateMessage
import com.techzo.cambiazo.common.components.LoadingMessage
import com.techzo.cambiazo.common.components.MainScaffoldApp
import com.techzo.cambiazo.common.components.Products
import com.techzo.cambiazo.common.components.ReviewItem
import com.techzo.cambiazo.common.components.StarRating
import com.techzo.cambiazo.common.components.TextTitleHeaderApp
import com.techzo.cambiazo.presentation.profile.ProfileViewModel

@Composable
fun MyReviewsScreen(
    back: () -> Unit = {},
    profileViewModel: ProfileViewModel = hiltViewModel(),
    myReviewsViewModel: MyReviewsViewModel = hiltViewModel(),
    onUserClick: (Int) -> Unit
) {

    val averageRating = profileViewModel.averageRating.value
    val countReviews = profileViewModel.countReviews.value
    val state = myReviewsViewModel.state.value

    MainScaffoldApp(
        paddingCard = PaddingValues(top = 20.dp),
        contentsHeader = {
            Column(
                Modifier
                    .padding(bottom = 30.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ButtonIconHeaderApp(Icons.Filled.ArrowBack, onClick = { back() })
                TextTitleHeaderApp("Reseñas")
            }
        },
        content = {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                if (state.isLoading) {
                    LoadingMessage()
                } else {
                    if (averageRating != null && countReviews != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = String.format("%.1f", averageRating),
                                color = Color(0xFFFFD146),
                                modifier = Modifier.padding(top = 5.dp),
                                fontSize = 70.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.width(15.dp))

                            Column {
                                StarRating(rating = averageRating, size = 35.dp)
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(
                                    text = "$countReviews reseñas",
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(start = 5.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(30.dp))

                        if (state.data.isNullOrEmpty()) {
                            HorizontalDivider(color = Color(0xFFDCDCDC), thickness = 1.dp)

                            EmptyStateMessage(
                                icon = Icons.Default.StarOutline,
                                message = "Aún no tienes reseñas",
                                subMessage = "Intercambia y gana valoraciones.",
                            )
                        } else {
                            LazyColumn {
                                items(state.data ?: emptyList()) { review ->
                                    ReviewItem(review, onUserClick)
                                }
                            }
                        }
                    }
                }
            }
        }
    )

}
