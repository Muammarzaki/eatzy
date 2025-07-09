package com.github.eatzy.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.eatzy.ui.theme.EaTzyTheme
import com.github.eatzy.ui.theme.GrayDark


@Composable
fun WhiteInputTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
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
            modifier = Modifier.padding(contentPadding),
            color = MaterialTheme.colorScheme.primary,
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
            placeholder = { Text("Username", color = GrayDark, fontWeight = FontWeight.SemiBold) },
        )

        Spacer(modifier = Modifier.height(16.dp))

        WhiteInputTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Password", color = GrayDark, fontWeight = FontWeight.SemiBold) },
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
            modifier = Modifier.background(MaterialTheme.colorScheme.primary),
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
                    color = GrayDark,
                    fontWeight = FontWeight.SemiBold
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(16.dp))

        WhiteInputTextField(
            value = address,
            onValueChange = { address = it },
            placeholder = { Text("Address", color = GrayDark, fontWeight = FontWeight.SemiBold) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(16.dp))

        WhiteInputTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            placeholder = {
                Text(
                    "Phone Number",
                    color = GrayDark,
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
            placeholder = { Text("Email", color = GrayDark, fontWeight = FontWeight.SemiBold) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        WhiteInputTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Password", color = GrayDark, fontWeight = FontWeight.SemiBold) },
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
            modifier = Modifier.background(MaterialTheme.colorScheme.primary),
            onRegisterClicked = { _, _, _, _, _ -> },
            onAlreadyHaveAccountClicked = {},
        )

    }
}