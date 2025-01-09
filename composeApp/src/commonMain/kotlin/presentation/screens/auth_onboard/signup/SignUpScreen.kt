package presentation.screens.auth_onboard.signup

// Login screen implementation
import ContentWithMessageBar
import UserInfoFormScreen
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import avikfitness.composeapp.generated.resources.Res
import avikfitness.composeapp.generated.resources.img
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import presentation.components.CustomTextField
import presentation.screens.tabs.TabsScreen
import rememberMessageBarState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.hapticfeedback.HapticFeedback

class SignUpScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel = koinScreenModel<SignupViewModel>()
        val isPasswordVisible = remember { mutableStateOf(false) }
        val uiState = viewModel.uiState.value
        val state = rememberMessageBarState()
        
        // Add keyboard and focus management
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        
        // Create focus requesters
        val usernameFocusRequester = remember { FocusRequester() }
        val emailFocusRequester = remember { FocusRequester() }
        val passwordFocusRequester = remember { FocusRequester() }

        ContentWithMessageBar(messageBarState = state) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                        )
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(Res.drawable.img),
                    contentDescription = null,
                    modifier = Modifier
                        .size(320.dp)
                        .padding(30.dp)
                )

                CustomTextField(
                    value = uiState.fullName,
                    onValueChange = { viewModel.onFullNameChange(it) },
                    label = "Username",
                    keyboardType = KeyboardType.Text,
                    modifier = Modifier.focusRequester(usernameFocusRequester),
                    imeAction = ImeAction.Next,
                    onImeAction = { emailFocusRequester.requestFocus() }
                )

                CustomTextField(
                    value = uiState.emailOrUsername,
                    onValueChange = { viewModel.onEmailOrUsernameChange(it) },
                    label = "Email",
                    keyboardType = KeyboardType.Email,
                    modifier = Modifier.focusRequester(emailFocusRequester),
                    imeAction = ImeAction.Next,
                    onImeAction = { passwordFocusRequester.requestFocus() }
                )

                Spacer(modifier = Modifier.height(8.dp))

                CustomTextField(
                    value = uiState.password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = "Password",
                    visualTransformation = if (isPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                    isPasswordTextField = true,
                    keyboardType = KeyboardType.Password,
                    isPasswordVisible = isPasswordVisible.value,
                    onPasswordVisibilityToggle = { isPasswordVisible.value = !isPasswordVisible.value },
                    modifier = Modifier.focusRequester(passwordFocusRequester),
                    imeAction = ImeAction.Done,
                    onImeAction = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        viewModel.signUp()
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                when {
                    uiState.isAuthenticating -> {
                        CircularProgressIndicator()
                    }
                    uiState.authenticationSucceed -> {
                        LaunchedEffect(Unit) {
                            keyboardController?.hide()
                            if (uiState.isFormFilled) {
                                navigator?.replaceAll(TabsScreen())
                            } else {
                                navigator?.replaceAll(UserInfoFormScreen())
                            }
                        }
                    }
                    else -> {
                        OutlinedButton(
                            enabled = !uiState.isAuthenticating,
                            onClick = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                                viewModel.signUp()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            border = BorderStroke(2.dp, Color.Gray)
                        ) {
                            Text(
                                text = if (uiState.authErrorMessage != null) "Retry" else "Sign up",
                                color = Color.Red.copy(0.9f)
                            )
                        }
                    }
                }
            }
        }

        // Focus username field when screen is shown
        LaunchedEffect(Unit) {
            usernameFocusRequester.requestFocus()
        }

        // Handle error state with haptic feedback
        LaunchedEffect(uiState.authErrorMessage) {
            uiState.authErrorMessage?.let {
                state.addError(exception = Exception(it))
            }
        }
    }
}