package com.github.eatzy.ui.screen

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
import com.github.eatzy.domain.FoodCondition
import com.github.eatzy.domain.FoodForm
import com.github.eatzy.domain.FoodUnit
import com.github.eatzy.domain.LeftoverStatus
import com.github.eatzy.domain.Recipient
import com.github.eatzy.domain.RecipientType
import com.github.eatzy.domain.WastedFood
import com.github.eatzy.ui.component.DestinationDistributionCard
import com.github.eatzy.ui.component.FoodInfoCard
import com.github.eatzy.ui.component.TopAppBarComponent
import com.github.eatzy.ui.component.WhiteInputTextFieldWithBorder
import com.github.eatzy.ui.theme.EaTzyTheme
import java.util.Date

data class DistributingFormData(
    val wastedFoofId: Int,
    val recipientId: Int,
    val notes: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistributingScreen(
    wastedFood: WastedFood,
    recipient: List<Recipient> = emptyList(),
    onBackClicked: (() -> Unit) = {},
    onSubmitted: (DistributingFormData) -> Unit
) {
    var note by remember { mutableStateOf("") }
    var lockeStatus by remember { mutableStateOf(false) }
    var lockedId by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(
        initialPage = Int.MAX_VALUE / 2,
        pageCount = { Int.MAX_VALUE }
    )

    val isExpired = wastedFood.expirationDate.before(Date())

    Scaffold(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        topBar = {
            TopAppBarComponent(
                title = "Distributing",
                onBackClick = onBackClicked
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
                modifier = Modifier.padding(16.dp),
                progress = wastedFood.difference ?: 0.0,
                valueText = wastedFood.leftoverQuantity.toString(),
                foodName = wastedFood.foodItem,
                quantityDetails = "${wastedFood.leftoverQuantity} ${wastedFood.unit}",
                typeText = wastedFood.form,
                expiryInfo = "${wastedFood.expirationDate}${if (isExpired) ", expired" else ""}",
                isExpired = isExpired
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (recipient.isNotEmpty()) {
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
            } else {
                Text(
                    text = "Tidak ada penerima yang tersedia",
                    style = MaterialTheme.typography.bodyLarge
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
                onClick = {
                    onSubmitted(
                        DistributingFormData(
                            wastedFoofId = wastedFood.id ?: 0,
                            recipientId = lockedId,
                            notes = note
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = lockeStatus && recipient.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onTertiaryContainer)
            ) {
                Text("Submit", fontSize = 16.sp, color = Color.DarkGray)
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun DistributingScreenEmptyRecipientPreview() {
    EaTzyTheme {
        DistributingScreen(
            recipient = emptyList(),
            wastedFood = WastedFood(
                id = 101,
                foodItemId = 201,
                foodItem = "Nasi Goreng Spesial",
                leftoverInputDate = Date(System.currentTimeMillis() - 86400000),
                leftoverQuantity = 5.0,
                unit = FoodUnit.PACK,
                expirationDate = Date(System.currentTimeMillis() + 86400000 * 2),
                condition = FoodCondition.DISPOSED,
                form = FoodForm.SOLID,
                status = LeftoverStatus.AVAILABLE,
                difference = 0.5
            ),
            onSubmitted = {}
        )
    }
}


@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun DistributingScreenPreview() {
    val dummyRecipients = (1..5).map {
        Recipient(
            id = it,
            recipientName = "Penerima $it",
            address = "Alamat Penerima $it",
            contact = "08123456789$it",
            description = when (it) {
                1 -> "Deskripsi untuk penerima 1 yang cukup panjang."
                3 -> "Deskripsi untuk penerima 3. Ini adalah deskripsi yang lebih panjang untuk memastikan tampilannya benar."
                else -> "Deskripsi untuk penerima $it."
            },
            type = if (it % 2 == 0) RecipientType.LIVESTOCK_COMPOST else RecipientType.SOCIAL
        )
    }
    EaTzyTheme {
        DistributingScreen(
            recipient = dummyRecipients,
            wastedFood = WastedFood(
                id = 101,
                foodItemId = 201,
                foodItem = "Nasi Goreng Spesial",
                leftoverInputDate = Date(System.currentTimeMillis() - 86400000),
                leftoverQuantity = 5.0,
                unit = FoodUnit.PACK,
                expirationDate = Date(System.currentTimeMillis() + 86400000 * 2),
                condition = FoodCondition.DISPOSED,
                form = FoodForm.SOLID,
                status = LeftoverStatus.AVAILABLE,
                difference = 0.5
            ),
            onSubmitted = {}
        )
    }
}
