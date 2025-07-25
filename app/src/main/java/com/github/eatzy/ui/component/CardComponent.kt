package com.github.eatzy.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LineWeight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.eatzy.R
import com.github.eatzy.domain.FoodForm
import com.github.eatzy.domain.FoodUnit
import com.github.eatzy.ui.theme.EaTzyTheme
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

@Composable
fun WeightIndicatorCard(
    modifier: Modifier = Modifier,
    weight: Float,
    progress: Float,
    unit: FoodUnit,
    foodStatus: String = "You have a healthy BMI"
) {
    val context = LocalContext.current
    val cardBackgroundColor = Color(0xFFE6F8F0)
    val primaryColor = Color(0xFF0D63F3)
    val trackColor = Color(0xFFE0E0E0)
    val textColorPrimary = Color.Black
    val textColorSecondary = Color.Gray

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardBackgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$weight ${unit.getLabel(context)}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColorPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = foodStatus,
                    fontSize = 14.sp,
                    color = textColorSecondary
                )
            }

            WeightProgressIndicator(
                progress = progress,
                primaryColor = primaryColor,
                trackColor = trackColor
            )
        }
    }
}

@Composable
private fun WeightProgressIndicator(
    progress: Float,
    primaryColor: Color,
    trackColor: Color,
    modifier: Modifier = Modifier
) {
    val strokeWidth = 12f
    val indicatorSize = 60.dp

    Box(
        modifier = modifier.size(indicatorSize),
        contentAlignment = Alignment.Center
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {
            val startAngle = 135f
            val sweepAngle = 270f

            drawArc(
                color = trackColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            drawArc(
                color = primaryColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle * progress,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Icon(
            imageVector = Icons.Default.LineWeight,
            contentDescription = "Weight Icon",
            tint = primaryColor,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeightIndicatorCardPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        WeightIndicatorCard(
            weight = 70f,
            progress = 0.7f,
            unit = FoodUnit.KILOGRAM
        )
    }
}

@Composable
fun MenuListItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val border = if (isSelected) BorderStroke(2.dp, Color.White) else null

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = border,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(text = subtitle, color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun MenuListItemPreview() {
    EaTzyTheme {
        MenuListItem(
            icon = Icons.Default.LocationOn,
            title = "Addresses",
            subtitle = "Update your address for better...",
            onClick = { /*TODO*/ }
        )
    }

}

@Composable
fun HelpItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Text(text = subtitle, color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HelpItemPreview() {
    EaTzyTheme {
        HelpItem(
            icon = Icons.AutoMirrored.Filled.Chat,
            title = "Help",
            subtitle = "Talk with us to resolve your problem",
            onClick = { /*TODO*/ }
        )
    }

}

@Composable
fun ProfileHeader(
    name: String,
    phone: String,
    cornerRadius: Dp = 12.dp,
    onEditProfileClicked: () -> Unit = {}
) {
    Column {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius))
                .background(Color.White)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "User Avatar",
                modifier = Modifier.size(60.dp),
                tint = Color.Gray
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name, fontSize = 18.sp, fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(text = phone, fontSize = 14.sp, color = Color.Black)
            }
            IconButton(onClick = onEditProfileClicked) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profile",
                    tint = Color.Gray
                )
            }
        }
        MembershipStatusBanner(12.dp)
    }
}

@Preview(showBackground = false)
@Composable
private fun ProfileHeaderPreview() {
    EaTzyTheme {
        ProfileHeader(name = "John Doe", phone = "+1 123 456 7890")
    }
}

@Composable
fun DistributionInfoCard(
    value: Double,
    progress: Double,
    foodName: String,
    typeText: String,
    modifier: Modifier = Modifier,
    status: Boolean = true
) {
    val formattedValue = NumberFormat.getNumberInstance(Locale.US).format(value)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { progress.toFloat() },
                    modifier = Modifier.fillMaxSize(),
                    color = if (status) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.error,
                    strokeWidth = 15.dp,
                    trackColor = Color(0xFFE0E0E0),
                    strokeCap = StrokeCap.Round,
                )
                Text(
                    text = formattedValue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Box(Modifier.fillMaxWidth()) {
                Column(
                    Modifier
                        .align(Alignment.CenterStart)
                ) {
                    Text(
                        text = foodName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.DarkGray,
                        lineHeight = 20.sp
                    )
                    Text(
                        text = typeText,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    painter = painterResource(R.drawable.arrow_green_check),
                    contentDescription = "Status",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.BottomEnd)
                        .offset(0.dp, 20.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DistributionInfoCardPreview() {
    EaTzyTheme {
        DistributionInfoCard(
            value = 7550.0,
            progress = 0.9,
            foodName = "Sample Food",
            typeText = "Available for Distribution"
        )
    }
}

@Composable
fun SimpleFoodCard(
    modifier: Modifier = Modifier,
    foodName: String,
    date: String,
    size: Double,
    unit: String,
    form: FoodForm? = null,
    type: String? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = foodName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                val formatter = DecimalFormat("0.#").apply { maximumFractionDigits = 2 }
                Text(
                    text = "${formatter.format(size)} $unit",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(end = 8.dp),
                )
                if (form != null) {
                    Icon(
                        painter = painterResource(id = if (form == FoodForm.SOLID) R.drawable.ic_solid else R.drawable.ic_liquid),
                        contentDescription = form.name,
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SimpleFoodCardPreview() {
    EaTzyTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            SimpleFoodCard(
                foodName = "Beef Rendang",
                date = "12 December 2023",
                size = 2.0,
                unit = FoodUnit.KILOGRAM.getLabel(LocalContext.current),
                form = FoodForm.SOLID
            )
            Spacer(modifier = Modifier.height(8.dp))
            SimpleFoodCard(
                foodName = "Beef Rendang",
                date = "12 December 2023",
                size = 2.0,
                unit = FoodUnit.KILOGRAM.getLabel(LocalContext.current),
            )
            Spacer(modifier = Modifier.height(8.dp))
            SimpleFoodCard(
                foodName = "UHT Milk",
                date = "15 January 2024",
                size = 1.2,
                unit = FoodUnit.LITER.getLabel(LocalContext.current),
                form = FoodForm.LIQUID
            )
            Spacer(modifier = Modifier.height(8.dp))
            SimpleFoodCard(
                foodName = "UHT Milk",
                date = "15 January 2024",
                size = 1.2,
                unit = FoodUnit.LITER.getLabel(LocalContext.current),
            )
        }
    }
}

@Composable
fun DestinationDistributionCard(
    isLocked: Boolean,
    modifier: Modifier = Modifier,
    name: String,
    address: String,
    description: String,
    onLockStatusChange: (Boolean) -> Unit
) {
    Card(
        modifier = modifier
            .aspectRatio(19 / 7f)
            .clickable {
                onLockStatusChange(isLocked)
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    LockAbleButton(
                        isLocked = isLocked,
                        onLockedChange = onLockStatusChange
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "location $name",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.Top)
                    )
                    val fullText = "$address, $description"
                    val annotatedString = androidx.compose.ui.text.buildAnnotatedString {
                        append(fullText)
                        addStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold),
                            start = 0,
                            end = address.length
                        )
                    }
                    Text(
                        overflow = Ellipsis,
                        maxLines = 3, text = annotatedString,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DestinationDistributionCardPreview() {
    EaTzyTheme {
        DestinationDistributionCard(
            name = "Destination Name",
            address = "Address",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do usermod temper incident ut labor et do lore magna aliquot. This is an extended description to make it longer than a single sentence, fulfilling the requirement.",
            onLockStatusChange = {},
            isLocked = false
        )
    }
}

@Composable
fun SimpleNotificationCard(
    subject: String,
    message: String,
    modifier: Modifier = Modifier,
    isRead: Boolean = false,
    onReadeChange: (Boolean) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    var currentIsRead by remember { mutableStateOf(isRead) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                expanded = !expanded
                onReadeChange(true)
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Box {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(4f)) {
                    Text(
                        overflow = Ellipsis,
                        text = subject,
                        maxLines = if (expanded) Int.MAX_VALUE else 1,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = message,
                        overflow = Ellipsis,
                        maxLines = if (expanded) Int.MAX_VALUE else 3,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
            if (!expanded) {
                Icon(
                    imageVector = if (isRead) Icons.Default.CheckCircle else Icons.Default.Circle,
                    contentDescription = stringResource(if (currentIsRead) R.string.status_read else R.string.status_unread),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(34.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = 4.dp, y = 4.dp)

                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFC8E6C9)
@Composable
fun NotificationCardPreview() {
    Column {
        var isRead1 by remember { mutableStateOf(true) }
        var isRead2 by remember { mutableStateOf(false) }
        SimpleNotificationCard(
            isRead = isRead1,
            onReadeChange = { isRead1 = it },
            modifier = Modifier.padding(16.dp),
            subject = "Orphanage is willing to accept allocated food",
            message = "Note type, Fried Rice. This is an example of a longer message. Hope it can help you. Thank you for your attention."
        )
        SimpleNotificationCard(
            isRead = isRead2,
            onReadeChange = { isRead2 = it },
            modifier = Modifier.padding(16.dp),
            subject = "Orphanage is willing to accept allocated food",
            message =
                "Note type, Fried Rice. This is an example of a longer message. Hope it can help you. Thank you for your attention."
        )
    }
}


@Composable
fun FoodInfoCard(
    modifier: Modifier = Modifier,
    progress: Double,
    valueText: String,
    foodName: String,
    quantityDetails: String,
    typeText: FoodForm,
    expiryInfo: String,
    isExpired: Boolean = false
) {
    val context = LocalContext.current
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { progress.toFloat() },
                    modifier = Modifier.fillMaxSize(),
                    color = if (isExpired) Color.Red else Color(0xFF8BC34A),
                    strokeWidth = 8.dp,
                    trackColor = Color(0xFFE0E0E0),
                    strokeCap = StrokeCap.Round,
                )
                Text(
                    text = valueText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = foodName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    maxLines = 1,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = quantityDetails,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = typeText.getLabel(context),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_liquid),
                        contentDescription = "Type Icon",
                        tint = Color(0xFF9C27B0),
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    text = expiryInfo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isExpired) Color.Red else Color.Gray,
                    fontWeight = if (isExpired) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodInfoCardPreview() {
    FoodInfoCard(
        modifier = Modifier.padding(16.dp),
        progress = 0.75,
        valueText = "25",
        foodName = "Yogurt",
        quantityDetails = "25 bottles",
        typeText = FoodForm.LIQUID,
        expiryInfo = "24 Dec 2025 , expired",
        isExpired = false
    )
}