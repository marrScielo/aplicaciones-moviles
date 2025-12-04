package com.example.educash.subtitle

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.educash.R
import com.example.educash.infomodulos.informacion
import com.example.educash.infomodulos.practica
import com.example.educash.entidad.ModuloDetalle
import com.example.educash.service.categoriaApi
import kotlinx.coroutines.launch

class subtitle : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private var moduloId: Int = 0
    private var listaDetalles = emptyList<ModuloDetalle>() // 🔥 donde se guardará la data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subtitle)

        val btnRegresar = findViewById<ImageButton>(R.id.btnRegresarInicio)
        btnRegresar.setOnClickListener { finish() }

        imageView = findViewById(R.id.imageViewModulo)

        // Obtener datos recibidos
        val tituloCategoria = intent.getStringExtra("categoriaNombre") ?: "Categoría"
        val moduloNombre = intent.getStringExtra("moduloNombre") ?: "Subtítulo"
        val moduloDescripcion = intent.getStringExtra("moduloDescripcion") ?: "Sin información"
        val moduloImagen = intent.getStringExtra("moduloImagen") ?: ""
        moduloId = intent.getIntExtra("moduloId", 0)

        // Cargar imagen del módulo
        val resourceId = resources.getIdentifier(moduloImagen.trim(), "drawable", packageName)
        if (resourceId != 0) {
            imageView.setImageResource(resourceId)
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_background)
            println("❌ No se encontró la imagen en drawable: $moduloImagen")
        }

        // Textos
        findViewById<TextView>(R.id.textView5).text = tituloCategoria
        findViewById<TextView>(R.id.textView7).text = moduloNombre
        findViewById<TextView>(R.id.textView9).text = moduloDescripcion

        // Cargar detalles del módulo desde la API
        loadDetalle()

        findViewById<TextView>(R.id.tabInformacion).setOnClickListener {
            abrirInformacion()
            selectTab(true)
        }

        findViewById<TextView>(R.id.tabPractica).setOnClickListener {
            replaceFragment(practica())
            selectTab(false)
        }
    }

    // 🔥 Llama a la API
    private fun loadDetalle() {
        lifecycleScope.launch {
            val response = categoriaApi.getModuloDetalles(moduloId)
            if (response.isSuccessful && response.body()?.ok == true) {
                listaDetalles = response.body()!!.data

                if (listaDetalles.isNotEmpty()) {
                    val primeraImagen = listaDetalles[0].imagen.trim()
                    val resId = resources.getIdentifier(primeraImagen, "drawable", packageName)
                    if (resId != 0) {
                        imageView.setImageResource(resId)
                    } else {
                        println("❌ Imagen del detalle no encontrada: $primeraImagen")
                    }
                }

                // Cargar automáticamente la pestaña de Información después de recibir datos
                abrirInformacion()
                selectTab(true)
            } else {
                println("❌ Error obteniendo detalles del módulo")
            }
        }
    }

    // 📌 Enviar la lista de detalles al fragment Información
    private fun abrirInformacion() {
        val fragment = informacion()
        val bundle = Bundle()
        bundle.putInt("moduloId", moduloId)
        bundle.putParcelableArrayList("detalles", ArrayList(listaDetalles))
        fragment.arguments = bundle
        replaceFragment(fragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedorFragment, fragment)
            .commit()
    }

    private fun selectTab(isInfoSelected: Boolean) {
        val tabInfo = findViewById<TextView>(R.id.tabInformacion)
        val tabPractica = findViewById<TextView>(R.id.tabPractica)

        if (isInfoSelected) {
            tabInfo.paint.isUnderlineText = true
            tabInfo.setTypeface(null, android.graphics.Typeface.BOLD)
            tabPractica.paint.isUnderlineText = false
            tabPractica.setTypeface(null, android.graphics.Typeface.NORMAL)
        } else {
            tabPractica.paint.isUnderlineText = true
            tabPractica.setTypeface(null, android.graphics.Typeface.BOLD)
            tabInfo.paint.isUnderlineText = false
            tabInfo.setTypeface(null, android.graphics.Typeface.NORMAL)
        }
    }
}
