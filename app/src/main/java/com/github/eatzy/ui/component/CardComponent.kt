package com.github.eatzy.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LineWeight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.eatzy.ui.screen.HelpItem
import com.github.eatzy.ui.theme.EaTzyTheme
import com.github.eatzy.ui.theme.GrayDark
import com.github.eatzy.ui.theme.Green80
import com.github.eatzy.ui.theme.Mint40

@Composable
fun WeightIndicatorCard(
    modifier: Modifier = Modifier,
    weight: Int = 70,
    progress: Float = 0.85f,
    bmiStatus: String = "You have a healthy BMI"
) {
    // Warna yang digunakan dalam komponen
    val cardBackgroundColor = Color(0xFFE6F8F0)
    val primaryColor = Color(0xFF0D63F3)
    val trackColor = Color(0xFFE0E0E0)
    val textColorPrimary = Color.Black
    val textColorSecondary = Color.Gray

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp), // Memberi tinggi yang tetap pada kartu
        shape = RoundedCornerShape(20.dp), // Sudut yang lebih tumpul
        colors = CardDefaults.cardColors(
            containerColor = cardBackgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Bagian Kiri: Teks Berat dan Status BMI
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$weight kg",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColorPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = bmiStatus,
                    fontSize = 14.sp,
                    color = textColorSecondary
                )
            }

            // Bagian Kanan: Lingkaran Progres dengan Ikon
            WeightProgressIndicator(
                progress = progress,
                primaryColor = primaryColor,
                trackColor = trackColor
            )
        }
    }
}

@Composable
private fun WeightProgressIndicator(
    progress: Float,
    primaryColor: Color,
    trackColor: Color,
    modifier: Modifier = Modifier
) {
    val strokeWidth = 12f
    val indicatorSize = 60.dp

    // Box untuk menumpuk Canvas dan Ikon
    Box(
        modifier = modifier.size(indicatorSize),
        contentAlignment = Alignment.Center
    ) {
        // Menggunakan Canvas untuk kontrol penuh atas gambar lingkaran
        Canvas(modifier = Modifier.fillMaxSize()) {
            val startAngle = 135f
            val sweepAngle = 270f

            // Menggambar track (latar belakang) lingkaran
            drawArc(
                color = trackColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            drawArc(
                color = primaryColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle * progress,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Icon(
            imageVector = Icons.Default.LineWeight,
            contentDescription = "Weight Icon",
            tint = primaryColor,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeightIndicatorCardPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        WeightIndicatorCard()
    }
}

@Composable
fun MenuListItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val border = if (isSelected) BorderStroke(2.dp, Color.White) else null

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = border,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Green80,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(text = subtitle, color = Green80, fontSize = 12.sp)
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun MenuListItemPreview() {
    EaTzyTheme {
        MenuListItem(
            icon = Icons.Default.LocationOn,
            title = "Addresses",
            subtitle = "Update your address for better...",
            onClick = { /*TODO*/ }
        )
    }

}

@Composable
fun HelpItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Mint40,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Text(text = subtitle, color = Green80, fontSize = 12.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HelpItemPreview() {
    EaTzyTheme {
        HelpItem(
            icon = Icons.Default.Chat,
            title = "Help",
            subtitle = "Talk with us to resolve your problem",
            onClick = { /*TODO*/ }
        )
    }

}

@Composable
fun ProfileHeader(name: String, phone: String, cornerRadius: Dp = 12.dp) {
    Column {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius))
                .background(Color.White)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "User Avatar",
                modifier = Modifier.size(60.dp),
                tint = Color.Gray
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = phone, fontSize = 14.sp, color = GrayDark)
            }
            IconButton(onClick = { /* TODO: Aksi untuk edit profil */ }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profile",
                    tint = Color.Gray
                )
            }
        }
        MembershipStatusBanner(12.dp)
    }
}

@Preview(showBackground = false)
@Composable
private fun ProfileHeaderPreview() {
    EaTzyTheme {
        ProfileHeader(name = "User (((((", phone = "+91 9996418776")
    }
}