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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.eatzy.domain.FoodForm
import com.github.eatzy.domain.FoodOption
import com.github.eatzy.domain.FoodUnit
import com.github.eatzy.ui.component.AddFab
import com.github.eatzy.ui.component.FoodToggle
import com.github.eatzy.ui.component.SimpleFoodCard
import com.github.eatzy.ui.component.TopAppBarComponent
import com.github.eatzy.ui.theme.EaTzyTheme
import kotlinx.coroutines.flow.flowOf


data class FoodItemCard(
    val id: Int,
    val foodName: String,
    val date: String,
    val size: Double,
    val unit: FoodUnit,
    val type: FoodForm = FoodForm.SOLID,
    val option: FoodOption
)

@Composable
fun ListFoodScreen(
    lazyItems: LazyPagingItems<FoodItemCard>,
    tabState: FoodOption = FoodOption.STOCK,
    onTabChange: (FoodOption) -> Unit = { },
    onAddNewClicked: (FoodOption) -> Unit,
    onCardClicked: (Int) -> Unit = {},
    onNotificationClick: () -> Unit,
    bottomBar: @Composable () -> Unit = {},
) {
    val context = LocalContext.current
    Scaffold(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        topBar = {
            TopAppBarComponent(onNotificationClick = onNotificationClick)
        },
        floatingActionButton = {
            AddFab {
                onAddNewClicked(tabState)
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(lazyItems.itemCount) { index ->
                    val item = lazyItems[index]
                    item?.let {
                        SimpleFoodCard(
                            modifier = Modifier.clickable {
                                onCardClicked(it.id)
                            },
                            foodName = it.foodName,
                            date = it.date,
                            size = it.size,
                            unit = it.unit.getLabel(context),
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
    var tabState by remember { mutableStateOf(FoodOption.STOCK) }
    val fakeItems = flowOf(
        PagingData.from(
            listOf(
                FoodItemCard(
                    0,
                    "Apple",
                    "2023-10-27",
                    1.0,
                    FoodUnit.PIECE,
                    FoodForm.SOLID,
                    FoodOption.STOCK
                ),
                FoodItemCard(
                    1,
                    "Milk",
                    "2023-10-28",
                    1.0,
                    FoodUnit.LITER,
                    FoodForm.LIQUID,
                    FoodOption.STOCK
                )
            )
        )
    ).collectAsLazyPagingItems()
    EaTzyTheme {
        ListFoodScreen(
            tabState = tabState,
            lazyItems = fakeItems,
            onAddNewClicked = {},
            onNotificationClick = {},
            onTabChange = {
                tabState = it
            }
        )
    }
}
