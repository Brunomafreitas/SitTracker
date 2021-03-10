package ipvc.estg.cm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ipvc.estg.cm.DAO.notasDAO
import ipvc.estg.cm.ENTIDADES.notasPessoais
import kotlinx.coroutines.CoroutineScope

@Database(entities = arrayOf(notasPessoais::class), version = 1, exportSchema = false)
public abstract class NotasDB : RoomDatabase() {

    abstract fun wordDao(): notasDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: NotasDB? = null

        fun getDatabase(
            context: Context,
            viewModelScope: CoroutineScope
        ): NotasDB {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotasDB::class.java,
                    "notas_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}