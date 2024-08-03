package presentation.screens.profile

actual fun formatBmi(bmi: Float): String {
    return "%.2f".format(bmi)
}
