package com.github.eatzy.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.eatzy.ui.theme.Danger
import com.github.eatzy.ui.theme.EaTzyTheme
import com.github.eatzy.ui.theme.Green40

@Composable
fun LogoutButton() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: Aksi logout */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Logout,
                    contentDescription = "Log out",
                    tint = Danger,
                    modifier = Modifier.rotate(180f)
                )
            }
            Spacer(Modifier.width(16.dp))
            Text(
                text = "Log out",
                fontWeight = FontWeight.Bold,
                color = Danger,
                fontSize = 16.sp
            )
        }
    }
}

@Preview
@Composable
private fun LogoutButtonPreview() {
    EaTzyTheme {
        LogoutButton()
    }
}

@Composable
fun MembershipStatusBanner(bottomCorner: Dp = 0.dp) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = bottomCorner, bottomEnd = bottomCorner),
        colors = CardDefaults.cardColors(containerColor = Green40)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "EATHLY PLUS MEMBER",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Preview
@Composable
private fun MembershipStatusBannerPreview() {
    EaTzyTheme {
        MembershipStatusBanner(16.dp)
    }
}