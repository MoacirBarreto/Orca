package com.deveandroid.moacir.orca

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lancamentos") // Define o nome da tabela
data class Lancamento(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Chave primária que se auto-incrementa

    val valor: Double,

    val descricao: String,

    val natureza: String,

    val categoria: String,

    @ColumnInfo(name = "data_lancamento") // Nome da coluna no banco
    val dataLancamento: Long, // Salvaremos como timestamp (milisegundos)

    @ColumnInfo(name = "data_criacao")
    val dataCriacao: Long = System.currentTimeMillis() // Pega o tempo atual por padrão
)
