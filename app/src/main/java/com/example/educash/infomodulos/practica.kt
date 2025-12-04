package com.example.educash.infomodulos

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.educash.R
import com.example.educash.question_answer.question_answer // Importar la Activity de destino

class practica : Fragment() {

    private var moduloId: Int = -1
    private var temaNombre: String? = null // Variable para guardar el nombre del módulo/tema

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Recibe los datos pasados desde la Activity principal (SubtitleActivity)
        moduloId = arguments?.getInt("moduloId") ?: -1
        temaNombre = arguments?.getString("temaNombre")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_practica, container, false)

        // 2. Inicializar las vistas del layout
        val btnIniciarPrueba = view.findViewById<Button>(R.id.btnIniciarPrueba)
        val tvModuloNombre = view.findViewById<TextView>(R.id.tvModuloNombre)

        // 3. Mostrar el nombre del módulo en el título del Quiz
        tvModuloNombre.text = "Prueba de ${temaNombre ?: "Módulo"}"

        // 4. Configurar el Click Listener para iniciar el Quiz
        btnIniciarPrueba.setOnClickListener {
            // Verificamos que tenemos los datos esenciales
            if (moduloId > 0 && !temaNombre.isNullOrEmpty()) {

                // Creamos el Intent para lanzar la Activity question_answer
                val intent = Intent(requireContext(), question_answer::class.java).apply {

                    putExtra("temaId", moduloId)
                    putExtra("temaNombre", temaNombre)
                    putExtra("moduloId", moduloId)
                }
                startActivity(intent)

            } else {
                tvModuloNombre.text = "Error: Módulo ID no válido para iniciar la prueba."
                // Opcional: Mostrar un Toast al usuario
                // Toast.makeText(requireContext(), "No se puede iniciar la prueba, faltan datos.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}