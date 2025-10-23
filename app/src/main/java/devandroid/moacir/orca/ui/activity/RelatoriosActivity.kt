package devandroid.moacir.orca.ui.activity

// Importe as classes que você precisa para a factory

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.MaterialToolbar
import devandroid.moacir.orca.R
import devandroid.moacir.orca.data.database.AppDatabase
import devandroid.moacir.orca.data.repository.LancamentoRepositoryImpl
import devandroid.moacir.orca.viewmodel.DateFilterType
import devandroid.moacir.orca.viewmodel.ReportViewModel
import devandroid.moacir.orca.viewmodel.ReportViewModelFactory
import kotlinx.coroutines.launch

class RelatoriosActivity : AppCompatActivity() {

    // --- CORREÇÃO AQUI ---
    // A inicialização da factory e suas dependências deve acontecer DENTRO do bloco by viewModels
    private val reportViewModel: ReportViewModel by viewModels {
        // 1. Crie a instância do banco de dados de forma segura
        val database = AppDatabase.getDatabase(applicationContext)
        // 2. Crie a instância do repositório
        val repository = LancamentoRepositoryImpl(database.lancamentoDao())
        // 3. Crie e retorne a Factory, passando o repositório para ela
        ReportViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_relatorios)

        // --- Configuração da Toolbar ---
        val toolbar: MaterialToolbar = findViewById(R.id.toolbarRelatorios)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Relatórios"

        // Agora o viewModel está instanciado corretamente e pronto para uso
        reportViewModel.loadReportData(DateFilterType.CURRENT_MONTH)

        // Observe os dados do ViewModel para atualizar a UI
        lifecycleScope.launch {
            reportViewModel.reportData.collect { reportItems ->
                // Aqui você atualiza sua RecyclerView com a lista 'reportItems'
                // Ex: adapter.submitList(reportItems)
            }
        }
    }

    // Função para fazer o botão de "voltar" funcionar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
