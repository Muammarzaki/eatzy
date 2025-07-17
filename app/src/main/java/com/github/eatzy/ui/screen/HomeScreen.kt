package com.github.eatzy.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.github.eatzy.domain.FoodUnit
import com.github.eatzy.domain.FoodWasteChartData
import com.github.eatzy.domain.WastedFood
import com.github.eatzy.ui.component.CalorieTrackerChart
import com.github.eatzy.ui.component.ChipFilterDropdown
import com.github.eatzy.ui.component.EnumSelector
import com.github.eatzy.ui.component.WeightIndicatorCard
import com.github.eatzy.ui.theme.EaTzyTheme
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    chartData: FoodWasteChartData? = null,
    lazyItems: LazyPagingItems<WastedFood>,
    bottomBar: @Composable () -> Unit = {},
    onTagSelected: (FoodUnit) -> Unit = {}
) {
    var selected by remember { mutableStateOf(FoodUnit.KILOGRAM) }
    Scaffold(
        bottomBar = bottomBar
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            ChipFilterDropdown(
                modifier = Modifier.offset(x = (-16).dp),
                categoryItems = listOf("Today", "Tomorrow", "This Week", "This Month"),
                onItemSelected = {}
            )
            CalorieTrackerChart(
                remaining = chartData?.remaining ?: 0f,
                mitigated = chartData?.distributed ?: 0f,
                lost = chartData?.wasted ?: 0f,
                modifier = Modifier.aspectRatio(1f)
            )
            Spacer(Modifier.height(8.dp))

            EnumSelector(
                options = FoodUnit.entries.toTypedArray(),
                selectedOption = selected,
                onOptionSelected = {
                    selected = it
                    onTagSelected(it)
                },
                labelProvider = { it.label }
            )
            LazyColumn(
                contentPadding = PaddingValues(vertical = 5.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(lazyItems.itemCount) { data ->
                    lazyItems[data]?.let {
                        WeightIndicatorCard(
                            weight = it.leftoverQuantity.toFloat(),
                            progress = it.difference?.toFloat() ?: 0f,
                            bmiStatus = it.foodItem
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val fakeItems = flowOf(
        PagingData.from(
            listOf<WastedFood>(

            )
        )
    )
    val lazyPagingItems = fakeItems.collectAsLazyPagingItems()
    EaTzyTheme {
        HomeScreen(
            lazyItems = lazyPagingItems
        )
    }
}
