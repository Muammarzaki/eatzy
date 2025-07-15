package com.github.eatzy.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.eatzy.domain.FoodItem
import com.github.eatzy.domain.FoodOption
import com.github.eatzy.domain.WastedFood
import com.github.eatzy.ui.component.FoodInputForm
import com.github.eatzy.ui.component.TopAppBarComponent
import com.github.eatzy.ui.component.WastedFoodInputForm
import com.github.eatzy.ui.theme.EaTzyTheme
import kotlinx.coroutines.flow.flowOf

@Composable
fun FoodFormScreen(
    onBackClicked: () -> Unit = {},
    lazyFoodItem: LazyPagingItems<FoodItem> ,
    option: FoodOption,
    onSubmitted: (FoodItem?, WastedFood?) -> Unit = { _, _ -> }
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        topBar = {
            Column(
                modifier = Modifier
                    .background(Color.Unspecified),
            ) {
                TopAppBarComponent(
                    title = "Add New $option Food",
                    onBackClick = onBackClicked,
                )
            }
        }
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            if (option == FoodOption.Stock) {
                FoodInputForm(
                    onSubmitted = {
                        onSubmitted(it, null)
                    }
                )
            }
            if (option == FoodOption.Wasted) {
                WastedFoodInputForm(
                    onSubmitted = {
                        onSubmitted(null, it)
                    },
                    foodItems = lazyFoodItem,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodFormScreenPreview() {
    EaTzyTheme {
        FoodFormScreen(
            option = FoodOption.Stock,
            lazyFoodItem = flowOf(PagingData.empty<FoodItem>()).collectAsLazyPagingItems()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WastedFormScreenPreview() {
    EaTzyTheme {
        FoodFormScreen(
            option = FoodOption.Wasted,
            lazyFoodItem = flowOf(PagingData.empty<FoodItem>()).collectAsLazyPagingItems()
        )
    }
}

