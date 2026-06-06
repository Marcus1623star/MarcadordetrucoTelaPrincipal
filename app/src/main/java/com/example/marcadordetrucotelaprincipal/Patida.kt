package com.example.marcadordetrucotelaprincipal
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "partidas")
data class Partida(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val jogador1: String,
    val jogador2: String,
    val pontos1: Int,
    val pontos2: Int,
    val data: Long = System.currentTimeMillis()
)
