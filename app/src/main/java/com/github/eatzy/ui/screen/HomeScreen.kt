package com.github.eatzy.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.eatzy.ui.component.CalorieTrackerChart
import com.github.eatzy.ui.component.ChipFilterDropdown
import com.github.eatzy.ui.component.WeightIndicatorCard
import com.github.eatzy.ui.theme.EaTzyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(bottomBar: @Composable () -> Unit = {}) {
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
                wasted = 1500f,
                mitigated = 300f,
                score = 30f
            )
            Spacer(Modifier.height(8.dp))
            ChipFilterDropdown(
                modifier = Modifier.offset(x = (-16).dp),
                categoryItems = listOf("Type"),
                onItemSelected = {}
            )
            LazyColumn(
                contentPadding = PaddingValues(vertical = 5.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(20) {
                    WeightIndicatorCard(
                        weight = 70,
                        progress = 0.85f,
                        bmiStatus = "You have a healthy BMI"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    EaTzyTheme {
        HomeScreen()
    }
}

