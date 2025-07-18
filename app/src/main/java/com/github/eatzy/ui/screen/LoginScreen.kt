package com.github.eatzy.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.github.eatzy.R
import com.github.eatzy.ui.component.FoodMerchantRegistrationData
import com.github.eatzy.ui.component.LoginFormComponent
import com.github.eatzy.ui.component.RegistrationFormComponent
import com.github.eatzy.ui.component.WhiteButton
import com.github.eatzy.ui.theme.EaTzyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginClicked: ((String, String) -> Unit)? = null,
    onRegisterClicked: ((FoodMerchantRegistrationData) -> Unit)? = null,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { false }
    )
    var optionState by remember { mutableStateOf("") }
    Scaffold { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                painter = if (optionState.isEmpty())
                    painterResource(R.drawable.cooking_figure)
                else
                    painterResource(R.drawable.eating_healty),
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(vertical = 50.dp),
                contentScale = ContentScale.Fit,
                contentDescription = if (optionState.isEmpty()) "Cooking Figure" else "Eat Figure"
            )

            ModalBottomSheet(
                onDismissRequest = {},
                sheetState = sheetState,
                scrimColor = Color.Transparent,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            ) {
                if (optionState == "login") {
                    LoginFormComponent(
                        onLoginClicked = onLoginClicked ?: { _, _ -> },
                        onForgotPasswordClicked = { optionState = "register" },
                    )
                }

                if (optionState == "register") {
                    RegistrationFormComponent(
                        onRegisterClicked = onRegisterClicked ?: { _ -> },
                        onAlreadyHaveAccountClicked = { optionState = "login" },
                    )
                }
                val buttonSize = DpSize(250.dp, 50.dp)
                if (optionState.isEmpty()) {
                    WhiteButton(
                        onClick = {
                            optionState = "login"
                        },
                        modifier = Modifier
                            .size(buttonSize)
                            .align(Alignment.CenterHorizontally),
                        text = "Login",
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    WhiteButton(
                        onClick = {
                            optionState = "register"
                        },
                        modifier = Modifier
                            .size(buttonSize)
                            .align(Alignment.CenterHorizontally),
                        text = "Register",
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "sign up, or Continue with",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = {/*TODO*/ }) {
                        Icon(
                            painter = painterResource(R.drawable.icons8_google),
                            modifier = Modifier.size(32.dp),
                            contentDescription = "Google Login",
                            tint = Color.Unspecified
                        )
                    }
                    IconButton(onClick = {/*TODO*/ }) {
                        Icon(
                            painter = painterResource(R.drawable.icons8_facebook),
                            contentDescription = "Facebook Login",
                            modifier = Modifier.size(32.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    EaTzyTheme {
        LoginScreen()
    }
}
