package com.github.eatzy.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.eatzy.domain.DistributionOption
import com.github.eatzy.ui.component.DistributionInfoCard
import com.github.eatzy.ui.component.DistributionToggle
import com.github.eatzy.ui.component.SimpleNotificationCard
import com.github.eatzy.ui.component.TopAppBarComponent
import com.github.eatzy.ui.theme.EaTzyTheme

@Composable
fun NotificationScreen(onBackClick: () -> Unit) {
    var tabState by remember { mutableStateOf(DistributionOption.Send) }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        topBar = {
            TopAppBarComponent(onBackClick = onBackClick, title = "Notifikasi")
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
                items(20) {
                    SimpleNotificationCard(
                        subject = "Panti asuhan bersedia menerima makanan teralokasikan",
                        message = "Note jenis, Nasi Goreng. Ini adalah contoh pesan yang lebih panjang. Semoga bisa membantu Anda. Terima kasih atas perhatiannya."
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun NotificationScreenPreview() {
    EaTzyTheme {
        NotificationScreen(
            onBackClick = {}
        )
    }
}