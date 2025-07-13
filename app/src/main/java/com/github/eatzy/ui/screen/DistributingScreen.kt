package com.github.eatzy.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.eatzy.domain.Recipient
import com.github.eatzy.domain.RecipientType
import com.github.eatzy.ui.component.DestinationDistributionCard
import com.github.eatzy.ui.component.FoodInfoCard
import com.github.eatzy.ui.component.TopAppBarComponent
import com.github.eatzy.ui.component.WhiteInputTextFieldWithBorder
import com.github.eatzy.ui.theme.EaTzyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistributingScreen(recipient: List<Recipient> = emptyList()) {
    var note by remember { mutableStateOf("") }
    var lockeStatus by remember { mutableStateOf(false) }
    var lockedId by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(
        initialPage = Int.MAX_VALUE / 2,
        pageCount = { Int.MAX_VALUE }
    )
    Scaffold(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        topBar = {
            TopAppBarComponent(
                title = "Distributing",
                onBackClick = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            FoodInfoCard(
                Modifier.border(
                    width = 10.dp,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    shape = RoundedCornerShape(16.dp)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalPager(state = pagerState) { virtualPage ->
                val realPageIndex = (virtualPage - (Int.MAX_VALUE / 2)).mod(recipient.size)
                val item = recipient[realPageIndex]
                DestinationDistributionCard(
                    name = item.recipientName,
                    address = item.address,
                    description = item.description,
                    onLockStatusChange = {
                        lockeStatus = !lockeStatus
                        lockedId = item.id
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Catatan",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                WhiteInputTextFieldWithBorder(
                    value = note,
                    onValueChange = { note = it },
                    placeholder = "Masukkan catatan",
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { /* TODO: Aksi submit */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onTertiaryContainer)
            ) {
                Text("Submit", fontSize = 16.sp, color = Color.DarkGray)
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun DistributingScreenPreview() {
    val dummyRecipients = listOf(
        Recipient(
            id = 1,
            recipientName = "Penerima 1",
            address = "Alamat Penerima 1",
            contact = "081234567890",
            description = "Deskripsi untuk penerima 1 yang cukup panjang.",
            type = RecipientType.SOCIAL
        ),
        Recipient(
            id = 2,
            recipientName = "Penerima 2",
            address = "Alamat Penerima 2",
            contact = "081234567891",
            description = "Deskripsi untuk penerima 2.",
            type = RecipientType.LIVESTOCK_COMPOST
        ),
        Recipient(
            id = 3,
            recipientName = "Penerima 3",
            address = "Alamat Penerima 3",
            contact = "081234567892",
            description = "Deskripsi untuk penerima 3. Ini adalah deskripsi yang lebih panjang untuk memastikan tampilannya benar.",
            type = RecipientType.SOCIAL
        ),
        Recipient(
            id = 4,
            recipientName = "Penerima 4",
            address = "Alamat Penerima 4",
            contact = "081234567893",
            description = "Deskripsi singkat.",
            type = RecipientType.LIVESTOCK_COMPOST
        ),
        Recipient(
            id = 5,
            recipientName = "Penerima 5",
            address = "Alamat Penerima 5",
            contact = "081234567894",
            description = "Deskripsi terakhir untuk penerima kelima.",
            type = RecipientType.SOCIAL
        )
    )
    EaTzyTheme {
        DistributingScreen(recipient = dummyRecipients)
    }
}
