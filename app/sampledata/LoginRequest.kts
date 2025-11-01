// LoginRequest.kt
data class LoginRequest(
    val username: String,
    val password: String
)

// LoginResponse.kt
data class LoginResponse(
    val success: Boolean,
    val token: String?,
    val user: UsuarioResponse?,
    val message: String?
)

// UsuarioResponse.kt
data class UsuarioResponse(
    val id: Int,
    val username: String,
    val rol: String,
    val nombres: String,
    val apellidos: String,
    val activo: Boolean
)

// PersonalResponse.kt
data class PersonalResponse(
    val id: Int,
    val grado: String,
    val nombres: String,
    val apellidos: String,
    val cedula: String,
    val unidad: String?,
    val cargo: String?,
    val estado: Boolean
)

// MarcacionRequest.kt
data class MarcacionRequest(
    val personal_id: Int,
    val tipo: String,
    val ubicacion: String = "Puerta Principal"
)

// ApiResponse.kt
data class ApiResponse(
    val success: Boolean,
    val message: String,
    val data: Any?
)

// MarcacionHoyResponse.kt
data class MarcacionHoyResponse(
    val success: Boolean,
    val data: List<MarcacionData>
)

data class MarcacionData(
    val id: Int,
    val personal_nombre: String,
    val centinela: String,
    val tipo: String,
    val fecha_hora: String,
    val ubicacion: String
)