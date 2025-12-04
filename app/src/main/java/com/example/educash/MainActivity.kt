package com.example.educash

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.educash.Crear_Cuenta.crear_cuenta
import com.example.educash.Iniciar_Sesion.inicio_sesion

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btn: Button = findViewById(R.id.btn_inicioSesion)

        btn.setOnClickListener {
            val intent = Intent(this, inicio_sesion::class.java)
            startActivity(intent)
        }
        val btn2: Button = findViewById(R.id.btn_crearCuenta)

        btn2.setOnClickListener {
            val intent = Intent(this, crear_cuenta::class.java)
            startActivity(intent)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
