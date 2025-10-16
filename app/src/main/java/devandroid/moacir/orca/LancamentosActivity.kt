package devandroid.moacir.orca

import android.os.Bundle
import android.util.Log
import android.view.MenuItem // 1. IMPORT NECESSÁRIO para o botão "voltar"
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar // 2. IMPORT CORRETO (android.icu.util é mais novo, mas este é suficiente)
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class LancamentosActivity : AppCompatActivity() {
    // Variável para guardar a data selecionada. Inicia com a data/hora atual.
    private val selectedDate: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lancamentos)

        // --- 1. Configuração da ToolBar ---
        val toolbar: MaterialToolbar = findViewById(R.id.toolbarLancamentos)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Lançamentos"

        // --- 2. Configuração do Campo de Data com DatePicker ---
        configuraCampoData()

        // --- 3. Configuração do Campo de Valor (Formatação Monetária) ---
        configuraCampoValor()

        // --- 4. Configuração do Botão Salvar ---
        configuraBotaoSalvar()
    }

    private fun configuraCampoData() {
        val textFieldDate = findViewById<TextInputLayout>(R.id.textFieldDate)
        val dateEditText = textFieldDate.editText as? TextInputEditText

        // Define a data de hoje como valor inicial
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val hoje = dateFormat.format(Date())
        dateEditText?.setText(hoje)

        // Configura o clique no ÍCONE para abrir o DatePicker
        textFieldDate.setEndIconOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecione a data")
                .setSelection(selectedDate.timeInMillis) // Usa a data já selecionada (ou hoje)
                .build()

            datePicker.addOnPositiveButtonClickListener { selection ->
                // Corrige o fuso horário para não pegar o dia anterior
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(selection)
                val dateInMillis = selection + offset

                // Atualiza a data selecionada na nossa variável
                selectedDate.timeInMillis = dateInMillis

                // Formata e exibe a data no campo
                dateEditText?.setText(dateFormat.format(selectedDate.time))
            }

            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }
    }

    private fun configuraCampoValor() {
        // CORREÇÃO: Usando o ID correto 'editTextValor' do seu XML
        val valorEditText: TextInputEditText = findViewById(R.id.editTextValor)
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

        valorEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val text = valorEditText.text.toString()
                if (text.isNotEmpty()) {
                    try {
                        val cleanString = text.replace(",", ".")
                        val value = cleanString.toDouble()
                        val formattedValue = currencyFormat.format(value)
                        valorEditText.setText(formattedValue)
                    } catch (e: NumberFormatException) {
                        valorEditText.setText("") // Limpa se for inválido
                        Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun configuraBotaoSalvar() {
        val buttonSalvar = findViewById<Button>(R.id.buttonSalvar)
        buttonSalvar.setOnClickListener {
            // Pega a hora, minuto e segundo do momento do clique
            val agora = Calendar.getInstance()

            // Combina a DATA selecionada no DatePicker com a HORA atual
            val finalTimestamp = Calendar.getInstance().apply {
                time = selectedDate.time // Começa com a data correta (dia, mês, ano)
                set(Calendar.HOUR_OF_DAY, agora.get(Calendar.HOUR_OF_DAY))
                set(Calendar.MINUTE, agora.get(Calendar.MINUTE))
                set(Calendar.SECOND, agora.get(Calendar.SECOND))
            }

            val timestampParaSalvar: Long = finalTimestamp.timeInMillis

            Log.d("LancamentosActivity", "Timestamp a ser salvo: $timestampParaSalvar")
            Log.d("LancamentosActivity", "Data/Hora formatada: ${Date(timestampParaSalvar)}")

            // TODO: Chamar a função para inserir os dados no banco de dados
            Toast.makeText(this, "Lançamento salvo!", Toast.LENGTH_SHORT).show()
        }
    }

    // --- 5. Trata o clique no botão "voltar" da Toolbar ---
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish() // Finaliza a activity atual e retorna para a anterior
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
