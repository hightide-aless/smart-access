// MarcacionViewModel.kt
class MarcacionViewModel(
    private val authRepository: AuthRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _personalList = MutableLiveData<List<Personal>>()
    val personalList: LiveData<List<Personal>> = _personalList

    private val _marcacionState = MutableLiveData<MarcacionState>()
    val marcacionState: LiveData<MarcacionState> = _marcacionState

    private var pendingPersonalId: Int = -1
    private var pendingTipo: String = ""

    fun cargarPersonal() {
        viewModelScope.launch {
            try {
                val response = authRepository.obtenerPersonal()
                if (response.isSuccessful) {
                    _personalList.value = response.body() ?: emptyList()
                } else {
                    _marcacionState.value = MarcacionState.Error("Error al cargar personal")
                }
            } catch (e: Exception) {
                _marcacionState.value = MarcacionState.Error(ErrorHandler.handleNetworkError(e))
            }
        }
    }

    fun prepararMarcacion(personalId: Int, tipo: String) {
        pendingPersonalId = personalId
        pendingTipo = tipo
    }

    fun confirmarMarcacion() {
        if (pendingPersonalId == -1 || pendingTipo.isEmpty()) {
            _marcacionState.value = MarcacionState.Error("No hay marcación pendiente")
            return
        }

        _marcacionState.value = MarcacionState.Loading

        viewModelScope.launch {
            try {
                val userId = preferencesManager.getUserId()
                val response = authRepository.registrarMarcacion(
                    personalId = pendingPersonalId,
                    usuarioId = userId,
                    tipo = pendingTipo
                )

                if (response.isSuccessful && response.body()?.success == true) {
                    _marcacionState.value = MarcacionState.Success(
                        "Marcación de ${if (pendingTipo == Constants.TIPO_ENTRADA) "entrada" else "salida"} registrada"
                    )
                    // Reset pending
                    pendingPersonalId = -1
                    pendingTipo = ""
                } else {
                    _marcacionState.value = MarcacionState.Error(
                        response.body()?.message ?: "Error al registrar marcación"
                    )
                }
            } catch (e: Exception) {
                _marcacionState.value = MarcacionState.Error(ErrorHandler.handleNetworkError(e))
            }
        }
    }
}

sealed class MarcacionState {
    object Idle : MarcacionState()
    object Loading : MarcacionState()
    data class Success(val message: String) : MarcacionState()
    data class Error(val message: String) : MarcacionState()
}