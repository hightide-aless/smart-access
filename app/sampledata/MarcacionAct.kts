// MarcacionActivity.kt - Para centinelas
class MarcacionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMarcacionBinding
    private val viewModel: MarcacionViewModel by viewModels()
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarcacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBiometricPrompt()
        setupObservers()
        loadPersonal()
    }

    private fun setupBiometricPrompt() {
        biometricPrompt = BiometricPrompt(
            this,
            ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // La huella del centinela es válida, registrar marcación
                    viewModel.confirmarMarcacion()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    binding.tvStatus.text = "Error de autenticación: $errString"
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Confirmar Marcación")
            .setSubtitle("Use su huella para confirmar la marcación")
            .setNegativeButtonText("Cancelar")
            .setConfirmationRequired(true)
            .build()
    }

    private fun setupObservers() {
        viewModel.personalList.observe(this) { personal ->
            // Actualizar RecyclerView o Spinner con lista de personal
            updatePersonalList(personal)
        }

        viewModel.marcacionState.observe(this) { state ->
            when (state) {
                is MarcacionState.Success -> {
                    binding.tvStatus.text = state.message
                    binding.progressBar.visibility = View.GONE
                    resetForm()
                }
                is MarcacionState.Error -> {
                    binding.tvStatus.text = state.message
                    binding.progressBar.visibility = View.GONE
                }
                is MarcacionState.Loading -> {
                    binding.tvStatus.text = "Registrando marcación..."
                    binding.progressBar.visibility = View.VISIBLE
                }
                else -> {}
            }
        }
    }

    private fun loadPersonal() {
        viewModel.cargarPersonal()
    }

    fun onRegistrarEntradaClick(view: View) {
        val personalSeleccionado = obtenerPersonalSeleccionado()
        if (personalSeleccionado == null) {
            binding.tvStatus.text = "Seleccione un personal"
            return
        }

        viewModel.prepararMarcacion(personalSeleccionado.id, Constants.TIPO_ENTRADA)
        biometricPrompt.authenticate(promptInfo)
    }

    fun onRegistrarSalidaClick(view: View) {
        val personalSeleccionado = obtenerPersonalSeleccionado()
        if (personalSeleccionado == null) {
            binding.tvStatus.text = "Seleccione un personal"
            return
        }

        viewModel.prepararMarcacion(personalSeleccionado.id, Constants.TIPO_SALIDA)
        biometricPrompt.authenticate(promptInfo)
    }

    private fun resetForm() {
        // Limpiar selección después de 2 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            binding.tvStatus.text = "Seleccione personal y acción"
        }, 2000)
    }
}