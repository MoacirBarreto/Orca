package devandroid.moacir.orca.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

// @Entity informa ao Room que esta classe representa uma tabela no banco de dados.
@Entity(tableName = "orcamentos")
data class Orcamento(
    // @PrimaryKey define a chave primária da tabela.
    // autoGenerate = true faz com que o Room gere o ID automaticamente.
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Outros campos que um orçamento pode ter.
    // Adapte estes campos para o seu projeto.
    val nome: String,          // Ex: "Alimentação", "Transporte"
    val valorPlanejado: Double,
    val dataInicio: Date,
    val dataFim: Date
)
