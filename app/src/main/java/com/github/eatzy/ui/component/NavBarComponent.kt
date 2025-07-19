package com.github.eatzy.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.eatzy.R
import com.github.eatzy.domain.DistributionOption
import com.github.eatzy.domain.FoodOption
import com.github.eatzy.ui.navigation.Route
import com.github.eatzy.ui.theme.DarkGreen
import com.github.eatzy.ui.theme.EaTzyTheme

data class BottomNavItem(
    val route: Route,
    val label: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter,
)

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    selectedItemIndex: Int,
    onItemSelected: (Int, Route) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color.White,
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    onItemSelected(index, item.route)
                },
                alwaysShowLabel = true,
                icon = {
                    Icon(
                        painter = if (selectedItemIndex == index) {
                            item.selectedIcon
                        } else {
                            item.unselectedIcon
                        },
                        contentDescription = item.route.path,
                        tint = Color.Unspecified
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                ),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun NavbarComponentPreview() {
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
    EaTzyTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
                    .padding(bottom = 56.dp)
            ) {}
            var selectedItemIndex by remember { mutableIntStateOf(0) }
            BottomNavigationBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                items = items,
                selectedItemIndex = selectedItemIndex,
                onItemSelected = { index, _ ->
                    selectedItemIndex = index
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarComponent(
    modifier: Modifier = Modifier,
    showNotificationBadge: Boolean = false,
    title: String? = null,
    onBackClick: (() -> Unit)? = null,
    onNotificationClick: (() -> Unit)? = null
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (title != null) {
                    Text(
                        text = title,
                    )
                }
            }
        },
        navigationIcon = {

            if (onBackClick != null) {
                IconButtonCircle(
                    onClick = onBackClick
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp),
                        contentDescription = stringResource(R.string.back_button_desc)
                    )
                }
            } else {
                IconButton(
                    modifier = Modifier.aspectRatio(1f),
                    onClick = {}
                ) { }
            }
        },
        actions = {
            if (onNotificationClick != null) {
                BadgedBox(
                    badge = {
                        if (showNotificationBadge)
                            Badge(
                                modifier = Modifier
                                    .offset((-5).dp, y = 3.dp)
                                    .size(15.dp),
                                containerColor = DarkGreen,
                                contentColor = Color.White
                            )
                    }
                ) {
                    IconButtonCircle(
                        onClick = onNotificationClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp),
                            contentDescription = stringResource(R.string.notification_button_desc)
                        )
                    }
                }
            } else {
                IconButton(
                    modifier = Modifier.aspectRatio(1f),
                    onClick = {}
                ) { }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun TopAppBarComponentPreview() {
    EaTzyTheme {
        Column(Modifier.background(MaterialTheme.colorScheme.tertiaryContainer)) {
            TopAppBarComponent(
                showNotificationBadge = true,
                title = stringResource(R.string.food_option_wasted),
                onBackClick = {},
                onNotificationClick = {})
            TopAppBarComponent(
                title = stringResource(R.string.food_option_wasted),
                onNotificationClick = {})
            TopAppBarComponent(
                title = stringResource(R.string.food_option_wasted),
                onBackClick = {},
            )
        }
    }
}

@Composable
fun <T : Enum<T>> Toggle(
    options: Array<T>,
    initialSelectedOption: T,
    onOptionSelected: (T) -> Unit,
    optionTextProvider: (T) -> String,
) {
    var selectedOption by remember { mutableStateOf(initialSelectedOption) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(CircleShape)
            .background(Color(0xFF8BC34A).copy(alpha = 0.5f))
            .padding(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            options.forEach { option ->
                val isSelected = selectedOption == option
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(if (isSelected) Color.White else Color.Transparent)
                        .clickable { selectedOption = option; onOptionSelected(option) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = optionTextProvider(option),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun FoodToggle(onOptionSelected: (FoodOption) -> Unit = {}) {
    val context = LocalContext.current
    Toggle(
        options = FoodOption.entries.toTypedArray(),
        initialSelectedOption = FoodOption.STOCK,
        onOptionSelected = onOptionSelected,
        optionTextProvider = { it.getLabel(context) }
    )
}

@Composable
fun DistributionToggle(onOptionSelected: (DistributionOption) -> Unit = {}) {
    val context = LocalContext.current
    Toggle(
        options = DistributionOption.entries.toTypedArray(),
        initialSelectedOption = DistributionOption.Send,
        onOptionSelected = onOptionSelected,
        optionTextProvider = { it.getLabel(context) }
    )
}


@Preview(showBackground = true)
@Composable
private fun FoodTypeTogglePreview() {
    EaTzyTheme {
        FoodToggle()
    }
}

@Preview(showBackground = true)
@Composable
private fun DistributionStateTogglePreview() {
    EaTzyTheme {
        DistributionToggle()
    }
}