package devandroid.moacir.orca

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Encontra o botão pelo ID
        val btnLancamentos = findViewById<Button>(R.id.btnGoToLancamentos)

        // Configura o listener de clique
        btnLancamentos.setOnClickListener {
            // Cria uma Intent para abrir a LancamentosActivity
            val intent = Intent(this, LancamentosActivity::class.java)
            // Inicia a nova activity
            startActivity(intent)
        }

        // TODO: Adicionar listeners para os botões de Relatórios e Configurações quando criar as Activities
    }
}