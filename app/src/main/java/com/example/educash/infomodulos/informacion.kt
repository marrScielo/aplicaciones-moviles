package com.example.educash.infomodulos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.educash.R
import com.example.educash.entidad.ModuloDetalle
// Las importaciones de APIResponse y ModuloInfo ya no son necesarias aquí
// porque eliminamos la función que las usaba.


class informacion : Fragment() {

    private var listaDetalles: List<ModuloDetalle> = emptyList()
    private var moduloId: Int = -1
    private lateinit var textContenido: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listaDetalles = arguments?.getParcelableArrayList("detalles") ?: emptyList()
        moduloId = arguments?.getInt("moduloId") ?: -1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_informacion, container, false)

        textContenido = view.findViewById<TextView>(R.id.textContenido)
        val btnQueEs = view.findViewById<View>(R.id.btnQueEs)
        val btnImportancia = view.findViewById<View>(R.id.btnImportancia)

        // ✅ ASIGNACIÓN INICIAL (Mostrar solo la primera información)
        if (listaDetalles.isNotEmpty()) {
            textContenido.text = listaDetalles[0].contenido
        }

        // ✅ El botón ahora abre el fragmento de información extra.
        btnQueEs.setOnClickListener {
            abrirExtraInfo("que_es")
        }

        btnImportancia.setOnClickListener { abrirExtraInfo("porque_importante") }

        return view
    }

    // ** La función cargarInfoEnContenido ha sido eliminada. **

    private fun abrirExtraInfo(tipo: String) {
        val fragment = plantilla_extra_info().apply {
            arguments = Bundle().apply {
                putInt("moduloId", moduloId)
                putString("tipoInfo", tipo)
            }
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.contenedorFragment, fragment)
            .addToBackStack(null)
            .commit()
    }
}