package com.github.eatzy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.rememberNavController
import com.github.eatzy.ui.component.BottomNavItem
import com.github.eatzy.ui.component.BottomNavigationBar
import com.github.eatzy.ui.navigation.MainNavHost
import com.github.eatzy.ui.navigation.Route
import com.github.eatzy.ui.theme.EaTzyTheme


class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels { MainViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var selectedItemIndex by remember { mutableIntStateOf(0) }
            EaTzyTheme {
                val items = listOf(
                    BottomNavItem(
                        route = Route.HomeScreen,
                        label = "Home",
                        selectedIcon = painterResource(R.drawable.ic_filled_home),
                        unselectedIcon = painterResource(R.drawable.ic_outlined_home),
                    ),
                    BottomNavItem(
                        route = Route.FoodListScreen,
                        label = "Food",
                        selectedIcon = painterResource(R.drawable.ic_filled_document),
                        unselectedIcon = painterResource(R.drawable.ic_outlined_document),
                    ),
                    BottomNavItem(
                        route = Route.DistributionScreen,
                        label = "Distribution",
                        selectedIcon = painterResource(R.drawable.ic_filled_pie),
                        unselectedIcon = painterResource(R.drawable.ic_outlined_pie),
                    ),
                    BottomNavItem(
                        route = Route.ProfileScreen,
                        label = "Profile",
                        selectedIcon = painterResource(R.drawable.ic_filled_people),
                        unselectedIcon = painterResource(R.drawable.ic_outlined_people),
                    ),
                )
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
