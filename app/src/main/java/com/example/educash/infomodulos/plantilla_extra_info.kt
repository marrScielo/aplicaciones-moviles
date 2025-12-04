package com.example.educash.infomodulos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.educash.databinding.FragmentPlantillaExtraInfoBinding
import com.example.educash.service.categoriaApi
import kotlinx.coroutines.launch

// === START: CLASES TEMPORALES PARA RESOLVER EL ERROR DE REFERENCIA ===
// NOTA: Si ya tienes estas clases definidas en 'com.example.educash.entidad',
// borra estas definiciones y usa 'import com.example.educash.entidad.APIResponse'
data class ModuloInfo(
    val que_es: String?,
    val porque_importante: String?
)

data class APIResponse(
    val ok: Boolean,
    val data: ModuloInfo?
)
// === END: CLASES TEMPORALES PARA RESOLVER EL ERROR DE REFERENCIA ===


class plantilla_extra_info : Fragment() {

    // Usamos binding para acceder a las vistas del layout 'fragment_plantilla_extra_info.xml'
    private lateinit var binding: FragmentPlantillaExtraInfoBinding

    private var moduloId: Int = -1
    private var tipoInfo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recibimos los argumentos del Fragmento padre (informacion.kt)
        moduloId = arguments?.getInt("moduloId") ?: -1
        tipoInfo = arguments?.getString("tipoInfo")

        Log.d("EXTRA_INFO", "Cargando Extra Info. Modulo ID: $moduloId, Tipo: $tipoInfo")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlantillaExtraInfoBinding.inflate(inflater, container, false)

        // Botón de cierre para volver al Fragmento anterior
        binding.btnCerrar.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Llamamos a la función que hace la petición a la API y carga el texto
        cargarInfo()
        return binding.root
    }

    private fun cargarInfo() {
        if (moduloId <= 0) {
            binding.textTitulo.text = "ERROR"
            binding.textContenido.text = "Error: El ID del módulo no es válido ($moduloId)."
            return
        }

        // Muestra un mensaje de carga mientras espera la respuesta
        binding.textContenido.text = "Cargando información..."

        lifecycleScope.launch {
            try {
                // Hacemos la llamada a la API. Retrofit debe devolver Response<APIResponse>
                val response = categoriaApi.getModuloInfo(moduloId)

                if (response.isSuccessful) {
                    val apiResponse = response.body() // Obtenemos el objeto envoltorio

                    // Verificamos si la respuesta fue exitosa y si hay datos
                    if (apiResponse != null && apiResponse.ok && apiResponse.data != null) {
                        val data = apiResponse.data // data es de tipo ModuloInfo

                        // 1. Asignar el Título basado en el tipo de info
                        val titulo = when (tipoInfo) {
                            "que_es" -> "¿QUÉ ES?"
                            "porque_importante" -> "¿POR QUÉ ES IMPORTANTE?"
                            else -> "INFORMACIÓN EXTRA"
                        }

                        // 2. Extraer el Texto de la API basado en el tipo
                        val texto = when (tipoInfo) {
                            "que_es" -> data.que_es
                            "porque_importante" -> data.porque_importante
                            else -> "Información no disponible."
                        }

                        // ✅ ASIGNACIÓN FINAL: Usamos post{} para asegurar la renderización en la vista
                        binding.textTitulo.post {
                            binding.textTitulo.text = titulo
                            binding.textContenido.text = texto
                            binding.textContenido.textSize = 15f // Restaurar el tamaño original si es necesario
                        }
                    } else {
                        binding.textContenido.text = "Error del servidor: La respuesta no es válida o está incompleta."
                    }
                } else {
                    binding.textContenido.text = "Error ${response.code()} obteniendo información del servidor."
                }

            } catch (e: Exception) {
                binding.textContenido.text = "Error de conexión/Parseo: ${e.message}"
            }
        }
    }
}