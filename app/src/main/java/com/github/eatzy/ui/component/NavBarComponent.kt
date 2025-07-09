package com.github.eatzy.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.eatzy.ui.theme.EaTzyTheme

data class BottomNavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

@Composable
fun NavbarComponent(
    modifier: Modifier = Modifier,
    onItemSelected: (Int) -> Unit = {}
) {
    val items = listOf(
        BottomNavItem(
            label = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
        ),
        BottomNavItem(
            label = "Wallet",
            selectedIcon = Icons.Filled.AccountBalanceWallet,
            unselectedIcon = Icons.Outlined.AccountBalanceWallet,
        ),
        BottomNavItem(
            label = "Stats",
            selectedIcon = Icons.Filled.PieChart,
            unselectedIcon = Icons.Outlined.PieChart,
        ),
        BottomNavItem(
            label = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
        ),
    )

    var selectedItemIndex by remember { mutableIntStateOf(0) }
    NavigationBar(
        modifier = modifier,
        containerColor = Color.White,
    ) {

        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index
                    onItemSelected(index)
                },
                label = {
                    Text(
                        text = item.label,
                        fontWeight = if (selectedItemIndex == index) FontWeight.Bold else FontWeight.Normal
                    )
                },
                alwaysShowLabel = true,
                icon = {
                    Icon(
                        imageVector = if (selectedItemIndex == index) {
                            item.selectedIcon
                        } else {
                            item.unselectedIcon
                        },
                        contentDescription = item.label,
                        tint = if (selectedItemIndex == index) Color.White else Color(0xFF66BB6A) // Darker Green for unselected
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    indicatorColor = Color(0xFF66BB6A), // Darker Green for indicator
                    unselectedIconColor = Color(0xFF66BB6A), // Darker Green for unselected icon
                    unselectedTextColor = Color.Gray
                ),
                modifier = Modifier.padding(horizontal = 4.dp) // Add some padding between items
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun NavbarComponentPreview() {
    EaTzyTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
                    .padding(bottom = 56.dp)
            ) {}
            NavbarComponent(modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}