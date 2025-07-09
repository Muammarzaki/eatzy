package com.github.eatzy.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.eatzy.ui.component.LogoutButton
import com.github.eatzy.ui.component.MenuListItem
import com.github.eatzy.ui.component.ProfileHeader
import com.github.eatzy.ui.theme.Green80
import com.github.eatzy.ui.theme.LightGreen40
import com.github.eatzy.ui.theme.Mint40


@Composable
fun ProfileScreen() {
    Surface(
        color = LightGreen40,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Spacer(Modifier.height(24.dp))
            ProfileHeader(name = "User (((((", phone = "+91 9996418776", cornerRadius = 16.dp)
            Spacer(Modifier.height(16.dp))

            Spacer(Modifier.height(24.dp))

            MenuSection()
            Spacer(Modifier.height(24.dp))

            HelpAndFeedbackSection()
            Spacer(Modifier.height(24.dp))

            LogoutButton()
        }
    }
}


@Composable
fun MenuSection() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        MenuListItem(
            icon = Icons.Default.LocationOn,
            title = "Addresses",
            subtitle = "Update your address for better...",
            onClick = { /*TODO*/ }
        )
        MenuListItem(
            icon = Icons.Default.Notifications,
            title = "Notifications",
            subtitle = "Personalised your notifications here",
            isSelected = true,
            onClick = { /*TODO*/ }
        )
        MenuListItem(
            icon = Icons.Default.History,
            title = "Orders History",
            subtitle = "See all past orders here",
            onClick = { /*TODO*/ }
        )
    }
}


@Composable
fun HelpAndFeedbackSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            HelpItem(
                icon = Icons.AutoMirrored.Default.Chat,
                title = "Help",
                subtitle = "Talk with us to resolve your problem",
                onClick = { /*TODO*/ }
            )
            HelpItem(
                icon = Icons.Default.Feedback,
                title = "Feedback",
                subtitle = "Give us feedback to improve the app",
                onClick = { /*TODO*/ }
            )
        }
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    ProfileScreen()
}