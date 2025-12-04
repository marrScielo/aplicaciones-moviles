package com.example.educash.Iniciar_Sesion

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.educash.MainActivity
import com.example.educash.R
import com.example.educash.entidad.LoginRequest
import com.example.educash.service.authApi
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class inicio_sesion : AppCompatActivity() {

    private lateinit var etCorreo: TextInputEditText
    private lateinit var etContrasena: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio_sesion)

        etCorreo = findViewById(R.id.etCorreo)
        etContrasena = findViewById(R.id.etContrasena)

        findViewById<ImageButton>(R.id.btnRegresarInicio).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        findViewById<android.view.View>(R.id.btnIniciar).setOnClickListener {
            val email = etCorreo.text?.toString()?.trim().orEmpty()
            val pass  = etContrasena.text?.toString()?.trim().orEmpty()
            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Completa correo y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                try {
                    val resp = authApi.login(LoginRequest(email, pass))
                    if (resp.isSuccessful) {
                        val body = resp.body()
                        if (body?.ok == true && body.usuario != null) {
                            val nombre = body.usuario.nombreyapellido
                            val correo = body.usuario.email

                            saveUserData(nombre, correo)              // 👈 GUARDA NOMBRE Y EMAIL
                            body.token?.let { saveToken(it) }         // si tu API envía token

                            Toast.makeText(
                                this@inicio_sesion,
                                "¡Bienvenido, ${nombre ?: correo}!",
                                Toast.LENGTH_SHORT
                            ).show()

                            startActivity(Intent(this@inicio_sesion, com.example.educash.home.Home::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@inicio_sesion, body?.message ?: "Credenciales inválidas", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(this@inicio_sesion, "Error ${resp.code()}: ${resp.message()}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@inicio_sesion, "Error de red: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
    }
    private fun saveUserData(nombre: String?, email: String?) {
        getSharedPreferences("auth", MODE_PRIVATE)
            .edit()
            .putString("nombre", nombre ?: "")
            .putString("email", email ?: "")
            .apply()
    }


    private fun saveToken(token: String) {
        getSharedPreferences("auth", MODE_PRIVATE).edit().putString("token", token).apply()
    }
}
