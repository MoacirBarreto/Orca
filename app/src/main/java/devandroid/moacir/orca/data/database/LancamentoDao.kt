package devandroid.moacir.orca.data.database

import devandroid.moacir.orca.data.model.Lancamento
import devandroid.moacir.orca.data.model.LancamentoComOrcamento
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface LancamentoDao {

    @Insert
    suspend fun inserir(lancamento: Lancamento)

    @Query("SELECT * FROM lancamentos ORDER BY data_lancamento DESC")
    fun getAll(): Flow<List<Lancamento>>

    @Query("SELECT * FROM lancamentos WHERE data_lancamento BETWEEN :startDate AND :endDate ORDER BY data_lancamento DESC") // Corrigido: data_lancamento para data
    fun getLancamentosEntre(startDate: Date, endDate: Date): Flow<List<Lancamento>>

    @Transaction // Essencial para consultas que usam @Relation
    @Query("SELECT * FROM lancamentos WHERE data_lancamento BETWEEN :startDate AND :endDate")
    fun getLancamentosAgrupadosPorData(startDate: Date, endDate: Date): Flow<List<LancamentoComOrcamento>>

}
