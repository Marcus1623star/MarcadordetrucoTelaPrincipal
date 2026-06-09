package com.example.marcadordetrucotelaprincipal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.ImageView
import android.media.SoundPool
import android.media.AudioAttributes
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var tvNomeJ1: TextView
    private lateinit var tvNomeJ2: TextView

    private lateinit var tvplacar1: TextView
    private lateinit var tvplacar2: TextView
    private var pontosJogador1 = 0
    private var pontosJogador2 = 0
    private var jogoEncerrado = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)

            WindowCompat.getInsetsController(window, v).isAppearanceLightStatusBars = false
            insets
        }
//tvNome Jogador
        tvNomeJ1=findViewById(R.id.tvNomeJ1)
        tvNomeJ1.text=intent.getStringExtra("nome1".toString())

        tvNomeJ2=findViewById(R.id.tvNomeJ2)
        tvNomeJ2.text=intent.getStringExtra("nome2".toString())


        // tv Placar
        tvplacar1 = findViewById(R.id.tvplacar1)

        tvplacar2 = findViewById(R.id.tvplacar2)

        atualizarPlacar()
    }

    private fun atualizarPlacar() {
        tvplacar1.text = "$pontosJogador1"
        tvplacar2.text = "$pontosJogador2"

        // Verificar vencedor
        verificarVencedor()
    }

    private fun verificarVencedor() {
        if (pontosJogador1 >= 12 || pontosJogador2 >= 12) {
            jogoEncerrado = true
            val vencedor = if (pontosJogador1 >= 12) tvNomeJ1.text.toString() else tvNomeJ2.text.toString()

            // SALVAR NO BANCO DE DADOS
            val database = AppDatabase.getDatabase(this)
            lifecycleScope.launch {
                val novaPartida = Partida(
                    jogador1 = tvNomeJ1.text.toString(),
                    jogador2 = tvNomeJ2.text.toString(),
                    pontos1 = pontosJogador1,
                    pontos2 = pontosJogador2
                )
                database.partidaDao().salvar(novaPartida)
            }

            mostrartelavitoria(vencedor)
        }
    }
//mostrar imagen trofeu  com som
    private fun mostrartelavitoria(vencedor: String) {
        // Configuração da Imagem
        val imagemVitoria = ImageView(this)
        imagemVitoria.setImageResource(R.drawable.trofeu1)
        imagemVitoria.adjustViewBounds = true
        imagemVitoria.setPadding(0, 30, 0, 0)
        imagemVitoria.scaleX=0f
        imagemVitoria.scaleY=0f
        imagemVitoria.animate()
            .scaleX(1.2f)
            .scaleY(1.2f)
            .setDuration(1000)
            .withEndAction {
                imagemVitoria.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(600)
                    .start()
            }
            .start()

        // Configuração do Áudio
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()

        // Carregar e Tocar o Som
        soundPool.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0) {
                soundPool.play(sampleId, 1f, 1f, 1, 0, 1f)
            }
        }
        soundPool.load(this, R.raw.rockyvencer, 1)

        // Criação do Alerta
        val builder = AlertDialog.Builder(this)
        builder.setTitle("FIM DE JOGO!")
        builder.setMessage("Parabéns $vencedor, você venceu a partida!")
        builder.setView(imagemVitoria)
        builder.setCancelable(false)
        builder.setPositiveButton("Nova Partida") { _, _ ->
            soundPool.release() // LIBERAR A MEMÓRIA ao clicar em nova partida
            reiniciarplacar()
        }

        val dialog = builder.create()
        dialog.show()
    }

    fun reiniciarplacar() {
        pontosJogador1 = 0
        pontosJogador2 = 0
        jogoEncerrado = false
        atualizarPlacar()
    }

    // Botões do Jogador 1
    fun bt1J1(view: View) {
        if (jogoEncerrado) return
        pontosJogador1 += 1
        atualizarPlacar()
    }

    fun bt3J1(view: View) {
        if (jogoEncerrado) return
        pontosJogador1 += 3
        atualizarPlacar()
    }

    fun bt6J1(view: View) {
        if (jogoEncerrado) return
        pontosJogador1 += 6
        atualizarPlacar()
    }

    fun bt9J1(view: View) {
        if (jogoEncerrado) return
        pontosJogador1 += 9
        atualizarPlacar()
    }

    fun bt12J1(view: View) {
        if (jogoEncerrado) return
        pontosJogador1 += 12
        atualizarPlacar()
    }

    fun btvolt1(view: View) {
        if (jogoEncerrado) return
        if (pontosJogador1 > 0) {
            pontosJogador1 -= 1
            atualizarPlacar()
        }
    }

    // Botões do Jogador 2
    fun bt1J2(view: View) {
        if (jogoEncerrado) return
        pontosJogador2 += 1
        atualizarPlacar()
    }

    fun bt3J2(view: View) {
        if (jogoEncerrado) return
        pontosJogador2 += 3
        atualizarPlacar()
    }

    fun bt6J2(view: View) {
        if (jogoEncerrado) return
        pontosJogador2 += 6
        atualizarPlacar()
    }

    fun bt9J2(view: View) {
        if (jogoEncerrado) return
        pontosJogador2 += 9
        atualizarPlacar()
    }

    fun bt12J2(view: View) {
        if (jogoEncerrado) return
        pontosJogador2 += 12
        atualizarPlacar()
    }

    fun btvolt2(view: View) {
        if (jogoEncerrado) return
        if (pontosJogador2 > 0) {
            pontosJogador2 -= 1
            atualizarPlacar()
        }
    }

    // Outros botões
    // Atualize a função btZH na sua MainActivity.kt
    fun btZH(view: View) {
        // Cria um alerta para confirmar se o usuário quer mesmo apagar
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Zerar Histórico")
        builder.setMessage("Deseja apagar todas as partidas salvas?")

        builder.setPositiveButton("Sim") { _, _ ->
            // Pega a instância do banco de dados
            val database = AppDatabase.getDatabase(this)

            // Usa o lifecycleScope para apagar em segundo plano
            lifecycleScope.launch {
                database.partidaDao().apagaTudo()
                // Opcional: mostrar um aviso que foi apagado
                android.widget.Toast.makeText(this@MainActivity, "Histórico apagado!", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancelar", null)

        val dialog = builder.create()
        dialog.show()
    }

    fun btHistoric(view: View) {
        val intent= Intent(this, HistoricodeJogos::class.java)
        startActivity(intent)
    }

    fun btnomes(view: View) {
        val intent= Intent(this, nomesjogadores::class.java)
        startActivity(intent)
    }

    fun btSair(view: View) {
        finishAffinity()
    }
}
