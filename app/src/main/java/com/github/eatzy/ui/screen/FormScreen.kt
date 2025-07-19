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
import com.github.eatzy.domain.FoodCondition
import com.github.eatzy.domain.FoodForm
import com.github.eatzy.domain.FoodItem
import com.github.eatzy.domain.FoodOption
import com.github.eatzy.domain.FoodUnit
import com.github.eatzy.domain.WastedFood
import com.github.eatzy.ui.component.FoodInputForm
import com.github.eatzy.ui.component.TopAppBarComponent
import com.github.eatzy.ui.component.WastedFoodInputForm
import com.github.eatzy.ui.theme.EaTzyTheme
import kotlinx.coroutines.flow.flowOf
import java.util.Calendar

@Composable
fun FoodFormScreen(
    initialFoodItem: FoodItem? = null,
    initialWastedFood: WastedFood? = null,
    onBackClicked: () -> Unit = {},
    lazyFoodItem: LazyPagingItems<FoodItem>,
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
                    title = "Add New ${if (option == FoodOption.STOCK) "Stock" else "Wasted"} Food",
                    onBackClick = onBackClicked,
                )
            }
        }
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            if (option == FoodOption.STOCK) {
                FoodInputForm(
                    onSubmitted = {
                        onSubmitted(it, null)
                    },
                    initialData = initialFoodItem
                )
            }
            if (option == FoodOption.Wasted) {
                WastedFoodInputForm(
                    onSubmitted = {
                        onSubmitted(null, it)
                    },
                    initialData = initialWastedFood,
                    foodItems = lazyFoodItem,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UpdateFoodFormScreenPreview() {
    EaTzyTheme {
        FoodFormScreen(
            option = FoodOption.STOCK,
            lazyFoodItem = flowOf(PagingData.empty<FoodItem>()).collectAsLazyPagingItems(),
            initialFoodItem = FoodItem(
                id = 1,
                foodName = "Fries Rice",
                foodType = "Main Course",
                expirationDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 5) }.time,
                initialQuantity = 2.0,
                unit = FoodUnit.PORTION
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UpdateWastedFormScreenPreview() {
    EaTzyTheme {
        FoodFormScreen(
            option = FoodOption.Wasted,
            lazyFoodItem = flowOf(PagingData.empty<FoodItem>()).collectAsLazyPagingItems(),
            initialWastedFood = WastedFood(
                foodItemId = 1,
                leftoverQuantity = 2.0,
                unit = FoodUnit.PORTION,
                condition = FoodCondition.EDIBLE,
                expirationDate = Calendar
                    .getInstance()
                    .apply {
                        add(Calendar.DAY_OF_YEAR, 2)
                    }.time,
                form = FoodForm.SOLID,
                foodItem = "Fries Rice"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FoodFormScreenPreview() {
    EaTzyTheme {
        FoodFormScreen(
            option = FoodOption.STOCK,
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

