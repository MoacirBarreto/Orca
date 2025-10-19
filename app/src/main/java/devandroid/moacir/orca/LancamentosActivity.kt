package devandroid.moacir.orca

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class LancamentosActivity : AppCompatActivity() {
    // Variável para guardar a data selecionada. Será inicializada no onCreate.
    private val selectedDate: Calendar = Calendar.getInstance()

    // Declaração dos componentes da UI
    private lateinit var dateEditText: TextInputEditText
    private lateinit var toolbar: MaterialToolbar
    private lateinit var radioButtonCat1: RadioButton
    private lateinit var radioButtonCat2: RadioButton
    private lateinit var radioButtonCat3: RadioButton
    private lateinit var radioButtonCat4: RadioButton
    private lateinit var radioButtonCat5: RadioButton
    private lateinit var radioGroupCategoria: RadioGroup
    private lateinit var editTextDescricao: TextInputEditText
    private lateinit var buttonLimpar: Button
    private var isResetting = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lancamentos)

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

        // RadioButtons
        radioButtonCat1 = findViewById(R.id.radio_button_cat1)
        radioButtonCat2 = findViewById(R.id.radio_button_cat2)
        radioButtonCat3 = findViewById(R.id.radio_button_cat3)
        radioButtonCat4 = findViewById(R.id.radio_button_cat4)
        radioButtonCat5 = findViewById(R.id.radio_button_cat5)
        radioGroupCategoria = findViewById(R.id.radioGroupCategoria)
        editTextDescricao = findViewById(R.id.editTextDescricao)
        buttonLimpar = findViewById(R.id.buttonLimpar)
    }


    private fun configuraBotaoLimpar() {
        buttonLimpar.setOnClickListener {
            resetarFormulario()
        }
    }

    private fun resetarFormulario() {
        // --- PASSO CRUCIAL: ATIVA A FLAG ---
        // Avisa ao resto do app: "Estou limpando o formulário!"
        isResetting = true

        // 1. Limpa os campos de texto principais
        findViewById<TextInputEditText>(R.id.editTextValor).setText("")
        editTextDescricao.setText("")

        // 2. Limpa a seleção dos RadioGroups
        findViewById<RadioGroup>(R.id.radioGroupNatureza).clearCheck()
        radioGroupCategoria.clearCheck()

        // 3. RECARREGA OS PADRÕES (Nomes das categorias)
        recarregarNomesCategorias()

        // 4. RESETA A DATA PARA O PADRÃO (HOJE)
        definirDataPadraoParaHoje()

        // 5. LIMPA O FOCO DE QUALQUER CAMPO DE TEXTO ATIVO
        currentFocus?.clearFocus()

        // 6. Mostra uma mensagem para o usuário
        Toast.makeText(this, "Formulário limpo", Toast.LENGTH_SHORT).show()

        // --- PASSO CRUCIAL: DESATIVA A FLAG ---
        // Avisa ao app: "Terminei de limpar. Pode voltar ao normal."
        isResetting = false
    }


    // NOVA FUNÇÃO AUXILIAR para carregar apenas os nomes
    private fun recarregarNomesCategorias() {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        radioButtonCat1.text = sharedPreferences.getString("NOME_CAT_1", "Moradia")
        radioButtonCat2.text = sharedPreferences.getString("NOME_CAT_2", "Alimentação")
        radioButtonCat3.text = sharedPreferences.getString("NOME_CAT_3", "Transporte")
        radioButtonCat4.text = sharedPreferences.getString("NOME_CAT_4", "Lazer")
        radioButtonCat5.text = sharedPreferences.getString("NOME_CAT_5", "Outros")
    }

    // Substitua a função antiga por esta versão melhorada
    private fun configuraPreenchimentoDescricao() {
        // Adiciona um "ouvinte" que é acionado QUANDO A SELEÇÃO DA CATEGORIA MUDA
        radioGroupCategoria.setOnCheckedChangeListener { group, checkedId ->

            // Se a flag 'isResetting' estiver ativa, ignora TUDO.
            if (isResetting) {
                return@setOnCheckedChangeListener
            }

            // Se nenhum botão estiver selecionado (o que é raro), não faz nada.
            if (checkedId == -1) {
                return@setOnCheckedChangeListener
            }

            // Encontra o RadioButton que foi selecionado.
            val selectedRadioButton: RadioButton = group.findViewById(checkedId)

            // Pega o texto da categoria (ex: "Alimentação").
            val categoriaTexto = selectedRadioButton.text.toString()

            // --- LÓGICA INTELIGENTE ---
            // Verifica se a descrição está vazia OU se o texto atual
            // é o nome de uma das outras categorias. Isso permite a atualização
            // automática enquanto o usuário decide, mas para de atualizar
            // se ele digitar um texto personalizado (ex: "Alimentação - almoço").
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


    /**
     * Carrega as preferências salvas pelo usuário (dia de vencimento, nomes de categoria)
     * e as aplica nesta tela.
     */
    private fun carregarConfiguracoes() {
        // Agora esta função carrega APENAS os nomes das categorias
        recarregarNomesCategorias()
        // E chama a função que define a data padrão para HOJE
        definirDataPadraoParaHoje()
    }

    private fun definirDataPadraoParaHoje() {
        // 1. Pega uma instância FRESCA do calendário (data de hoje)
        val calendarioHoje = Calendar.getInstance()

        // 2. Atualiza a data selecionada globalmente
        selectedDate.time = calendarioHoje.time

        // 3. Formata e exibe a data no campo
        val formatoData = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateEditText.setText(formatoData.format(selectedDate.time))
    }

    private fun configuraCampoData() {
        val textFieldDate = findViewById<TextInputLayout>(R.id.textFieldDate)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        // Configura o clique no ÍCONE para abrir o DatePicker
        textFieldDate.setEndIconOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecione a data")
                .setSelection(selectedDate.timeInMillis) // Usa a data já definida
                .build()

            datePicker.addOnPositiveButtonClickListener { selection ->
                // Corrige o fuso horário para não pegar o dia anterior
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(selection)

                // Atualiza a data selecionada na nossa variável
                selectedDate.timeInMillis = selection + offset

                // Formata e exibe a data no campo
                dateEditText.setText(dateFormat.format(selectedDate.time))
            }

            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }
    }

    private fun configuraCampoValor() {
        val valorEditText: TextInputEditText = findViewById(R.id.editTextValor)
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

        valorEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // Ao focar, remove a formatação R$ para facilitar a edição
                val cleanString = valorEditText.text.toString()
                    .replace("R$", "")
                    .replace(".", "")
                    .replace(",", ".")
                    .trim()
                valorEditText.setText(cleanString)
            } else {
                // Ao perder o foco, formata para moeda
                val text = valorEditText.text.toString()
                if (text.isNotEmpty()) {
                    try {
                        val value = text.toDouble()
                        valorEditText.setText(currencyFormat.format(value))
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

    // Trata o clique no botão "voltar" da Toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish() // Finaliza a activity atual e retorna para a anterior
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
