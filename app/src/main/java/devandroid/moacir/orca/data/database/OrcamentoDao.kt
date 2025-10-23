package devandroid.moacir.orca.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import devandroid.moacir.orca.data.model.Orcamento
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) para a entidade Orcamento.
 * Define os métodos para interagir com a tabela 'orcamentos' no banco de dados.
 */
@Dao
interface OrcamentoDao {

    /**
     * Insere um novo orçamento no banco de dados.
     * Se um orçamento com o mesmo ID já existir, ele será substituído.
     * @param orcamento O objeto Orcamento a ser inserido.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(orcamento: Orcamento)

    /**
     * Busca todos os orçamentos do banco de dados, ordenados pelo nome.
     * Retorna um Flow, que permite à UI observar mudanças na tabela e se atualizar automaticamente.
     * @return Um Flow contendo a lista de todos os orçamentos.
     */
    @Query("SELECT * FROM orcamentos ORDER BY nome ASC")
    fun getAll(): Flow<List<Orcamento>>

    /**
     * Busca um orçamento específico pelo seu ID.
     * @param id O ID do orçamento a ser buscado.
     * @return Um Flow contendo o Orcamento correspondente, se encontrado.
     */
    @Query("SELECT * FROM orcamentos WHERE id = :id")
    fun getById(id: Long): Flow<Orcamento>
}
