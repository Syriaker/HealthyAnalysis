package com.healthanalysis.app.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthanalysis.app.presentation.theme.*

@Composable
fun AuthStubScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(48.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Primary, PrimaryDark)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.HealthAndSafety,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "\u0417\u0434\u043E\u0440\u043E\u0432\u044B\u0439 \u0410\u043D\u0430\u043B\u0438\u0437",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "\u0412\u043E\u0439\u0434\u0438\u0442\u0435 \u0432 \u0430\u043A\u043A\u0430\u0443\u043D\u0442 \u0434\u043B\u044F \u043F\u0440\u043E\u0434\u043E\u043B\u0436\u0435\u043D\u0438\u044F",
                fontSize = 16.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}
