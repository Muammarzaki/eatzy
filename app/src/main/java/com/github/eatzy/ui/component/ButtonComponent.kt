package com.github.eatzy.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.eatzy.R
import com.github.eatzy.ui.theme.Danger
import com.github.eatzy.ui.theme.EaTzyTheme

@Composable
fun LogoutButton() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: Aksi logout */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Logout,
                    contentDescription = "Log out",
                    tint = Danger,
                    modifier = Modifier.rotate(180f)
                )
            }
            Spacer(Modifier.width(16.dp))
            Text(
                text = "Log out",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun AddFab(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier.size(68.dp),
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add",
            modifier = Modifier.size(32.dp)
        )
    }
}

@Preview
@Composable
private fun AddFabPreview() {
    EaTzyTheme {
        Box(
            modifier = Modifier
                .size(100.dp)
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            AddFab {}
        }
    }
}

@Preview
@Composable
private fun LogoutButtonPreview() {
    EaTzyTheme {
        LogoutButton()
    }
}

@Composable
fun MembershipStatusBanner(bottomCorner: Dp = 0.dp) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = bottomCorner, bottomEnd = bottomCorner),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "EATHLY PLUS MEMBER",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Preview
@Composable
private fun MembershipStatusBannerPreview() {
    EaTzyTheme {
        MembershipStatusBanner(16.dp)
    }
}

@Composable
fun <T : Enum<T>> BigSwitch(
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    optionToString: (T) -> String = { it.name },
    backgroundColor: Color = Color.White,
    contentColor: Color = Color.Black,
    selectedColor: Color = backgroundColor,

    ) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .selectableGroup(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            options.forEach { option ->
                val isSelected = option == selectedOption
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) contentColor else Color.Transparent)
                        .clickable { onOptionSelected(option) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = optionToString(option),
                        color = if (isSelected) selectedColor else contentColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

enum class SampleEnum { OPTION_1, OPTION_2 }

@Preview
@Composable
private fun BigSwitchPreview() {
    EaTzyTheme {
        val options = SampleEnum.entries
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(options[0]) }
        BigSwitch(
            options = options,
            selectedOption = selectedOption,
            onOptionSelected = onOptionSelected
        )
    }
}

enum class DeliveryOptions { DELIVERY, PICKUP, JAWA }

@Preview
@Composable
private fun BigSwitchPreviewDark() {
    EaTzyTheme(darkTheme = true) {
        val options = DeliveryOptions.values().toList()
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(options[1]) }
        BigSwitch(
            options = options,
            selectedOption = selectedOption,
            onOptionSelected = onOptionSelected
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChipFilterDropdown(
    modifier: Modifier = Modifier,
    categoryItems: List<String>,
    selectedItem: String = categoryItems.firstOrNull() ?: "",
    onItemSelected: (String) -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    val rotationAngle by animateFloatAsState(
        targetValue = if (isMenuExpanded) 180f else 0f,
        label = "Dropdown Arrow Rotation"
    )

    Box(
        modifier = modifier
            .wrapContentSize(),
    ) {
        FilterChip(
            selected = isMenuExpanded,
            onClick = { isMenuExpanded = !isMenuExpanded },
            label = { Text(text = selectedItem, fontWeight = FontWeight.Bold) },
            border = BorderStroke(0.dp, Color.Transparent),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown Arrow",
                    modifier = Modifier.rotate(rotationAngle)
                )
            }
        )

        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false }
        ) {
            categoryItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        onItemSelected(item)
                        isMenuExpanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChipFilterDropdownPreview() {
    val categoryItems = listOf("All Categories", "Clothing", "Electronics", "Food", "Books")
    var selectedCategory by remember { mutableStateOf(categoryItems[0]) }
    EaTzyTheme {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            ChipFilterDropdown(
                categoryItems = categoryItems,
                selectedItem = selectedCategory,
                onItemSelected = { selectedCategory = it }
            )
        }
    }
}

@Composable
fun IconButtonCircle(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    IconButton(
        onClick = onClick, modifier
            .padding(10.dp)
            .aspectRatio(1f)
            .background(
                MaterialTheme.colorScheme.onTertiaryContainer,
                shape = RoundedCornerShape(40)
            )
    ) {
        content()
    }
}

@Preview
@Composable
private fun IconButtonCirclePreview() {
    EaTzyTheme {
        Box(Modifier.size(100.dp)) {
            IconButtonCircle(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun LockAbleButton(
    isLocked: Boolean,
    onLockedChange: (Boolean) -> Unit
) {
    Button(
        modifier = Modifier
            .wrapContentSize(),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        ),
        onClick = {
            onLockedChange(!isLocked)
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.scale(1.2f),
                painter = if (isLocked) painterResource(R.drawable.lock_check)
                else painterResource(R.drawable.unlock_check),
                contentDescription = "Lock Button",
            )
        }
    }
}

@Preview
@Composable
private fun LockAbleButtonPreview() {
    EaTzyTheme {
        var isLocked by remember { mutableStateOf(false) }
        LockAbleButton(
            isLocked = isLocked,
            onLockedChange = { isLocked = it }
        )
    }
}

@Composable
fun RegularButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    textColor: Color = MaterialTheme.colorScheme.primary,
    text: String
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(60.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onTertiaryContainer,
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = textColor,
            fontWeight = FontWeight.W500
        )
    }
}

@Preview
@Composable
private fun RegularButtonPreview() {
    EaTzyTheme {
        RegularButton(onClick = {}, text =  "Test Button")
    }
}