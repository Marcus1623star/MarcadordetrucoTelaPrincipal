package com.example.marcadordetrucotelaprincipal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat



private lateinit var etNome1: TextView
private lateinit var etNome2: TextView


class nomesjogadores : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nomesjogadores)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowCompat.getInsetsController(window, v).isAppearanceLightStatusBars = false
            insets
        }

        etNome1=findViewById(R.id.etNome1)
        etNome2=findViewById(R.id.etNome2)
    }

      fun btJogar(view: View) {
val intent= Intent(this, MainActivity::class.java)
        intent.putExtra("nome1",etNome1.text.toString())
          intent.putExtra("nome2",etNome2.text.toString())

          startActivity(intent)
          finish()

      }
}