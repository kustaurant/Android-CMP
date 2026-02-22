sealed interface UiError {
    data class Message(val value: String) : UiError
    data object Network : UiError
    data object Unauthorized : UiError
}
