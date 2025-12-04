package com.example.educash.homee

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.educash.R
import androidx.navigation.fragment.findNavController



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



class homeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val tvNombre = view.findViewById<TextView>(R.id.txtNombre)
        val prefs = requireContext().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val nombre = prefs.getString("nombre", null) ?: "Usuario"
        tvNombre.text = " $nombre "

        // Obtener categorías desde la vista
        val catGastos = view.findViewById<View>(R.id.catGastos)
        val catAhorro = view.findViewById<View>(R.id.catAhorro)
        val catInversion = view.findViewById<View>(R.id.catInversion)
        val catDeudas = view.findViewById<View>(R.id.catDeudas)

        catGastos.setOnClickListener {
            abrirModulos(1, "Gastos \nresponsables")
        }

        catAhorro.setOnClickListener {
            abrirModulos(2, "Ahorro y \npresupuesto")
        }

        catInversion.setOnClickListener {
            abrirModulos(3, "Inversión\nbásica")
        }

        catDeudas.setOnClickListener {
            abrirModulos(4, "Deudas y \ncrédito")
        }

        return view
    }

    private fun abrirModulos(idCategoria: Int, nombreCategoria: String) {
        val intent = android.content.Intent(requireContext(), com.example.educash.extracategories.Categories::class.java)
        intent.putExtra("categoriaId", idCategoria)
        intent.putExtra("categoriaNombre", nombreCategoria)
        startActivity(intent)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment homeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            homeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}