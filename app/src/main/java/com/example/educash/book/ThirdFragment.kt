package com.example.educash.book

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.educash.R
import com.example.educash.entidad.Categoria
import com.example.educash.extracategories.Categories
import com.example.educash.service.categoriaApi
import kotlinx.coroutines.launch

class ThirdFragment : Fragment() {

    private lateinit var itemGastos: LinearLayout
    private lateinit var itemAhorro: LinearLayout
    private lateinit var itemInversion: LinearLayout
    private lateinit var itemDeuda: LinearLayout

    private lateinit var imgGastos: ImageView
    private lateinit var imgAhorro: ImageView
    private lateinit var imgInversion: ImageView
    private lateinit var imgDeuda: ImageView

    private var categoriasList: List<Categoria> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views here
        itemGastos = view.findViewById(R.id.itemGastos)
        itemAhorro = view.findViewById(R.id.itemAhorro)
        itemInversion = view.findViewById(R.id.itemInversion)
        itemDeuda = view.findViewById(R.id.itemDeuda)
        imgGastos = view.findViewById(R.id.imgGastos)
        imgAhorro = view.findViewById(R.id.imgAhorro)
        imgInversion = view.findViewById(R.id.imgInversion)
        imgDeuda = view.findViewById(R.id.imgDeuda)

        loadCategorias()
    }

    private fun loadCategorias() {
        lifecycleScope.launch {
            try {
                val response = categoriaApi.getCategorias()
                if (response.isSuccessful && response.body() != null) {
                    categoriasList = response.body()!!.data

                    if (categoriasList.size >= 4) {
                        setClickActions()
                    }
                    cargarImagenes()
                } else {
                    Toast.makeText(requireContext(), "Error obteniendo categorías", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setClickActions() {
        val gastos = categoriasList.firstOrNull { it.nombre.equals("Gastos \nresponsables", ignoreCase = true) }
        val ahorro = categoriasList.firstOrNull { it.nombre.equals("Ahorro y \npresupuesto", ignoreCase = true) }
        val inversion = categoriasList.firstOrNull { it.nombre.equals("Inversión\nbásica", ignoreCase = true) }
        val deuda = categoriasList.firstOrNull { it.nombre.equals("Deudas y \ncrédito", ignoreCase = true) }

        itemGastos.setOnClickListener { gastos?.let { openCategoria(it.id, it.nombre) } }
        itemAhorro.setOnClickListener { ahorro?.let { openCategoria(it.id, it.nombre) } }
        itemInversion.setOnClickListener { inversion?.let { openCategoria(it.id, it.nombre) } }
        itemDeuda.setOnClickListener { deuda?.let { openCategoria(it.id, it.nombre) } }
    }

    private fun cargarImagenes() {
        categoriasList.forEach { categoria ->
            val resID = resources.getIdentifier(categoria.imagen, "drawable", requireContext().packageName)

            when (categoria.nombre.trim()) {
                "Gastos responsables" -> imgGastos.setImageResource(resID)
                "Ahorro y presupuesto" -> imgAhorro.setImageResource(resID)
                "Inversión básica" -> imgInversion.setImageResource(resID)
                "Deudas y crédito" -> imgDeuda.setImageResource(resID)
            }
        }
    }

    private fun openCategoria(id: Int, nombre: String) {
        val intent = Intent(requireContext(), Categories::class.java)
        intent.putExtra("categoriaId", id)
        intent.putExtra("categoriaNombre", nombre)
        startActivity(intent)
    }
}
