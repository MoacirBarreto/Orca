package devandroid.moacir.orca.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import devandroid.moacir.orca.ui.activity.CustomizacaoActivity
import devandroid.moacir.orca.ui.adapter.LancamentosActivity
import devandroid.moacir.orca.R
import devandroid.moacir.orca.ui.activity.RelatoriosActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Encontra o botão pelo ID
        val btnLancamentos = findViewById<Button>(R.id.btnGoToLancamentos)
        val btnGoToRelatorios = findViewById<Button>(R.id.btnGoToRelatorios)
        val btnGoToCustom = findViewById<Button>(R.id.btnGoToCustom)

        // Configura o listener de clique
        btnLancamentos.setOnClickListener {
            // Cria uma Intent para abrir a LancamentosActivity
            val intent = Intent(this, LancamentosActivity::class.java)
            // Inicia a nova activity
            startActivity(intent)
        }

        btnGoToRelatorios.setOnClickListener {
            // Cria uma Intent para abrir a RelatoriosActivity
            val intent = Intent(this, RelatoriosActivity::class.java)
            // Inicia a nova activity
            startActivity(intent)
        }

        btnGoToCustom.setOnClickListener {
            // Cria uma Intent para abrir a RelatoriosActivity
            val intent = Intent(this, CustomizacaoActivity::class.java)
            // Inicia a nova activity
            startActivity(intent)
        }

    }
}