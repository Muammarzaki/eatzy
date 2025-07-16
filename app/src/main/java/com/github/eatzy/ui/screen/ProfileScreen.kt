package com.github.eatzy.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.eatzy.domain.User
import com.github.eatzy.ui.component.HelpItem
import com.github.eatzy.ui.component.LogoutButton
import com.github.eatzy.ui.component.MenuListItem
import com.github.eatzy.ui.component.ProfileHeader
import com.github.eatzy.ui.theme.EaTzyTheme


@Composable
fun ProfileScreen(bottomBar: @Composable () -> Unit = {}, user: User) {
    Scaffold(
        bottomBar = bottomBar,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Spacer(Modifier.height(24.dp))
            ProfileHeader(name = user.name, phone = user.phoneNumber, cornerRadius = 16.dp)
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    EaTzyTheme {
        ProfileScreen(
            bottomBar = {},
            user = User(
                name = "John Doe",
                phoneNumber = "+1234567890",
                email = "helloworld@gmail.com",
                business = null
            )
        )
    }
}