package presentation.screens.auth_onboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import avikfitness.composeapp.generated.resources.Res
import avikfitness.composeapp.generated.resources.login_signup_dark
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.jetbrains.compose.resources.painterResource
import presentation.screens.auth_onboard.login.LoginScreen
import presentation.screens.auth_onboard.signup.SignUpScreen
import ui.GymAppTheme

class AuthScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Background Image
            Image(
                painter = painterResource(Res.drawable.login_signup_dark),
                contentDescription = null, // Provide content description if needed
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 55.dp, start = 25.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    "NO MORE EXCUSES | ",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp, // TextUnit.Sp
                        color = Color.White
                    )
                )

                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .background(Color.Red.copy(0.9f), shape = RoundedCornerShape(10.dp))
                ) {
                    Text(
                        "GET STARTED",
                        modifier = Modifier.padding(4.dp),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color.Black
                        )
                    )
                }

                Text(
                    "Embark on your fitness journey with AvikFitness. Our programs, guidance, and facilities ensure you achieve your goals.",
                    modifier = Modifier.padding(top = 10.dp, end = 25.dp),
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp, // TextUnit.Sp
                        color = Color.White.copy(alpha = 0.8f)
                    )
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    // Login Button
                    Button(onClick = {
                        navigator?.push(LoginScreen())
                    },shape = RoundedCornerShape(10.dp),
                        colors = buttonColors(
                            backgroundColor = colors.primary.copy(alpha = 0.9f),
                            contentColor = Color.Black
                        )

                    ) {
                        Text("Login")
                    }

                    // SignUp Button
                    Button(onClick = {
                        navigator?.push(SignUpScreen())
                    },shape = RoundedCornerShape(10.dp),
                        colors = buttonColors(
                            backgroundColor = colors.primary.copy(alpha = 0.9f),
                            contentColor = Color.Black
                        )

                    ) {
                        Text("SignUp")
                    }
                }
            }
        }
    }
}