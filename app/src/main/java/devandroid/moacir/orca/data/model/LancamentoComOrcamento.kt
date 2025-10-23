// Em: app/src/main/java/devandroid/moacir/orca/data/model/LancamentoComOrcamento.kt

package devandroid.moacir.orca.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class LancamentoComOrcamento(
    @Embedded val lancamento: Lancamento,
    @Relation(
        parentColumn = "orcamentoId", // Chave estrangeira em 'Lancamento'
        entityColumn = "id"           // Chave primária em 'Orcamento'
    )
    val orcamento: Orcamento
)
