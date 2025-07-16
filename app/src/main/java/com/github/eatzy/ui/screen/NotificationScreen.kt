package com.github.eatzy.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.eatzy.domain.UnreadableNotification
import com.github.eatzy.ui.component.SimpleNotificationCard
import com.github.eatzy.ui.component.TopAppBarComponent
import com.github.eatzy.ui.theme.EaTzyTheme
import kotlinx.coroutines.flow.flowOf
import java.util.Date

@Composable
fun NotificationScreen(
    unreadableNotifications: LazyPagingItems<UnreadableNotification>,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        topBar = {
            TopAppBarComponent(
                showNotificationBadge = unreadableNotifications.itemCount > 0,
                onBackClick = onBackClick,
                title = "Notifikasi"
            )
        },

        ) { contentPadding ->
        Column(
            Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(unreadableNotifications.itemCount) { index ->
                    val notification = unreadableNotifications.peek(index)
                    SimpleNotificationCard(
                        subject = notification?.subject ?: "Default Subject",
                        message = notification?.message ?: "Default Message"
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun NotificationScreenPreview() {
    val fakeItems = flowOf(
        PagingData.from(
            (1..10).map {
                UnreadableNotification(
                    id = it,
                    subject = "Notifikasi Dummy $it",
                    message = "Ini adalah pesan notifikasi dummy ke-$it.",
                    timestamp = Date()
                )
            }
        )
    )
    val lazyPagingItems = fakeItems.collectAsLazyPagingItems()
    EaTzyTheme {
        NotificationScreen(
            unreadableNotifications = lazyPagingItems,
            onBackClick = {}
        )
    }
}