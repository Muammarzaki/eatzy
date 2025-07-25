package com.github.eatzy.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.eatzy.domain.DistributionOption
import com.github.eatzy.ui.component.DistributionInfoCard
import com.github.eatzy.ui.component.DistributionToggle
import com.github.eatzy.ui.component.TopAppBarComponent
import com.github.eatzy.ui.theme.EaTzyTheme
import kotlinx.coroutines.flow.flowOf

data class DistributionItem(
    val id: Int,
    val value: Double,
    val progress: Double,
    val foodName: String,
    val typeText: String,
)

@Composable
fun ListDistributionScreen(
    onTabChange: (DistributionOption) -> Unit,
    onCardClick: (Int) -> Unit,
    lazyItems: LazyPagingItems<DistributionItem>,
    onNotificationClick: () -> Unit,
    bottomBar: @Composable () -> Unit = {}
) {
    var distributionOptionState by remember { mutableStateOf(DistributionOption.SEND) }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        topBar = {
            TopAppBarComponent(onNotificationClick = onNotificationClick)
        },
        bottomBar = bottomBar
    ) { contentPadding ->
        Column(
            Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
        ) {
            DistributionToggle {
                distributionOptionState = it
                onTabChange(it)
            }
            LazyColumn(
                modifier = Modifier.padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(lazyItems.itemCount) { index ->
                    lazyItems[index]?.let {
                        DistributionInfoCard(
                            modifier = Modifier.clickable {
                                if (distributionOptionState == DistributionOption.SEND) {
                                    onCardClick(
                                        it.id
                                    )
                                }
                            },
                            value = it.value,
                            progress = it.progress,
                            foodName = it.foodName,
                            typeText = it.typeText,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ListFoodScreenPreview() {
    val fakeItems = flowOf(
        PagingData.from(
            listOf(
                DistributionItem(0, 7550.0, 0.8, "Apple Pie", "Available For Distribution"),
                DistributionItem(1, 5000.0, 0.5, "Box of Milk", "Available For Distribution")
            )
        )
    ).collectAsLazyPagingItems()

    EaTzyTheme {
        ListDistributionScreen(
            lazyItems = fakeItems,
            onNotificationClick = {},
            onTabChange = { },
            onCardClick = {}
        )
    }
}