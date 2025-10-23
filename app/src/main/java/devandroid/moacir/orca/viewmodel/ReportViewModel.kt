package devandroid.moacir.orca.viewmodel

import androidx.lifecycle.viewModelScope
import devandroid.moacir.orca.data.model.Lancamento
import devandroid.moacir.orca.data.repository.LancamentoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

// Enum para representar os filtros (está correto)
enum class DateFilterType {
    CURRENT_MONTH,
    YEAR_TO_DATE,
    CUSTOM_RANGE
}

//                       👇 Use sua classe de repositório REAL aqui
class ReportViewModel(private val lancamentoRepository: LancamentoRepository) :
    androidx.lifecycle.ViewModel() {

    //           👇 Use sua classe de entidade REAL aqui
    private val _reportData = MutableStateFlow<List<Lancamento>>(emptyList())
    val reportData: StateFlow<List<Lancamento>> = _reportData

    fun loadReportData(
        filterType: DateFilterType,
        customStartDate: Date? = null,
        customEndDate: Date? = null
    ) {
        val (startDate, endDate) = calculateDateRange(
            filterType,
            customStartDate,
            customEndDate
        )

        viewModelScope.launch {
            // Use seu repositório e sua entidade reais
            lancamentoRepository.getLancamentosEntre(startDate, endDate).collect { data ->
                _reportData.value = data
            }
        }
    }

    // A função calculateDateRange está correta, não precisa mudar.
    // Definição CORRETA
    private fun calculateDateRange(
        filterType: DateFilterType,
        customStartDate: Date?,
        customEndDate: Date?
    ): Pair<Date, Date> { // <-- Especifica o tipo de retorno
        val calendar = java.util.Calendar.getInstance()
        return when (filterType) {
            DateFilterType.CURRENT_MONTH -> {
                calendar.set(java.util.Calendar.DAY_OF_MONTH, 1)
                val start = calendar.time
                calendar.add(java.util.Calendar.MONTH, 1)
                calendar.add(java.util.Calendar.DAY_OF_MONTH, -1)
                val end = calendar.time
                start to end // Retorna o Pair
            }

            DateFilterType.YEAR_TO_DATE -> {
                calendar.set(java.util.Calendar.DAY_OF_YEAR, 1)
                val start = calendar.time
                val end = Date() // Hoje
                start to end // Retorna o Pair
            }

            DateFilterType.CUSTOM_RANGE -> {
                requireNotNull(customStartDate) { "Data de início não pode ser nula para o filtro customizado." }
                requireNotNull(customEndDate) { "Data de fim não pode ser nula para o filtro customizado." }
                customStartDate to customEndDate // Retorna o Pair
            }
        }
    }
}

