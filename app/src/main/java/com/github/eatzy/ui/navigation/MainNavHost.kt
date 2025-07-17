package com.github.eatzy.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.github.eatzy.domain.FoodItem
import com.github.eatzy.domain.FoodOption
import com.github.eatzy.domain.FoodUnit
import com.github.eatzy.domain.User
import com.github.eatzy.domain.WastedFood
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
            val wastedFood = viewModel.wastedFoodsByUnit.collectAsLazyPagingItems()
            val chartData = viewModel.chartData.collectAsState()
            LaunchedEffect(Unit) {
                viewModel.selectUnit(FoodUnit.KILOGRAM)
            }
            HomeScreen(
                chartData = chartData.value,
                lazyItems = wastedFood,
                bottomBar = bottomBar,
                onTagSelected = {
                    viewModel.selectUnit(it)
                }
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
                onAddNewClicked = {
                    navController.navigate(Route.FormScreen.createRoute(it))
                },
                bottomBar = bottomBar,
                tabState = tabState,
                onTabChange = {
                    tabState = it
                },
                lazyItems = lazyFood,
                onCardClicked = { id ->
                    navController.navigate(Route.FormScreen.createRoute(tabState, id))
                }
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
            val currentBusiness by viewModel.currentUser.collectAsState(null)
            ProfileScreen(
                bottomBar = bottomBar,
                user = currentBusiness ?: return@composable
            )
        }
        composable(
            Route.FormScreen.path,
            arguments = Route.FormScreen.ARGUMENTS
        ) { backStackEntry ->
            val optionArg = backStackEntry.arguments?.getString(Route.FormScreen.ARG_OPTION)
                ?: return@composable
            val foodIdArg = backStackEntry.arguments?.getInt(Route.FormScreen.ARG_FOOD_ID) ?: -1

            val option = FoodOption.valueOf(optionArg)
            var wastedFood by remember { mutableStateOf<WastedFood?>(null) }
            var initialFoodItem by remember { mutableStateOf<FoodItem?>(null) }

            LaunchedEffect(foodIdArg) {
                foodIdArg.let {
                    when (option) {
                        FoodOption.Wasted -> {
                            wastedFood = viewModel.findWastedFoodById(it)
                        }

                        FoodOption.Stock -> {
                            initialFoodItem = viewModel.findFoodItemById(it)
                        }
                    }
                }
            }
            if (option == FoodOption.Stock && foodIdArg > -1 && initialFoodItem == null) {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            } else if (option == FoodOption.Wasted && foodIdArg > -1 && wastedFood == null) {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            } else {
                FoodFormScreen(
                    initialFoodItem = initialFoodItem,
                    initialWastedFood = wastedFood,
                    onBackClicked = {
                        navController.popBackStack()
                    },
                    option = option,
                    onSubmitted = { foodStock, foodWasted ->
                        when (option) {
                            FoodOption.Stock -> foodStock?.let {
                                viewModel.saveFoodItemStock(
                                    FoodItem(
                                        id = initialFoodItem?.id,
                                        foodName = it.foodName,
                                        initialQuantity = it.initialQuantity,
                                        unit = it.unit,
                                        inputDate = it.inputDate,
                                        expirationDate = it.expirationDate,
                                        foodType = it.foodType
                                    )
                                )
                            }

                            FoodOption.Wasted -> foodWasted?.let {

                                viewModel.saveWastedFood(
                                    WastedFood(
                                        id = it.id ?: wastedFood?.id,
                                        foodItemId = wastedFood?.foodItemId ?: it.foodItemId,
                                        foodItem = it.foodItem,
                                        leftoverInputDate = wastedFood?.leftoverInputDate,
                                        leftoverQuantity = it.leftoverQuantity,
                                        unit = it.unit,
                                        expirationDate = it.expirationDate,
                                        condition = it.condition,
                                        form = it.form,
                                        status = it.status
                                    )
                                )
                            }
                        }
                        navController.popBackStack()
                    },
                    lazyFoodItem = viewModel.foodItems.collectAsLazyPagingItems()
                )
            }
        }
        composable(
            Route.DistributingScreen.path,
            arguments = Route.DistributingScreen.ARGUMENTS
        ) { backStackEntry ->
            val distributionIdArg =
                backStackEntry.arguments?.getInt(Route.DistributingScreen.ARG_DISTRIBUTION_ID)
                    ?: return@composable
            var distributionDetails by remember { mutableStateOf<WastedFood?>(null) }

            LaunchedEffect(distributionIdArg) {
                distributionDetails = viewModel.findWastedFoodById(distributionIdArg)
            }

            DistributingScreen(
                onBackClicked = {
                    navController.popBackStack()
                },
                wastedFood = distributionDetails ?: return@composable,
                onSubmitted = {
                    navController.popBackStack()
                }
            )
        }
        composable(Route.NotificationScreen.path) {
            val unreadableNotifications = viewModel.unreadableNotifications
            NotificationScreen(
                unreadableNotifications = unreadableNotifications.collectAsLazyPagingItems(),
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
