package devandroid.moacir.orca.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "lancamentos", // Supondo o nome da tabela
    // Adicionar um índice na coluna da chave estrangeira melhora o desempenho das consultas.
    indices = [Index(value = ["orcamentoId"])],
    foreignKeys = [
        ForeignKey(
            entity = Orcamento::class,
            parentColumns = ["id"], // Coluna de chave primária em Orcamento
            childColumns = ["orcamentoId"], // A nova coluna em Lancamento
            onDelete = ForeignKey.CASCADE // Opcional: define o que acontece quando um Orcamento é deletado
        )
    ]
)
data class Lancamento(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Adicione esta nova propriedade

    @ColumnInfo(name = "descricao_lancamento")
    val descricao: String,

    @ColumnInfo(name = "valor_lancamento")
    val valor: Double,

    @ColumnInfo(name = "natureza")
    val natureza: String,

    @ColumnInfo(name = "categoria")
    val categoria: String,

    @ColumnInfo(name = "dataLancamento")
    val dataLancamento: Long,// ou String, dependendo do seu tipo

    @ColumnInfo(name = "orcamentoId")
    val orcamentoId: Int = 1

    // Adicione esta nova propriedade

// ou String, dependendo do seu tipo

)
