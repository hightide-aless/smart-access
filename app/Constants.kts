object Constants {
    // Cambia por la IP de tu computadora (donde corre el backend)
    const val BASE_URL = "http://192.168.1.100:8000"

    // Endpoints
    const val ENDPOINT_LOGIN = "/login"
    const val ENDPOINT_PERSONAL = "/personal"
    const val ENDPOINT_MARCAR = "/marcar"
    const val ENDPOINT_MARCACIONES_HOY = "/marcaciones/hoy"

    // SharedPreferences
    const val PREFS_NAME = "app_prefs"
    const val KEY_AUTH_TOKEN = "auth_token"
    const val KEY_USER_DATA = "user_data"

    // Timeouts
    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L
}