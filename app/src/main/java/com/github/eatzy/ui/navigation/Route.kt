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
    object SubmitTransitionScreen : Route("submit_tr_screen")


    object FormScreen : Route("form_screen/{option}?foodId={foodId}") {

        const val ROUTE_BASE = "form_screen"
        const val ARG_OPTION = "option"
        const val ARG_FOOD_ID = "foodId"

        val ARGUMENTS = listOf(
            navArgument(ARG_OPTION) {
                type = NavType.StringType
            },
            navArgument(ARG_FOOD_ID) {
                type = NavType.IntType
                defaultValue = -1
            }
        )

        fun createRoute(option: FoodOption, foodId: Int? = null): String {
            val base = "$ROUTE_BASE/${option.name}"
            return foodId?.let { "$base?$ARG_FOOD_ID=$it" } ?: base
        }
    }

    object DistributingScreen : Route("distributing_screen/{distributionId}") {

        const val ROUTE_BASE = "distributing_screen"
        const val ARG_DISTRIBUTION_ID = "distributionId"

        val ARGUMENTS = listOf(
            navArgument(ARG_DISTRIBUTION_ID) {
                type = NavType.IntType
            }
        )

        fun createRoute(distributionId: Int): String {
            return "$ROUTE_BASE/$distributionId"
        }
    }

}
