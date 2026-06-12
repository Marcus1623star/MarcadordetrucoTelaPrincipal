package com.example.marcadordetrucotelaprincipal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoricodeJogos : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historicode_jogos)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        carregarHistorico()
    }

    private fun carregarHistorico() {
        val tvLista = findViewById<TextView>(R.id.tvListaPartidas)
        val database = AppDatabase.getDatabase(this)

        lifecycleScope.launch {
            val listaPartidas = database.partidaDao().buscarTodas()

            if (listaPartidas.isEmpty()) {
                tvLista.text = "Nenhuma partida registrada."
            } else {
                val textoExibicao = StringBuilder()
                val formatoData = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

                for (partida in listaPartidas) {
                    val dataFormatada = formatoData.format(Date(partida.data))

                    // LÓGICA DE DESTAQUE DO GANHADOR
                    val p1 =
                        if (partida.pontos1 > partida.pontos2) "⭐ *${partida.jogador1}*🏆" else partida.jogador1
                    val p2 =
                        if (partida.pontos2 > partida.pontos1) "⭐ *${partida.jogador2}*🏆" else partida.jogador2

                    textoExibicao.append("📅 $dataFormatada\n")
                    textoExibicao.append(" $p1 (${partida.pontos1}) x (${partida.pontos2}) $p2\n")
                    textoExibicao.append("---------------------------\n\n")
                }

                tvLista.text = textoExibicao.toString()
            }
        }
    }


    fun btvoltar(view: View) {
        finish()
    }

    fun btcompartilhar(view: View) {
            val tvLista = findViewById<TextView>(R.id.tvListaPartidas)
            val textoParaCompartilhar = tvLista.text.toString()

            // Verifica se não está vazio e se não é a mensagem padrão de erro
            if (textoParaCompartilhar.isNotEmpty() && !textoParaCompartilhar.contains("Nenhuma partida")) {

                // Aqui ele já pega o texto com as estrelas e negritos que fizemos acima
                val mensagemCompleta =
                    "🏆*CONFIRA MINHAS VITÓRIAS NO TRUCO!! *🏆\n\n$textoParaCompartilhar"

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, "Minhas Vitórias no Truco!!")
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    mensagemCompleta
                ) // Usando a mensagem com o título
                startActivity(Intent.createChooser(intent, "Compartilhar via"))
            }
        }
    }