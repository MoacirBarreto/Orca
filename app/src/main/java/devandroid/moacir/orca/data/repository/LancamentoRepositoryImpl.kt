// Em: app/src/main/java/devandroid/moacir/orca/data/repository/LancamentoRepositoryImpl.kt

package devandroid.moacir.orca.data.repository

import devandroid.moacir.orca.data.database.LancamentoDao
import devandroid.moacir.orca.data.model.Lancamento
import devandroid.moacir.orca.data.model.LancamentoComOrcamento
import kotlinx.coroutines.flow.Flow
import java.util.Date

class LancamentoRepositoryImpl(private val lancamentoDao: LancamentoDao) : LancamentoRepository {

    // 3. Sobrescreva (override) os métodos da interface e implemente-os
    fun getLancamentosAgrupadosPorData(
        startDate: Date,
        endDate: Date
    ): Flow<List<LancamentoComOrcamento>> {
        // CORREÇÃO: Chamar o método do DAO, não o próprio método.
        return lancamentoDao.getLancamentosAgrupadosPorData(startDate, endDate)
    }

    override fun getLancamentosEntre(startDate: Date, endDate: Date): Flow<List<Lancamento>> {
        // CORREÇÃO: Chamar o método do DAO, não o próprio método.
        return lancamentoDao.getLancamentosEntre(startDate, endDate)
    }
}
