package devandroid.moacir.orca

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar

class RelatoriosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_relatorios)

        // --- Configuração da Toolbar ---
        val toolbar: MaterialToolbar = findViewById(R.id.toolbarRelatorios)
        setSupportActionBar(toolbar)

        // Habilita o botão de "voltar" (seta) na Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Define um título para a barra
        supportActionBar?.title = "Relatórios"
    }

    // Função para fazer o botão de "voltar" funcionar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish() // Fecha a activity atual e volta para a anterior
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}