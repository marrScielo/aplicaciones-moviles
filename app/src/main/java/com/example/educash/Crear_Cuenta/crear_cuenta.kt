package com.example.educash.Crear_Cuenta

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.educash.MainActivity
import com.example.educash.R
import com.example.educash.entidad.Usuario
import com.example.educash.service.WebService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.example.educash.entidad.RegisterRequest
import kotlinx.coroutines.withContext
import com.example.educash.service.authApi


class crear_cuenta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_cuenta)

        // ----------- VISTAS DEL XML -----------
        val btnRegresar: ImageButton = findViewById(R.id.btnRegresarInicio)
        val btnCrear: Button = findViewById(R.id.btnIniciar)

        val etNombre: EditText = findViewById(R.id.etNombre)
        val etCorreo: EditText = findViewById(R.id.etCorreo)
        val etPassword: EditText = findViewById(R.id.etContrasena)


        // ----------- BOTÓN CREAR CUENTA -----------
        btnCrear.setOnClickListener {

            val nombre = etNombre.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (nombre.isEmpty() || correo.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // LLAMADA A LA API
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    // Crear el request: adapta los nombres de campo si tu RegisterRequest es distinto
                    val registerReq = RegisterRequest(
                        email = correo,
                        password = password,
                        nombreyapellido = nombre
                    )

                    // Llamada correcta al servicio
                    val resp = authApi.register(registerReq)

                    // Volver al hilo UI para mostrar toasts / navegar
                    withContext(Dispatchers.Main) {
                        if (resp.isSuccessful) {
                            val body = resp.body()
                            // WebService.register devuelve Response<Map<String, Any>> según tu WebService.kt
                            // Intentamos leer la clave "ok" o "token"
                            var ok = false
                            if (body is Map<*, *>) {
                                ok = (body["ok"] as? Boolean) ?: false
                            }
                            if (ok) {
                                Toast.makeText(this@crear_cuenta, "Cuenta creada correctamente", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@crear_cuenta, MainActivity::class.java))
                                finish()
                            } else {
                                // si el backend envía msg o error
                                val message = when {
                                    body is Map<*, *> && body["msg"] is String -> body["msg"] as String
                                    else -> "Error al crear cuenta"
                                }
                                Toast.makeText(this@crear_cuenta, message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@crear_cuenta, "Error: ${resp.code()} ${resp.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@crear_cuenta, "No se pudo conectar al servidor: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }


        // ----------- BOTÓN REGRESAR -----------
        btnRegresar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
