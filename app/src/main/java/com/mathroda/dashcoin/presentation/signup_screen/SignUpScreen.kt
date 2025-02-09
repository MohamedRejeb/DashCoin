package com.mathroda.dashcoin.presentation.signup_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mathroda.dashcoin.core.util.Constants
import com.mathroda.dashcoin.presentation.coin_detail.components.BackStackButton
import com.mathroda.dashcoin.presentation.signin_screen.components.CustomClickableText
import com.mathroda.dashcoin.presentation.signin_screen.components.CustomLoginButton
import com.mathroda.dashcoin.presentation.signin_screen.components.CustomTextField
import com.mathroda.dashcoin.presentation.signup_screen.viewmodel.SignUpViewModel
import com.mathroda.dashcoin.presentation.ui.theme.Gold
import com.mathroda.dashcoin.presentation.ui.theme.TextWhite
import com.talhafaki.composablesweettoast.util.SweetToastUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    navigateToSignInScreen: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var isEnabled by remember { mutableStateOf(true) }
    val signUpState = viewModel.signUp.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 32.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .requiredHeight(40.dp)
            ) {
                BackStackButton(
                    modifier = Modifier
                        .clickable {
                            navigateToSignInScreen()
                        }
                )
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomClickableText(
                    text = Constants.CREATE_ACCOUNT,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                ) {}
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomClickableText(
                    text = Constants.SIGN_UP_TO_GET_STARTED,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                ) {}
            }


            Spacer(modifier = Modifier.height(60.dp))

            CustomTextField(
                text = email,
                placeholder = "Email",
                isPasswordTextField = false,
                onValueChange = { email = it.trim() },
                isError = isError,
                errorMsg = "*Enter valid email address",
                trailingIcon = {
                    if (email.isNotBlank()) {
                        IconButton(onClick = { email = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                text = password,
                placeholder = "Password",
                isPasswordTextField = !isPasswordVisible,
                onValueChange = { password = it },
                isError = isError,
                errorMsg = "*Enter valid password",
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            tint = Color.Gray,
                            contentDescription = "Password Toggle")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)

            )


            Spacer(modifier = Modifier.weight(0.2f))

            CustomLoginButton(
                text = "REGISTER",
                modifier = Modifier.fillMaxWidth(),
                enabled = isEnabled,
            ) {
                isEnabled = false
                isError = email.isEmpty() || password.isEmpty()
                viewModel.signUp(email, password)
                isLoading = !isLoading
            }

            Spacer(modifier = Modifier.weight(0.4f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomClickableText(
                    text = "already have an account? ",
                    fontSize = 16.sp
                ) {}
                Spacer(modifier = Modifier.width(4.dp))
                CustomClickableText(
                    text = "Login",
                    color = Gold,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.W500
                ) {
                    navigateToSignInScreen()
                }
            }
        }
    }

    if (signUpState.value.isLoading) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }


        if (signUpState.value.signUp != null) {
            SweetToastUtil.SweetSuccess(
                message = "Account created successfully",
                padding = PaddingValues(bottom = 24.dp)
            )
            rememberCoroutineScope().launch {
                delay(700)
                navigateToSignInScreen()
            }
        }

    if(signUpState.value.error.isNotBlank()) {
        isEnabled = true
        val errorMsg = signUpState.value.error
        SweetToastUtil.SweetError(
            message = errorMsg,
            padding = PaddingValues(bottom = 24.dp)
        )
    }
}