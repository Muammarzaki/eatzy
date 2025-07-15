package com.github.eatzy.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.github.eatzy.domain.FoodOption

sealed class Route(val path: String) {
    object SplashScreen : Route("splash_screen")
    object LoginScreen : Route("login_screen")
    object HomeScreen : Route("home_screen")
    object ProfileScreen : Route("user_screen")
    object FoodListScreen : Route("food_list_screen")
    object DistributionScreen : Route("distribution_screen")
    object NotificationScreen : Route("notification_screen")


    object FormScreen : Route("form_screen/{option}") {
        val ARG = listOf(
            navArgument("option") {
                type = NavType.StringType
            }
        )

        fun createRoute(option: FoodOption) = "form_screen/${option.name}"
    }

    object DistributingScreen : Route("distributing_screen/{distributionId}") {
        val ARG = listOf(
            navArgument("distributionId") {
                type = NavType.IntType
            }
        )

        fun createRoute(distributionId: Int) = "distributing_screen/$distributionId"
    }
}
