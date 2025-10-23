package devandroid.moacir.orca.data.database

import devandroid.moacir.orca.data.model.Lancamento
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


/**
 * A classe principal do banco de dados Room para o aplicativo.
 * Ela une as entidades (tabelas) e os DAOs (objetos de acesso).
 */
@Database(
    entities = [Lancamento::class], // 1. Lista todas as entidades (tabelas) que o banco de dados terá.
    version = 1,                     // 2. Versão do banco de dados. Mude se alterar o esquema.
    exportSchema = false             // 3. Desativa a exportação de esquema para simplificar.
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    // 4. Fornece uma maneira de obter uma instância do nosso DAO.
    abstract fun lancamentoDao(): LancamentoDao
    abstract fun orcamentoDao(): OrcamentoDao

    // O companion object permite criar uma instância única (Singleton) do banco de dados.
    companion object {
        /**
         * A anotação @Volatile garante que o valor de INSTANCE seja sempre atualizado
         * e visível para todos os threads, prevenindo problemas de concorrência.
         */
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Método para obter a instância única do banco de dados.
         * Se a instância não existir, ela é criada de forma segura (thread-safe).
         */
        fun getDatabase(context: Context): AppDatabase {
            // Retorna a instância se ela já existir.
            return INSTANCE ?: synchronized(this) {
                // Se não existir, cria o banco de dados.
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "orca_app_database" // Este será o nome do arquivo do banco de dados no dispositivo.
                )
                    //.fallbackToDestructiveMigration() // Usado se precisar de migrações no futuro
                    .build()

                INSTANCE = instance
                // Retorna a instância recém-criada.
                instance
            }
        }
    }
}
