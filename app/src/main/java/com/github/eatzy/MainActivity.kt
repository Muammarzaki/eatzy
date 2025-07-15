package com.github.eatzy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.github.eatzy.ui.component.BottomNavItem
import com.github.eatzy.ui.component.BottomNavigationBar
import com.github.eatzy.ui.navigation.MainNavHost
import com.github.eatzy.ui.navigation.Route
import com.github.eatzy.ui.theme.EaTzyTheme

val items = listOf(
    BottomNavItem(
        route = Route.HomeScreen,
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    BottomNavItem(
        route = Route.FoodListScreen,
        label = "Food",
        selectedIcon = Icons.Filled.AccountBalanceWallet,
        unselectedIcon = Icons.Outlined.AccountBalanceWallet,
    ),
    BottomNavItem(
        route = Route.DistributionScreen,
        label = "Distribution",
        selectedIcon = Icons.Filled.PieChart,
        unselectedIcon = Icons.Outlined.PieChart,
    ),
    BottomNavItem(
        route = Route.ProfileScreen,
        label = "Profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
    ),
)

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels { MainViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var selectedItemIndex by remember { mutableIntStateOf(0) }
            EaTzyTheme {
                MainNavHost(
                    navController = navController,
                    viewModel = mainViewModel,
                    bottomBar = {
                        BottomNavigationBar(
                            items = items,
                            selectedItemIndex = selectedItemIndex,
                            onItemSelected = { index, route ->
                                selectedItemIndex = index
                                navController.navigate(route.path)
                            },
                        )
                    }
                )
            }
        }
    }
}
