package presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import avikfitness.composeapp.generated.resources.Res
import avikfitness.composeapp.generated.resources.hide_eye_icon_filled
import avikfitness.composeapp.generated.resources.show_eye_icon_filled
import org.jetbrains.compose.resources.painterResource

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    errorMessage: String? = null,
    isPasswordTextField : Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: ImageVector? = null,
    singleLine: Boolean = true
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            colors = textFieldColors(
                textColor = Color.White,
                backgroundColor = Color.Transparent,
                cursorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
            ),
            trailingIcon = if (isPasswordTextField) {
                {
                    PasswordEyeIcon(
                        isPasswordVisible = isPasswordVisible,
                        onPasswordVisibilityToggle = {isPasswordVisible = !isPasswordVisible}
                    )
                }
            } else null,
            leadingIcon = leadingIcon?.let {
                {
                    Icon(
                        imageVector = it,
                        contentDescription = null
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = visualTransformation,
            isError = isError,
            singleLine = singleLine,

        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.error
            )
        }
    }
}
@Composable
fun PasswordEyeIcon(
    isPasswordVisible: Boolean,
    onPasswordVisibilityToggle: () -> Unit
) {

    val image = if (isPasswordVisible){
        painterResource(Res.drawable.show_eye_icon_filled)
    }else{
        painterResource(Res.drawable.hide_eye_icon_filled)
    }

    IconButton(onClick = onPasswordVisibilityToggle) {
        Icon(painter = image, contentDescription = null)
    }

}