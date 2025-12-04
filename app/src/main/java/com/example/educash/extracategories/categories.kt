package com.example.educash.extracategories

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.educash.R
import com.example.educash.service.categoriaApi
import kotlinx.coroutines.launch

class Categories : AppCompatActivity() {

    private lateinit var layoutPadre: LinearLayout
    private lateinit var tituloCategoria: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        layoutPadre = findViewById(R.id.llCategorias)

        val btnRegresar = findViewById<ImageButton>(R.id.btnRegresarInicio)
        btnRegresar.setOnClickListener { finish() }

        val categoriaId = intent.getIntExtra("categoriaId", 0)
        tituloCategoria = intent.getStringExtra("categoriaNombre") ?: "Categoría"
        findViewById<TextView>(R.id.txtTituloCategoria).text = tituloCategoria

        if (categoriaId == 0) {
            Toast.makeText(this, "Error: categoría inválida", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadModulos(categoriaId)
    }

    private fun loadModulos(id: Int) {
        lifecycleScope.launch {
            try {
                val response = categoriaApi.getModulos(id)

                if (response.isSuccessful && response.body() != null) {

                    val listaModulos = response.body()!!.data

                    if (listaModulos.isEmpty()) {
                        Toast.makeText(this@Categories, "No hay módulos", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    listaModulos.forEach { modulo ->
                        val nuevoItem = LayoutInflater.from(this@Categories)
                            .inflate(R.layout.item_modulo, layoutPadre, false)

                        nuevoItem.findViewById<TextView>(R.id.txtNombreModulo).text = modulo.nombre

                        val img = nuevoItem.findViewById<ImageView>(R.id.imgModulo)
                        val resourceId = resources.getIdentifier(modulo.imagen, "drawable", packageName)
                        if (resourceId != 0) img.setImageResource(resourceId)

                        nuevoItem.setOnClickListener {
                            val intent = Intent(this@Categories, com.example.educash.subtitle.subtitle::class.java)
                            intent.putExtra("categoriaNombre", tituloCategoria)
                            intent.putExtra("moduloNombre", modulo.nombre)
                            intent.putExtra("moduloDescripcion", modulo.descripcion)
                            intent.putExtra("moduloImagen", modulo.imagen)
                            intent.putExtra("moduloId", modulo.id)


                            println("👉 Enviando a subtitle:")
                            println("categoriaNombre = $tituloCategoria")
                            println("moduloNombre = ${modulo.nombre}")
                            println("moduloDescripcion = ${modulo.descripcion}")
                            println("moduloImagen = ${modulo.imagen}")
                            println("moduloId = ${modulo.id}")

                            startActivity(intent)

                        }

                        layoutPadre.addView(nuevoItem)
                    }

                } else {
                    Toast.makeText(this@Categories, "No se pudieron cargar los módulos", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(this@Categories, "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
