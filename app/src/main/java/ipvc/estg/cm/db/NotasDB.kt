package ipvc.estg.cm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import ipvc.estg.cm.DAO.notasDAO

@Database(entities = arrayOf(NotasDB::class), version = 1, exportSchema = false)
public abstract class NotasDB {

    abstract fun wordDao(): notasDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: NotasDB? = null

        fun getDatabase(context: Context): NotasDB {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotasDB::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}