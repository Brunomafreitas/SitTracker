package ipvc.estg.cm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ipvc.estg.cm.DAO.notasDAO
import ipvc.estg.cm.ENTIDADES.notasPessoais
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

@Database(entities = arrayOf<KClass<*>>(notasPessoais::class), version = 4, exportSchema = false)
public abstract class NotasDB : RoomDatabase() {

    abstract fun wordDao(): notasDAO

    private class notasDataBaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback(){
        override fun onOpen (db: SupportSQLiteDatabase){
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var noteDAO = database.wordDao()
                    //noteDAO.deleteAll()


                }
            }
        }
    }


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: NotasDB? = null

        fun getDatabase(context: Context,scope: CoroutineScope): NotasDB {
            val tempInstance = INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotasDB::class.java,
                    "notas_database"

                )
                    .fallbackToDestructiveMigration()
                    .addCallback(notasDataBaseCallback(scope))
                    .build()
                INSTANCE=instance
                return instance
            }
        }
    }
}