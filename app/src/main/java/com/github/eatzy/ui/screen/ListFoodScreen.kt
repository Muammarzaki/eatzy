package com.github.eatzy.ui.screen

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
import com.github.eatzy.domain.FoodForm
import com.github.eatzy.domain.FoodOption
import com.github.eatzy.ui.component.AddFab
import com.github.eatzy.ui.component.FoodToggle
import com.github.eatzy.ui.component.SimpleFoodCard
import com.github.eatzy.ui.component.TopAppBarComponent
import com.github.eatzy.ui.theme.EaTzyTheme

@Composable
fun ListFoodScreen(onAddNewClick: (FoodOption) -> Unit, onNotificationClick: () -> Unit) {
    var tabState by remember { mutableStateOf(FoodOption.Stock) }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        topBar = {
            TopAppBarComponent(onNotificationClick = onNotificationClick)
        },
        floatingActionButton = {
            AddFab {
                onAddNewClick(tabState)
            }
        }
    ) { contentPadding ->
        Column(
            Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
        ) {
            FoodToggle {
                tabState = it
            }
            LazyColumn {
                items(20) {
                    if (tabState == FoodOption.Stock) {
                        SimpleFoodCard(
                            foodName = "Susu UHT",
                            date = "15 January 2024",
                            size = 1,
                            unit = "liter",
                            type = FoodForm.LIQUID
                        )
                    }
                    if (tabState == FoodOption.Wasted) {
                        SimpleFoodCard(
                            foodName = "Susu UHT",
                            date = "15 January 2024",
                            size = 1,
                            unit = "liter",
                            type = FoodForm.SOLID
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
    EaTzyTheme {
        ListFoodScreen(
            onAddNewClick = {},
            onNotificationClick = {}
        )
    }
}