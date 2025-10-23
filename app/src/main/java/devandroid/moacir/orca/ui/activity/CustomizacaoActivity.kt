package devandroid.moacir.orca.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import devandroid.moacir.orca.R

class CustomizacaoActivity : AppCompatActivity() {

    // Declara os componentes como variáveis da classe para fácil acesso
    private lateinit var textFieldDia: TextInputLayout
    private lateinit var editTextDia: TextInputEditText

    // ADICIONE ESTAS VARIÁVEIS para os campos de categoria
    private lateinit var editTextCat1: TextInputEditText
    private lateinit var editTextCat2: TextInputEditText
    private lateinit var editTextCat3: TextInputEditText
    private lateinit var editTextCat4: TextInputEditText
    private lateinit var editTextCat5: TextInputEditText
    private lateinit var buttonSalvar: Button
    private lateinit var buttonReset: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customizacao)

        // --- 1. Configuração da Toolbar ---
        val toolbar: MaterialToolbar = findViewById(R.id.toolbarCustom)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Meu Orçamento Pessoal"

        // --- 2. Encontrar os componentes do layout ---
        textFieldDia = findViewById(R.id.textFieldCustomDia)
        editTextDia = findViewById(R.id.editTextCustomDia)

        setupDiaVencimentoValidation()

        // --- 4. Encontrar os componentes das categorias e o botão ---
        editTextCat1 = findViewById(R.id.editTextCat1)
        editTextCat2 = findViewById(R.id.editTextCat2)
        editTextCat3 = findViewById(R.id.editTextCat3)
        editTextCat4 = findViewById(R.id.editTextCat4)
        editTextCat5 = findViewById(R.id.editTextCat5)
        buttonSalvar = findViewById(R.id.buttonCustomSalvar)
        buttonReset  = findViewById(R.id.buttonCustomLimpar)


// --- 5. Configurar a ação do botão Salvar ---
        buttonSalvar.setOnClickListener {
            salvarPreferencias()
        }
        buttonReset.setOnClickListener { // << Adicione este bloco
            restaurarPadroes()
        }
        carregarPreferencias()

    }

    private fun restaurarPadroes() {
        // 1. Define os valores padrão diretamente nos campos da tela
        val diaPadrao = 1
        val cat1Padrao = "Casa"
        val cat2Padrao = "Alimentação"
        val cat3Padrao = "Transporte"
        val cat4Padrao = "Lazer"
        val cat5Padrao = "Outros"

        editTextDia.setText(diaPadrao.toString())
        editTextCat1.setText(cat1Padrao)
        editTextCat2.setText(cat2Padrao)
        editTextCat3.setText(cat3Padrao)
        editTextCat4.setText(cat4Padrao)
        editTextCat5.setText(cat5Padrao)

        // 2. Opcional, mas recomendado: Limpa as preferências salvas
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear() // Remove TODAS as chaves deste arquivo de preferências
        editor.apply()

        // 3. Informa o usuário
        Toast.makeText(this, "Configurações restauradas para o padrão!", Toast.LENGTH_SHORT).show()
    }

    // Adicione esta função à sua classe CustomizacaoActivity

    private fun salvarPreferencias() {
        // Validação: Verifica se o campo de dia tem um erro antes de salvar.
        if (textFieldDia.error != null) {
            Toast.makeText(this, "Corrija os erros antes de salvar", Toast.LENGTH_SHORT).show()
            return // Para a execução da função aqui
        }

        // 1. Acessa o arquivo de SharedPreferences do app.
        // O nome "app_prefs" é um nome que você escolhe.
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // 2. Salva cada valor usando uma chave única.
        editor.putInt("DIA_VENCIMENTO", editTextDia.text.toString().toIntOrNull() ?: 1)
        editor.putString("NOME_CAT_1", editTextCat1.text.toString())
        editor.putString("NOME_CAT_2", editTextCat2.text.toString())
        editor.putString("NOME_CAT_3", editTextCat3.text.toString())
        editor.putString("NOME_CAT_4", editTextCat4.text.toString())
        editor.putString("NOME_CAT_5", editTextCat5.text.toString())

        // 3. Aplica (salva) as alterações.
        editor.apply() // .apply() salva em segundo plano, .commit() salva imediatamente.

        // 4. Mostra uma mensagem de sucesso para o usuário.
        Toast.makeText(this, "Configurações salvas com sucesso!", Toast.LENGTH_LONG).show()

        // 5. Opcional: Fecha a tela após salvar.
        finish()
    }


    private fun carregarPreferencias() {
        // 1. Acessa o mesmo arquivo de SharedPreferences que usamos para salvar.
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)

        // 2. Carrega cada valor usando a mesma chave.
        // Fornecemos um valor padrão caso a chave ainda não exista.
        val diaVencimento = sharedPreferences.getInt("DIA_VENCIMENTO", 1)
        val nomeCat1 = sharedPreferences.getString("NOME_CAT_1", "Casa") // Exemplo de valor padrão
        val nomeCat2 = sharedPreferences.getString("NOME_CAT_2", "Alimentação")
        val nomeCat3 = sharedPreferences.getString("NOME_CAT_3", "Transporte")
        val nomeCat4 = sharedPreferences.getString("NOME_CAT_4", "Lazer")
        val nomeCat5 = sharedPreferences.getString("NOME_CAT_5", "Outras")



        // 3. Define os valores carregados nos campos da tela.
        editTextDia.setText(diaVencimento.toString())
        editTextCat1.setText(nomeCat1)
        editTextCat2.setText(nomeCat2)
        editTextCat3.setText(nomeCat3)
        editTextCat4.setText(nomeCat4)
        editTextCat5.setText(nomeCat5)
    }



    private fun setupDiaVencimentoValidation() {
        editTextDia.addTextChangedListener { text ->
            // 1. Tenta converter o texto digitado para um número inteiro.
            // Se o campo estiver vazio ou não for um número, 'dia' será nulo.
            val dia = text.toString().toIntOrNull()

            // 2. Verifica as condições de erro
            if (dia == null) {
                // Se o campo não estiver vazio, mas não for um número válido
                if (text.toString().isNotEmpty()) {
                    textFieldDia.error = "Digite apenas números"
                } else {
                    // Se o campo estiver vazio, limpa o erro.
                    // Poderia também definir como "Campo obrigatório" se quisesse.
                    textFieldDia.error = null
                }
            } else if (dia < 1 || dia > 28) {
                // 3. Se o número estiver fora do intervalo permitido (1 a 28)
                textFieldDia.error = "O dia deve ser entre 1 e 28"
            } else {
                // 4. Se o número for válido, remove qualquer mensagem de erro
                textFieldDia.error = null
            }
        }
    }


    // Função para fazer o botão de "voltar" da Toolbar funcionar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Verifica se o ícone clicado foi o de "home" (a seta)
        if (item.itemId == android.R.id.home) {
            finish() // Fecha a activity atual e retorna à anterior
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}