package com.github.eatzy.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.eatzy.MainViewModel
import com.github.eatzy.domain.Business
import com.github.eatzy.domain.DistributionOption
import com.github.eatzy.domain.FoodOption
import com.github.eatzy.domain.User
import com.github.eatzy.ui.screen.DistributingScreen
import com.github.eatzy.ui.screen.FoodFormScreen
import com.github.eatzy.ui.screen.HomeScreen
import com.github.eatzy.ui.screen.ListDistributionScreen
import com.github.eatzy.ui.screen.ListFoodScreen
import com.github.eatzy.ui.screen.LoginScreen
import com.github.eatzy.ui.screen.NotificationScreen
import com.github.eatzy.ui.screen.ProfileScreen
import com.github.eatzy.ui.screen.SplashScreen


@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    bottomBar: @Composable () -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = Route.SplashScreen.path,
        modifier = modifier
    ) {
        composable(Route.SplashScreen.path) {
            SplashScreen(
                onGetStartedClick = {
                    navController.navigate(Route.LoginScreen.path)
                }
            )
        }
        composable(Route.LoginScreen.path) {
            LoginScreen(
                onLoginClicked = { username, password ->
                    viewModel.loginUser(username, password) {
                        navController.navigate(Route.HomeScreen.path)
                    }
                },
                onRegisterClicked = { (ownerName, businessName, address, phoneNumber, email, password) ->
                    viewModel.registerUser(
                        user = User(
                            name = ownerName,
                            email = email,
                            phoneNumber = phoneNumber,
                            password = password,
                            business = Business(
                                businessName = businessName,
                                address = address,
                            ),
                        ),
                        onRegistrationComplete = {
                            navController.navigate(Route.HomeScreen.path)
                        }
                    )
                }
            )
        }
        composable(Route.HomeScreen.path) {
            HomeScreen(
                bottomBar = bottomBar
            )
        }
        composable(Route.FoodListScreen.path) {
            var tabState by remember { mutableStateOf(FoodOption.Stock) }
            val lazyFood =
                if (tabState == FoodOption.Stock) viewModel.foodItemsCard.collectAsLazyPagingItems()
                else viewModel.wastedFoods.collectAsLazyPagingItems()
            ListFoodScreen(
                onNotificationClick = {
                    navController.navigate(Route.NotificationScreen.path)
                },
                onAddNewClick = {
                    navController.navigate(Route.FormScreen.createRoute(it))
                },
                bottomBar = bottomBar,
                tabState = tabState,
                onTabChange = {
                    tabState = it
                },
                lazyItems = lazyFood
            )
        }
        composable(Route.DistributionScreen.path) {
            var tabState by remember { mutableStateOf(DistributionOption.Send) }
            val lazyDistributionEntry =
                if (tabState == DistributionOption.Send) viewModel.unDistributedWastedFood.collectAsLazyPagingItems() else viewModel.distributedWastedFood.collectAsLazyPagingItems()
            ListDistributionScreen(
                onNotificationClick = {
                    navController.navigate(Route.NotificationScreen.path)
                },
                bottomBar = bottomBar,
                onTabChange = {
                    tabState = it
                },
                lazyItems = lazyDistributionEntry,
                onCardClick = {
                    navController.navigate(Route.DistributingScreen.createRoute(it))
                }
            )
        }
        composable(Route.ProfileScreen.path) {
            ProfileScreen(
                bottomBar = bottomBar
            )
        }
        composable(
            Route.FormScreen.path,
            arguments = Route.FormScreen.ARG
        ) { backStackEntry ->
            val optionString = backStackEntry.arguments?.getString("option") ?: return@composable
            val option = FoodOption.valueOf(optionString)

            FoodFormScreen(
                onBackClicked = {
                    navController.popBackStack()
                },
                option = option,
                onSubmitted = { foodStock, foodWasted ->
                    foodStock?.let {
                        viewModel.saveFoodItemStock(foodStock).let {
                            navController.popBackStack()
                        }
                    }
                    foodWasted?.let {
                        viewModel.saveWastedFood(foodWasted).let {
                            navController.popBackStack()
                        }
                    }
                },
                lazyFoodItem = viewModel.foodItems.collectAsLazyPagingItems()
            )
        }
        composable(
            Route.DistributingScreen.path,
            arguments = Route.DistributingScreen.ARG
        ) { backStackEntry ->
            val distributionId =
                backStackEntry.arguments?.getInt("distributionId") ?: return@composable
            val distributionDetails by
            viewModel.findWastedFoodById(distributionId).collectAsState(null)

            if (distributionDetails == null) return@composable

            DistributingScreen(
                onBackClicked = {
                    navController.popBackStack()
                },
                wastedFood = distributionDetails!!,
                onSubmitted = {
                    navController.popBackStack()
                }
            )
        }
        composable(Route.NotificationScreen.path) {
            NotificationScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
