// LoginViewModel.kt (Extendido)
class LoginViewModel(
    private val authRepository: AuthRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    fun login(username: String, password: String) {
        _loginState.value = LoginState.Loading

        viewModelScope.launch {
            try {
                val response = authRepository.login(username, password)
                if (response.isSuccessful && response.body()?.success == true) {
                    val loginResponse = response.body()!!

                    // Guardar sesión
                    preferencesManager.saveSession(loginResponse.user!!, loginResponse.token!!)

                    // Determinar a qué actividad navegar basado en el rol
                    val destination = if (loginResponse.user.rol == Constants.ROLE_ADMIN) {
                        LoginDestination.ADMIN
                    } else {
                        LoginDestination.CENTINELA
                    }

                    _loginState.value = LoginState.Success(
                        message = "Login exitoso",
                        destination = destination
                    )
                } else {
                    _loginState.value = LoginState.Error(
                        response.body()?.message ?: "Error de autenticación"
                    )
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(ErrorHandler.handleNetworkError(e))
            }
        }
    }
}

// Nuevo estado para manejar destino después del login
sealed class LoginDestination {
    object ADMIN : LoginDestination()
    object CENTINELA : LoginDestination()
}

// LoginState extendido
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val message: String, val destination: LoginDestination) : LoginState()
    data class Error(val message: String) : LoginState()
}