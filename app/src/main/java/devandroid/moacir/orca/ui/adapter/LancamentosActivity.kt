package devandroid.moacir.orca.ui.adapter

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import devandroid.moacir.orca.R
import devandroid.moacir.orca.data.database.AppDatabase
import devandroid.moacir.orca.data.database.LancamentoDao
import devandroid.moacir.orca.data.model.Lancamento
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class LancamentosActivity : AppCompatActivity() {
    // Variável para guardar a data selecionada. Será inicializada no onCreate.
    private val selectedDate: Calendar = Calendar.getInstance()

    // --- NOVA VARIÁVEL PARA O BANCO DE DADOS ---
    private lateinit var lancamentoDao: LancamentoDao

    // Declaração dos componentes da UI
    private lateinit var dateEditText: TextInputEditText
    private lateinit var toolbar: MaterialToolbar
    private lateinit var editTextValor: TextInputEditText
    private lateinit var radioGroupNatureza: RadioGroup
    private lateinit var radioButtonCat1: RadioButton
    private lateinit var radioButtonCat2: RadioButton
    private lateinit var radioButtonCat3: RadioButton
    private lateinit var radioButtonCat4: RadioButton
    private lateinit var radioButtonCat5: RadioButton
    private lateinit var radioGroupCategoria: RadioGroup
    private lateinit var editTextDescricao: TextInputEditText
    private lateinit var buttonLimpar: Button
    private lateinit var buttonSalvar: Button
    private var isResetting = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lancamentos)

        // --- NOVO: Inicialização do Banco de Dados ---
        // Pega a instância do DAO para que possamos usá-la na activity
        lancamentoDao = AppDatabase.Companion.getDatabase(this).lancamentoDao()

        // --- 1. Configuração da UI (Views) ---
        configurarViews()

        // --- 2. Carrega as configurações do usuário (SharedPreferences) ---
        carregarConfiguracoes()

        // --- 3. Configura funcionalidades dos campos ---
        configuraCampoData()
        configuraCampoValor()
        configuraBotaoSalvar()
        configuraPreenchimentoDescricao()
        configuraBotaoLimpar()
    }

    private fun configurarViews() {
        /**
         * Encontra todas as Views no layout e configura a Toolbar.
         */
        // Toolbar
        toolbar = findViewById(R.id.toolbarLancamentos)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Meu Orçamento Pessoal"

        // Campos de texto
        dateEditText = findViewById(R.id.date_edit_text)
        editTextValor = findViewById(R.id.editTextValor)
        editTextDescricao = findViewById(R.id.editTextDescricao)


        // RadioGroups e RadioButtons
        radioGroupNatureza = findViewById(R.id.radioGroupNatureza)
        radioGroupCategoria = findViewById(R.id.radioGroupCategoria)
        radioButtonCat1 = findViewById(R.id.radio_button_cat1)
        radioButtonCat2 = findViewById(R.id.radio_button_cat2)
        radioButtonCat3 = findViewById(R.id.radio_button_cat3)
        radioButtonCat4 = findViewById(R.id.radio_button_cat4)
        radioButtonCat5 = findViewById(R.id.radio_button_cat5)

        // Botões
        buttonLimpar = findViewById(R.id.buttonLimpar)
        buttonSalvar = findViewById(R.id.buttonSalvar)
    }

    // A função de configurar o botão salvar agora só define o listener
    private fun configuraBotaoSalvar() {
        buttonSalvar.setOnClickListener {
            salvarLancamento()
        }
    }

    /**
     * NOVA FUNÇÃO: Coleta, valida e salva os dados no banco.
     */
    private fun salvarLancamento() {
        // --- 1. COLETA DE DADOS ---

        // Valor
        val valorString = editTextValor.text.toString()
            .replace("R$", "")
            .replace(".", "")
            .replace(",", ".")
            .trim()
        val valor = valorString.toDoubleOrNull()

        // Natureza
        val idNaturezaSelecionada = radioGroupNatureza.checkedRadioButtonId
        val natureza = if (idNaturezaSelecionada != -1) {
            findViewById<RadioButton>(idNaturezaSelecionada).text.toString().uppercase()
        } else {
            null
        }

        // Categoria
        val idCategoriaSelecionada = radioGroupCategoria.checkedRadioButtonId
        val categoria = if (idCategoriaSelecionada != -1) {
            findViewById<RadioButton>(idCategoriaSelecionada).text.toString()
        } else {
            null
        }

        // Descrição
        val descricao = editTextDescricao.text.toString()

        // Data
        val dataLancamento = selectedDate.timeInMillis

        // --- 2. VALIDAÇÃO ---
        if (valor == null || valor == 0.0) {
            Toast.makeText(this, "Por favor, insira um valor válido.", Toast.LENGTH_SHORT).show()
            editTextValor.requestFocus() // Foca no campo de valor
            return
        }
        if (natureza == null) {
            Toast.makeText(this, "Por favor, selecione a natureza (Receita/Despesa).", Toast.LENGTH_SHORT).show()
            return
        }
        if (categoria == null) {
            Toast.makeText(this, "Por favor, selecione uma categoria.", Toast.LENGTH_SHORT).show()
            return
        }
        if (descricao.isBlank()) {
            Toast.makeText(this, "Por favor, insira uma descrição.", Toast.LENGTH_SHORT).show()
            editTextDescricao.requestFocus()
            return
        }

        // --- 3. CRIAÇÃO DO OBJETO ---
        val novoLancamento = Lancamento(
            valor = valor,
            natureza = natureza,
            categoria = categoria,
            descricao = descricao,
            dataLancamento = dataLancamento
            // 'id' e 'dataCriacao' são gerados automaticamente
        )

        // --- 4. SALVAR NO BANCO DE DADOS (USANDO COROUTINES) ---
        lifecycleScope.launch {
            try {
                lancamentoDao.inserir(novoLancamento)

                Log.d("LancamentosActivity", "Sucesso! Lançamento salvo: $novoLancamento")
                Toast.makeText(this@LancamentosActivity, "Lançamento salvo com sucesso!", Toast.LENGTH_LONG).show()

                // Opcional: fechar a tela após salvar
                finish()

            } catch (e: Exception) {
                Log.e("LancamentosActivity", "Erro ao salvar lançamento", e)
                Toast.makeText(this@LancamentosActivity, "Falha ao salvar. Tente novamente.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    // As outras funções permanecem as mesmas
    private fun configuraBotaoLimpar() {
        buttonLimpar.setOnClickListener {
            resetarFormulario()
        }
    }

    private fun resetarFormulario() {
        isResetting = true
        editTextValor.setText("")
        editTextDescricao.setText("")
        radioGroupNatureza.clearCheck()
        radioGroupCategoria.clearCheck()
        recarregarNomesCategorias()
        definirDataPadraoParaHoje()
        currentFocus?.clearFocus()
        Toast.makeText(this, "Formulário limpo", Toast.LENGTH_SHORT).show()
        isResetting = false
    }

    private fun recarregarNomesCategorias() {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        radioButtonCat1.text = sharedPreferences.getString("NOME_CAT_1", "Moradia")
        radioButtonCat2.text = sharedPreferences.getString("NOME_CAT_2", "Alimentação")
        radioButtonCat3.text = sharedPreferences.getString("NOME_CAT_3", "Transporte")
        radioButtonCat4.text = sharedPreferences.getString("NOME_CAT_4", "Lazer")
        radioButtonCat5.text = sharedPreferences.getString("NOME_CAT_5", "Outros")
    }

    private fun configuraPreenchimentoDescricao() {
        radioGroupCategoria.setOnCheckedChangeListener { group, checkedId ->
            if (isResetting) return@setOnCheckedChangeListener
            if (checkedId == -1) return@setOnCheckedChangeListener

            val selectedRadioButton: RadioButton = group.findViewById(checkedId)
            val categoriaTexto = selectedRadioButton.text.toString()
            val descricaoAtual = editTextDescricao.text.toString()
            val nomesCategorias = listOf(
                radioButtonCat1.text.toString(),
                radioButtonCat2.text.toString(),
                radioButtonCat3.text.toString(),
                radioButtonCat4.text.toString(),
                radioButtonCat5.text.toString()
            )

            if (descricaoAtual.isEmpty() || nomesCategorias.contains(descricaoAtual)) {
                editTextDescricao.setText(categoriaTexto)
            }
        }
    }

    private fun carregarConfiguracoes() {
        recarregarNomesCategorias()
        definirDataPadraoParaHoje()
    }

    private fun definirDataPadraoParaHoje() {
        val calendarioHoje = Calendar.getInstance()
        selectedDate.time = calendarioHoje.time
        val formatoData = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateEditText.setText(formatoData.format(selectedDate.time))
    }

    private fun configuraCampoData() {
        val textFieldDate = findViewById<TextInputLayout>(R.id.textFieldDate)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        textFieldDate.setEndIconOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecione a data")
                .setSelection(selectedDate.timeInMillis)
                .build()

            datePicker.addOnPositiveButtonClickListener { selection ->
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(selection)
                selectedDate.timeInMillis = selection + offset
                dateEditText.setText(dateFormat.format(selectedDate.time))
            }

            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }
    }

    private fun configuraCampoValor() {
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

        editTextValor.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val cleanString = editTextValor.text.toString()
                    .replace("R$", "")
                    .replace(".", "")
                    .replace(",", ".")
                    .trim()
                if (cleanString != "0.0") {
                    editTextValor.setText(cleanString)
                } else {
                    editTextValor.setText("")
                }
            } else {
                val text = editTextValor.text.toString()
                if (text.isNotEmpty()) {
                    try {
                        val value = text.toDouble()
                        editTextValor.setText(currencyFormat.format(value))
                    } catch (e: NumberFormatException) {
                        editTextValor.setText("")
                        Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}