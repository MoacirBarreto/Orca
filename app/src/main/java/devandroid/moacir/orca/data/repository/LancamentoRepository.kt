package devandroid.moacir.orca.data.repository
// Em app/src/main/java/devandroid/moacir/orca/data/repository/LancamentoRepository.kt

import devandroid.moacir.orca.data.model.Lancamento
import kotlinx.coroutines.flow.Flow
import java.util.Date

// Interface ou classe para seu repositório
interface LancamentoRepository {
    // Exemplo de função que seu ViewModel estava chamando
    fun getLancamentosEntre(startDate: Date, endDate: Date): Flow<List<Lancamento>>

    // ... outras funções do repositório
}

// Se você tiver uma implementação concreta, ela viria aqui.
// class LancamentoRepositoryImpl(...) : LancamentoRepository { ... }
