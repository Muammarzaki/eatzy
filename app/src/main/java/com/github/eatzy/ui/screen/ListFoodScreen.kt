package com.github.eatzy.ui.screen

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
import com.github.eatzy.domain.FoodForm
import com.github.eatzy.domain.FoodOption
import com.github.eatzy.ui.component.AddFab
import com.github.eatzy.ui.component.FoodToggle
import com.github.eatzy.ui.component.SimpleFoodCard
import com.github.eatzy.ui.component.TopAppBarComponent
import com.github.eatzy.ui.theme.EaTzyTheme
import kotlinx.coroutines.flow.flowOf


data class FoodItemCard(
    val foodName: String,
    val date: String,
    val size: Double,
    val unit: String,
    val type: FoodForm = FoodForm.SOLID,
    val option: FoodOption
)

@Composable
fun ListFoodScreen(
    lazyItems: LazyPagingItems<FoodItemCard>,
    tabState: FoodOption = FoodOption.Stock,
    onTabChange: (FoodOption) -> Unit = { },
    onAddNewClick: (FoodOption) -> Unit,
    onNotificationClick: () -> Unit,
    bottomBar: @Composable () -> Unit = {},
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        topBar = {
            TopAppBarComponent(onNotificationClick = onNotificationClick)
        },
        floatingActionButton = {
            AddFab {
                onAddNewClick(tabState)
            }
        },
        bottomBar = bottomBar
    ) { contentPadding ->
        Column(
            Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
        ) {
            FoodToggle {
                onTabChange(it)
            }
            LazyColumn(
                modifier = Modifier.padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(lazyItems.itemCount) { index ->
                    val item = lazyItems[index]
                    item?.let {
                        SimpleFoodCard(
                            foodName = it.foodName,
                            date = it.date,
                            size = it.size,
                            unit = it.unit,
                            type = it.type,
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
    var tabState by remember { mutableStateOf(FoodOption.Stock) }
    val fakeItems = flowOf(
        PagingData.from(
            listOf(
                FoodItemCard("Apple", "2023-10-27", 1.0, "piece", FoodForm.SOLID, FoodOption.Stock),
                FoodItemCard("Milk", "2023-10-28", 1.0, "liter", FoodForm.LIQUID, FoodOption.Stock)
            )
        )
    )
    val lazyPagingItems = fakeItems.collectAsLazyPagingItems()
    EaTzyTheme {
        ListFoodScreen(
            tabState = tabState,
            lazyItems = lazyPagingItems,
            onAddNewClick = {},
            onNotificationClick = {},
            onTabChange = {
                tabState = it
            }
        )
    }
}
