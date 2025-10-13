package devandroid.moacir.orca

import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class LancamentosActivity : AppCompatActivity() {
    private var selectedDate: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lancamentos)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbarLancamentos)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Habilita o botão de voltar ("Up button")

        // 1. Encontre o seu TextInputLayout pelo ID
        val textFieldDate = findViewById<TextInputLayout>(R.id.textFieldDate)

        // 2. Configure o listener de clique para o ÍCONE de calendário
        textFieldDate.setEndIconOnClickListener {

            // 3. Crie o MaterialDatePicker
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecione a data")
                // Pré-seleciona a data de hoje
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            // 4. Adicione um listener para quando o usuário confirmar a data
            datePicker.addOnPositiveButtonClickListener { selection ->
                // O 'selection' vem em milissegundos UTC. Precisamos converter.

                // Adiciona o fuso horário para corrigir a data (evita pegar o dia anterior)
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                val dateInMillis = selection + offset

                // Atualiza nossa variável da classe com a data e hora selecionadas
                selectedDate.timeInMillis = dateInMillis

                // Formata a data para o padrão brasileiro (dd/MM/yyyy) para exibir no campo
                val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = format.format(selectedDate.time)

                // 5. Coloque a data formatada no campo de texto
                textFieldDate.editText?.setText(formattedDate)
            }

            // 6. Mostre o DatePicker na tela
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }
        // Dentro do seu método onCreate, após o código do DatePicker

// ...

        // Lógica para o botão Salvar
        val buttonSalvar = findViewById<Button>(R.id.buttonSalvar)
        buttonSalvar.setOnClickListener {
            // Pega a hora, minuto e segundo atuais
            val now = Calendar.getInstance()
            val hour = now.get(Calendar.HOUR_OF_DAY)
            val minute = now.get(Calendar.MINUTE)
            val second = now.get(Calendar.SECOND)

            // Combina a DATA selecionada no DatePicker com a HORA atual do clique
            val finalTimestamp = Calendar.getInstance()
            finalTimestamp.time = selectedDate.time // Começa com a data correta
            finalTimestamp.set(Calendar.HOUR_OF_DAY, hour)
            finalTimestamp.set(Calendar.MINUTE, minute)
            finalTimestamp.set(Calendar.SECOND, second)

            // AGORA, 'finalTimestamp.time' é o objeto Date que você quer salvar!
            // 'finalTimestamp.timeInMillis' é o valor Long que você vai gravar no banco de dados.

            // --- Exemplo de como usar ---
            val timestampParaSalvar: Long = finalTimestamp.timeInMillis

            // Apenas para verificação no Logcat
            Log.d("MainActivity", "Timestamp a ser salvo no BD: $timestampParaSalvar")
            Log.d("MainActivity", "Data/Hora formatada: ${Date(timestampParaSalvar)}")

            // TODO: Aqui você chamaria a função para inserir os dados no banco de dados
            // ex: viewModel.salvarLancamento(timestampParaSalvar, natureza, categoria, etc...)

            Toast.makeText(this, "Lançamento salvo!", Toast.LENGTH_SHORT).show()
        }
    } // Fim do onCreate
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Verifica se o botão pressionado é o botão "home" (o de voltar)
        if (item.itemId == android.R.id.home) {
            // Finaliza a activity atual e retorna para a anterior (MainActivity)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}