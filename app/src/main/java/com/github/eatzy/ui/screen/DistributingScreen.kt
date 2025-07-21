package com.github.eatzy.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.eatzy.R
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
import kotlinx.coroutines.flow.flowOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DistributingFormData(
    val wastedFoofId: Int,
    val recipientId: Int,
    val notes: String
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DistributingScreen(
    wastedFood: WastedFood,
    recipient: LazyPagingItems<Recipient>,
    onBackClicked: () -> Unit = {},
    onSubmitted: (DistributingFormData) -> Unit
) {
    val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    var note by remember { mutableStateOf("") }
    var lockeStatus by remember { mutableStateOf(false) }
    var lockedId by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState { recipient.itemCount }

    val isExpired = wastedFood.expirationDate.before(Date())

    Scaffold(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        topBar = {
            TopAppBarComponent(
                title = stringResource(R.string.distributing_title),
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
                modifier = Modifier.padding(5.dp),
                progress = wastedFood.difference ?: 0.0,
                valueText = wastedFood.leftoverQuantity.toString(),
                foodName = wastedFood.foodItem,
                quantityDetails = "${wastedFood.leftoverQuantity} ${wastedFood.unit}",
                typeText = wastedFood.form,
                expiryInfo = "${dateFormatter.format(wastedFood.expirationDate)}${if (isExpired) ", expired" else ""}",
                isExpired = isExpired
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (recipient.itemCount > 0) {
                HorizontalPager(
                    state = pagerState,
                    pageSpacing = 16.dp
                ) { index ->
                    recipient[index]?.let { item ->
                        DestinationDistributionCard(
                            name = item.recipientName,
                            address = item.address,
                            description = item.description,
                            onLockStatusChange = {
                                lockeStatus = !lockeStatus
                                lockedId = item.id
                            },
                            isLocked = lockeStatus
                        )
                    }
                }
            } else {
                Text(
                    text = stringResource(R.string.no_one_recipients),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    stringResource(R.string.notes_label),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                WhiteInputTextFieldWithBorder(
                    value = note,
                    onValueChange = { note = it },
                    placeholder = stringResource(R.string.notes_placeholder),
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
                enabled = lockeStatus && recipient.itemCount > 0,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onTertiaryContainer)
            ) {
                Text("Submit", fontSize = 16.sp, color = Color.DarkGray)
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
private fun DistributingScreenPreview() {
    val fakeRecipients = flowOf(
        PagingData.from(
            listOf(
                Recipient(
                    id = 1,
                    recipientName = "Community Center",
                    address = "123 Main Street",
                    contact = "081234567891",
                    description = "Provides food for local families.",
                    type = RecipientType.SOCIAL
                ),
                Recipient(
                    id = 2,
                    recipientName = "Animal Shelter",
                    address = "456 Pet Road",
                    contact = "081234567892",
                    description = "Accepts leftover food for animals.",
                    type = RecipientType.LIVESTOCK_COMPOST
                )
            )
        )
    ).collectAsLazyPagingItems()

    val dummyWastedFood = WastedFood(
        id = 101,
        foodItemId = 201,
        foodItem = "Special Fried Rice",
        leftoverInputDate = Date(System.currentTimeMillis() - 86_400_000), // yesterday
        leftoverQuantity = 50.1,
        unit = FoodUnit.PACK,
        expirationDate = Date(System.currentTimeMillis() + 2 * 86_400_000), // in 2 days
        condition = FoodCondition.DISPOSED,
        form = FoodForm.SOLID,
        status = LeftoverStatus.AVAILABLE,
        difference = 0.5
    )

    EaTzyTheme {
        DistributingScreen(
            recipient = fakeRecipients,
            wastedFood = dummyWastedFood,
            onSubmitted = {}
        )
    }
}
