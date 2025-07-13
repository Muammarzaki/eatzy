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
import com.github.eatzy.domain.FoodOption
import com.github.eatzy.ui.component.FoodInputForm
import com.github.eatzy.ui.component.TopAppBarComponent
import com.github.eatzy.ui.component.WastedFoodInputForm
import com.github.eatzy.ui.theme.EaTzyTheme

@Composable
fun FoodFormScreen(onBackClicked: () -> Unit = {}, option: FoodOption) {
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

                    }
                )
            }
            if (option == FoodOption.Wasted) {
                WastedFoodInputForm(
                    onSubmitted = {

                    }
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
            option = FoodOption.Stock
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WastedFormScreenPreview() {
    EaTzyTheme {
        FoodFormScreen(
            option = FoodOption.Wasted
        )
    }
}

