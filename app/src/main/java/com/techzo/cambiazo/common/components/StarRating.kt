package com.techzo.cambiazo.common.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.twotone.StarHalf
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun StarRating(rating: Double, size : Dp) {
    val fullStars = rating.toInt()
    val partialStar = rating - fullStars
    val emptyStars = 5 - fullStars - if (partialStar > 0) 1 else 0

    Row() {
        repeat(fullStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = Color(0xFFFFD146),
                modifier = Modifier.size(size)
            )
        }

        if (partialStar > 0) {
            Icon(
                imageVector = Icons.TwoTone.StarHalf,
                contentDescription = null,
                tint = Color(0xFFFFD146),
                modifier = Modifier.size(size)
            )
        }

        repeat(emptyStars) {
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = Color(0xFFFFD146),
                modifier = Modifier.size(size)
            )
        }
    }
}