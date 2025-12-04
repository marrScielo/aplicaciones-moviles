package com.example.educash.question_answer

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.educash.R
import com.example.educash.entidad.PreguntaDTO
import com.example.educash.service.ApiClient
import kotlinx.coroutines.launch

class question_answer : AppCompatActivity() {

    private lateinit var tvTema: TextView
    private lateinit var tvProgreso: TextView
    private lateinit var tvPregunta: TextView
    private lateinit var tvIndicacion: TextView
    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var progressTimer: ProgressBar

    private var timer: CountDownTimer? = null
    private var tiempoLimite = 10000L // 10 segundos

    private var lista: List<PreguntaDTO> = emptyList()
    private var idx = 0
    private var moduloId = 1
    private var temaNombre = "Tema"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_question_answer)

        tvTema = findViewById(R.id.tvTema)
        tvProgreso = findViewById(R.id.tvProgreso)
        tvPregunta = findViewById(R.id.tvPregunta)
        tvIndicacion = findViewById(R.id.tvIndicacion)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        progressTimer = findViewById(R.id.progressTimer)
        moduloId = intent.getIntExtra("moduloId", 1)
        temaNombre = intent.getStringExtra("temaNombre") ?: "Tema"
        tvTema.text = temaNombre

        lifecycleScope.launch {
            try {
                val resp = ApiClient.quizApi.getQuiz(moduloId)
                if (!resp.isSuccessful) {
                    Toast.makeText(this@question_answer, "HTTP ${resp.code()}", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                lista = resp.body()?.data.orEmpty()

                if (lista.isEmpty()) {
                    Toast.makeText(this@question_answer, "No hay preguntas", Toast.LENGTH_SHORT).show()
                    finish(); return@launch
                }

                mostrar(0)

            } catch (e: Exception) {
                Toast.makeText(this@question_answer, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        btnA.setOnClickListener { verificar(0, btnA) }
        btnB.setOnClickListener { verificar(1, btnB) }
        btnC.setOnClickListener { verificar(2, btnC) }
    }

    private fun mostrar(i: Int) {
        idx = i
        val p = lista[i]

        tvProgreso.text = "Pregunta ${p.nro}/${lista.size}"
        tvPregunta.text = p.enunciado

        btnA.text = p.opciones.getOrNull(0)?.texto ?: ""
        btnB.text = p.opciones.getOrNull(1)?.texto ?: ""
        btnC.text = p.opciones.getOrNull(2)?.texto ?: ""

        listOf(btnA, btnB, btnC).forEach {
            it.isEnabled = true
            it.setBackgroundColor(Color.parseColor("#E5E6FD"))
            it.alpha = 1f
        }

        // Reinicia barra y timer
        progressTimer.progress = 100
        timer?.cancel()
        timer = object : CountDownTimer(tiempoLimite, 100) {
            override fun onTick(millisUntilFinished: Long) {
                val porcentaje = (millisUntilFinished * 100 / tiempoLimite).toInt()
                progressTimer.progress = porcentaje
                tvIndicacion.text = "¡Tiempo restante: ${millisUntilFinished / 1000} s!"
            }

            override fun onFinish() {
                progressTimer.progress = 0
                Toast.makeText(this@question_answer, "⏳ Tiempo agotado", Toast.LENGTH_SHORT).show()
                avanzar()
            }
        }.start()
    }

    private fun verificar(indiceOpcion: Int, botonPresionado: Button) {
        timer?.cancel()
        val p = lista[idx]
        val opcion = p.opciones.getOrNull(indiceOpcion) ?: return

        listOf(btnA, btnB, btnC).forEach { it.isEnabled = false }

        if (opcion.es_correcta == true) {
            botonPresionado.setBackgroundColor(Color.parseColor("#4CAF50"))
        } else {
            botonPresionado.setBackgroundColor(Color.parseColor("#F44336"))
        }

        botonPresionado.postDelayed({
            avanzar()
        }, 1000)
    }

    private fun avanzar() {
        val next = idx + 1
        if (next < lista.size) {
            mostrar(next)
        } else {
            Toast.makeText(this, "🎉 ¡Quiz finalizado!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
