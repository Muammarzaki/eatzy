package com.github.eatzy.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.eatzy.domain.FoodUnit
import com.github.eatzy.domain.FoodWasteChartData
import com.github.eatzy.domain.WastedFood
import com.github.eatzy.domain.WastedFoodTrend
import com.github.eatzy.ui.component.ChipFilterDropdown
import com.github.eatzy.ui.component.EnumSelector
import com.github.eatzy.ui.component.SalesRevenueChart
import com.github.eatzy.ui.component.SummaryPieChart
import com.github.eatzy.ui.component.TopAppBarComponent
import com.github.eatzy.ui.component.WeightIndicatorCard
import com.github.eatzy.ui.theme.EaTzyTheme
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    chartData: FoodWasteChartData? = null,
    lazyItems: LazyPagingItems<WastedFood>,
    trendData: List<WastedFoodTrend>,
    bottomBar: @Composable () -> Unit = {},
    onTagSelected: (FoodUnit) -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    var selected by remember { mutableStateOf(FoodUnit.KILOGRAM) }
    val pagerState = rememberPagerState(pageCount = { 2 })

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = bottomBar,
        topBar = {
            TopAppBarComponent(
                modifier = Modifier.offset(y = 16.dp),
                onNotificationClick = onNotificationClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            ChipFilterDropdown(
                modifier = Modifier.offset(y = 6.dp),
                categoryItems = listOf("Today", "Tomorrow", "This Week", "This Month"),
                onItemSelected = {}
            )
            val cardElevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            )
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 14f),
                pageSpacing = 10.dp
            ) { page ->
                when (page) {
                    0 -> {
                        SummaryPieChart(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .aspectRatio(16 / 14f),
                            remaining = chartData?.remaining ?: 0f,
                            mitigated = chartData?.distributed ?: 0f,
                            lost = chartData?.wasted ?: 0f,
                            elevation = cardElevation,
                            containerColor = Color.White
                        )
                    }

                    1 -> {
                        SalesRevenueChart(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .aspectRatio(16 / 14f),
                            salesData = trendData.map { it.wastedTotal },
                            months = trendData.map { it.month },
                            elevation = cardElevation,
                            containerColor = Color.White
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            val context = LocalContext.current
            LazyRow {
                item {
                    EnumSelector(
                        options = FoodUnit.entries.toTypedArray(),
                        selectedOption = selected,
                        onOptionSelected = {
                            selected = it
                            onTagSelected(it)
                        },
                        labelProvider = { it.getLabel(context) }
                    )
                }
            }
            LazyColumn(
                contentPadding = PaddingValues(vertical = 5.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(lazyItems.itemCount) { data ->
                    lazyItems[data]?.let {
                        WeightIndicatorCard(
                            weight = it.leftoverQuantity.toFloat(),
                            progress = it.difference?.toFloat() ?: 0f,
                            foodStatus = it.foodItem,
                            unit = selected
                        )
                    }
                }
            }
        }
    }
}

@Preview(device = Devices.PIXEL_4, showBackground = true, showSystemUi = true)
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
            lazyItems = lazyPagingItems,
            trendData = emptyList()
        )
    }
}
