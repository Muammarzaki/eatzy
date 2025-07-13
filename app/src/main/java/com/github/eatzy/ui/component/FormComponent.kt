package com.github.eatzy.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.eatzy.domain.FoodCondition
import com.github.eatzy.domain.FoodForm
import com.github.eatzy.domain.FoodItem
import com.github.eatzy.domain.FoodUnit
import com.github.eatzy.domain.WastedFood
import com.github.eatzy.ui.theme.DarkGreen
import com.github.eatzy.ui.theme.EaTzyTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
fun WhiteInputTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = placeholder,
        singleLine = true,
        shape = RoundedCornerShape(50),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        )
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInputForm(
    modifier: Modifier = Modifier,
    initialDate: Date? = null,
    placeholder: String = "DD/MM/YYYY",
    onDateSelected: (String, String, String) -> Unit
) {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    var date by remember {
        mutableStateOf(initialDate?.let { sdf.format(it) } ?: "")
    }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate?.time
            ?: Calendar.getInstance().timeInMillis
    )

    var showDatePicker by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        WhiteInputTextFieldWithBorder(
            value = date,
            onValueChange = {
                if (it.length <= 10 && (it.isEmpty() || it.matches(Regex("\\d{0,2}/?\\d{0,2}/?\\d{0,4}")))) {
                    date = it
                }
            },
            placeholder = placeholder,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                Button(
                    onClick = {
                        showDatePicker = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent) // Transparent background
                ) {
                    Icon(
                        Icons.Filled.DateRange,
                        contentDescription = "Pick Date",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        )
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        showDatePicker = false
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formattedDate = sdf.format(millis)
                            date = formattedDate
                            val parts = formattedDate.split("/")
                            val day = if (parts.isNotEmpty()) parts[0] else ""
                            val month = if (parts.size > 1) parts[1] else ""
                            val year = if (parts.size > 2) parts[2] else ""
                            onDateSelected(day, month, year)
                        }
                    }) {
                        Text("OK")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DateInputFormPreview() {
    EaTzyTheme {
        DateInputForm(
            modifier = Modifier.background(MaterialTheme.colorScheme.tertiaryContainer),
            onDateSelected = { _, _, _ -> }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Enum<T>> EnumDropdown(
    modifier: Modifier = Modifier,
    label: String,
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        WhiteInputTextFieldWithBorder(
            value = selectedOption.name,
            onValueChange = {},
            placeholder = label,
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption.name) },
                    onClick = {
                        onOptionSelected(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EnumDropdownPreview() {
    EaTzyTheme {
        Column {
            Spacer(modifier = Modifier.height(50.dp))
            var selectedOption by remember { mutableStateOf(SampleEnum.OPTION_1) }
            EnumDropdown(
                label = "Sample Dropdown",
                options = SampleEnum.entries,
                selectedOption = selectedOption,
                onOptionSelected = { selectedOption = it }
            )
        }
    }
}


@Composable
fun WhiteButton(
    modifier: Modifier,
    onClick: () -> Unit,
    text: String,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(Color.White),
        modifier = modifier
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(contentPadding)
                .padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LoginFormComponent(
    modifier: Modifier = Modifier,
    onLoginClicked: (String, String) -> Unit,
    onForgotPasswordClicked: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = modifier.padding(16.dp)) {
        WhiteInputTextField(
            value = username,
            onValueChange = { username = it },
            placeholder = {
                Text(
                    "Username",
                    color = MaterialTheme.colorScheme.outline,
                    fontWeight = FontWeight.SemiBold
                )
            },
        )

        Spacer(modifier = Modifier.height(16.dp))

        WhiteInputTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = {
                Text(
                    "Password",
                    color = MaterialTheme.colorScheme.outline,
                    fontWeight = FontWeight.SemiBold
                )
            },
            visualTransformation = PasswordVisualTransformation(),
        )
        TextButton(
            onClick = onForgotPasswordClicked,
        ) {
            Text(
                text = "Forgot Password?",
                color = Color.White,
                textAlign = TextAlign.Start,
                textDecoration = TextDecoration.Underline
            )
        }
        WhiteButton(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterHorizontally),
            onClick = { onLoginClicked(username, password) },
            text = "Login",
            contentPadding = PaddingValues(horizontal = 80.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginComponentPreview() {
    EaTzyTheme {
        LoginFormComponent(
            modifier = Modifier.background(MaterialTheme.colorScheme.tertiaryContainer),
            onLoginClicked = { _, _ -> },
            onForgotPasswordClicked = {},
        )
    }
}


@Composable
fun RegistrationFormComponent(
    modifier: Modifier = Modifier,
    onRegisterClicked: (String, String, String, String, String) -> Unit,
    onAlreadyHaveAccountClicked: () -> Unit,
) {
    var businessName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(16.dp),
    ) {
        WhiteInputTextField(
            value = businessName,
            onValueChange = { businessName = it },
            placeholder = {
                Text(
                    "Business name",
                    color = MaterialTheme.colorScheme.outline,
                    fontWeight = FontWeight.SemiBold
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(16.dp))

        WhiteInputTextField(
            value = address,
            onValueChange = { address = it },
            placeholder = {
                Text(
                    "Address",
                    color = MaterialTheme.colorScheme.outline,
                    fontWeight = FontWeight.SemiBold
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(16.dp))

        WhiteInputTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            placeholder = {
                Text(
                    "Phone Number",
                    color = MaterialTheme.colorScheme.outline,
                    fontWeight = FontWeight.SemiBold
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        WhiteInputTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = {
                Text(
                    "Email",
                    color = MaterialTheme.colorScheme.outline,
                    fontWeight = FontWeight.SemiBold
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        WhiteInputTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = {
                Text(
                    "Password",
                    color = MaterialTheme.colorScheme.outline,
                    fontWeight = FontWeight.SemiBold
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )
        TextButton(
            onClick = onAlreadyHaveAccountClicked,
        ) {
            Text(
                text = "Already Have Account ?",
                color = Color.White,
                textAlign = TextAlign.Start,
                textDecoration = TextDecoration.Underline
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        WhiteButton(
            onClick = { onRegisterClicked(businessName, address, phoneNumber, email, password) },
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterHorizontally),
            text = "Register",
            contentPadding = PaddingValues(horizontal = 80.dp)
        )
    }
}

@Preview
@Composable
private fun RegistrationFormPreview() {
    EaTzyTheme {
        RegistrationFormComponent(
            modifier = Modifier.background(MaterialTheme.colorScheme.tertiaryContainer),
            onRegisterClicked = { _, _, _, _, _ -> },
            onAlreadyHaveAccountClicked = {},
        )

    }
}

@Composable
fun WhiteInputTextFieldWithBorder(
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    shape: Shape = RoundedCornerShape(20)
) {
    OutlinedTextField(
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        readOnly = readOnly,
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder, color = Color.Gray) },
        modifier = modifier
            .fillMaxWidth()
            .border(5.dp, MaterialTheme.colorScheme.onTertiaryContainer, shape = shape)
            .padding(4.dp)
            .border(2.dp, borderColor, shape = shape),
        shape = shape,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = borderColor
        )
    )
}

@Composable
fun WastedFoodInputForm(
    modifier: Modifier = Modifier,
    onSubmitted: (WastedFood) -> Unit,
    initialData: WastedFood? = null
) {
    var foodName by remember { mutableStateOf(initialData?.foodItem ?: "") }
    val (selectedFoodForm, onFoodFormSelected) = remember { mutableStateOf(FoodForm.entries.first()) }
    var quantity by remember { mutableStateOf(initialData?.leftoverQuantity?.toString() ?: "") }
    var unit by remember { mutableStateOf(initialData?.unit ?: FoodUnit.KILOGRAM) }
    var condition by remember { mutableStateOf(initialData?.condition ?: FoodCondition.DISPOSED) }
    var expiryDate by remember { mutableStateOf(initialData?.expirationDate ?: Date()) }

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Food Name",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                textAlign = TextAlign.Start,
                color = Color.White,
                fontWeight = MaterialTheme.typography.displaySmall.fontWeight,
                style = MaterialTheme.typography.bodyLarge
            )
            WhiteInputTextFieldWithBorder(
                value = foodName,
                onValueChange = { foodName = it },
                placeholder = "Nama Makanan",
                borderColor = DarkGreen
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Quantity",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                textAlign = TextAlign.Start,
                fontWeight = MaterialTheme.typography.displaySmall.fontWeight,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
            WhiteInputTextFieldWithBorder(
                value = quantity,
                onValueChange = { quantity = it },
                placeholder = "Jumlah Sisa",
                borderColor = DarkGreen
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Unit",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                textAlign = TextAlign.Start,
                fontWeight = MaterialTheme.typography.displaySmall.fontWeight,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
            EnumDropdown(
                label = "Unit",
                options = FoodUnit.entries,
                selectedOption = unit,
                onOptionSelected = { unit = it }
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Condition",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                textAlign = TextAlign.Start,
                fontWeight = MaterialTheme.typography.displaySmall.fontWeight,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
            EnumDropdown(
                label = "Unit",
                options = FoodCondition.entries,
                selectedOption = condition,
                onOptionSelected = { condition = it }
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Expiry Date",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                textAlign = TextAlign.Start,
                fontWeight = MaterialTheme.typography.displaySmall.fontWeight,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
            DateInputForm(
                initialDate = expiryDate,
                onDateSelected = { day, month, year ->
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, year.toInt())
                    calendar.set(Calendar.MONTH, month.toInt() - 1)
                    calendar.set(Calendar.DAY_OF_MONTH, day.toInt())
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Food Form",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                textAlign = TextAlign.Start,
                fontWeight = MaterialTheme.typography.displaySmall.fontWeight,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )

            BigSwitch(
                options = FoodForm.entries,
                selectedOption = selectedFoodForm,
                onOptionSelected = onFoodFormSelected,
                backgroundColor = MaterialTheme.colorScheme.onTertiaryContainer,
                contentColor = Color.White,
                selectedColor = MaterialTheme.colorScheme.inverseSurface
            )

            Spacer(modifier = Modifier.height(60.dp))

        }
        RegularButton(
            onClick = {
                onSubmitted(
                    WastedFood(
                        foodItem = foodName,
                        leftoverQuantity = quantity.toDoubleOrNull() ?: 0.0,
                        unit = unit,
                        condition = condition,
                        expirationDate = expiryDate,
                        form = selectedFoodForm,
                    )
                )
            },
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = 16.dp)
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            textColor = Color.White,
            text = "Submit"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WastedFoodInputFormPreview() {
    EaTzyTheme {
        WastedFoodInputForm(
            modifier = Modifier.background(MaterialTheme.colorScheme.tertiaryContainer),
            onSubmitted = {},
            initialData = WastedFood(
                foodItem = "Nasi Goreng",
                leftoverQuantity = 2.0,
                unit = FoodUnit.PORTION,
                condition = FoodCondition.EDIBLE,
                expirationDate = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, 2)
                }.time,
                form = FoodForm.SOLID,
            )
        )
    }
}


@Composable
fun FoodInputForm(
    modifier: Modifier = Modifier,
    onSubmitted: (FoodItem) -> Unit,
    initialData: FoodItem? = null
) {
    var foodName by remember { mutableStateOf(initialData?.foodName ?: "") }
    val (selectedFoodForm, onFoodFormSelected) = remember { mutableStateOf(FoodForm.entries.first()) }
    var foodType by remember { mutableStateOf(initialData?.foodType ?: "") }
    var expiryDate by remember { mutableStateOf(initialData?.expirationDate ?: Date()) }
    var quantity by remember { mutableStateOf(initialData?.initialQuantity?.toString() ?: "") }
    var unit by remember { mutableStateOf(initialData?.unit ?: FoodUnit.KILOGRAM) }
    Box {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Food Name",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                textAlign = TextAlign.Start,
                color = Color.White,
                fontWeight = MaterialTheme.typography.displaySmall.fontWeight,
                style = MaterialTheme.typography.bodyLarge
            )
            WhiteInputTextFieldWithBorder(
                value = foodName,
                onValueChange = { foodName = it },
                placeholder = "Nama Makanan",
                borderColor = DarkGreen
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Quantity",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                textAlign = TextAlign.Start,
                fontWeight = MaterialTheme.typography.displaySmall.fontWeight,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
            WhiteInputTextFieldWithBorder(
                value = quantity,
                onValueChange = { quantity = it },
                placeholder = "Jumlah",
                borderColor = DarkGreen
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Unit",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                textAlign = TextAlign.Start,
                fontWeight = MaterialTheme.typography.displaySmall.fontWeight,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
            EnumDropdown(
                label = "Unit",
                options = FoodUnit.entries,
                selectedOption = unit,
                onOptionSelected = { unit = it }
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Type",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                textAlign = TextAlign.Start,
                fontWeight = MaterialTheme.typography.displaySmall.fontWeight,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
            WhiteInputTextFieldWithBorder(
                value = foodType,
                onValueChange = { foodType = it },
                placeholder = "Jenis Makanan",
                borderColor = DarkGreen
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Expiry Date",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                textAlign = TextAlign.Start,
                fontWeight = MaterialTheme.typography.displaySmall.fontWeight,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
            DateInputForm(
                initialDate = expiryDate,
                onDateSelected = { day, month, year ->
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, year.toInt())
                    calendar.set(Calendar.MONTH, month.toInt() - 1)
                    calendar.set(Calendar.DAY_OF_MONTH, day.toInt())
                    expiryDate = calendar.time
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Food Form",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                textAlign = TextAlign.Start,
                fontWeight = MaterialTheme.typography.displaySmall.fontWeight,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )

            BigSwitch(
                options = FoodForm.entries,
                selectedOption = selectedFoodForm,
                onOptionSelected = onFoodFormSelected,
                backgroundColor = MaterialTheme.colorScheme.onTertiaryContainer,
                contentColor = Color.White,
                selectedColor = MaterialTheme.colorScheme.inverseSurface
            )

            Spacer(modifier = Modifier.height(40.dp))

        }
        RegularButton(
            onClick = {
                onSubmitted(
                    FoodItem(
                        id = null,
                        foodName = foodName,
                        foodType = foodType,
                        expirationDate = expiryDate,
                        initialQuantity = quantity.toDouble(),
                        unit = unit,
                    )
                )
            },
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = 16.dp)
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            textColor = Color.White,
            text = "Submit"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FoodInputFormPreview() {
    EaTzyTheme {
        FoodInputForm(
            modifier = Modifier.background(MaterialTheme.colorScheme.tertiaryContainer),
            onSubmitted = {},
            initialData = FoodItem(
                id = 1,
                foodName = "Nasi Goreng",
                foodType = "Main Course",
                expirationDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 5) }.time,
                initialQuantity = 2.0,
                unit = FoodUnit.PORTION
            )
        )
    }
}
