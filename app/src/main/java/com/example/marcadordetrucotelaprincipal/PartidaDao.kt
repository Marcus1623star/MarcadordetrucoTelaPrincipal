package com.example.marcadordetrucotelaprincipal

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PartidaDao {
    @Insert
    suspend fun salvar(partida: Partida)

    @Query("SELECT * FROM partidas ORDER BY data DESC")
    suspend fun buscarTodas(): List<Partida>
    @Query("DELETE FROM partidas")
    suspend fun apagaTudo()
}