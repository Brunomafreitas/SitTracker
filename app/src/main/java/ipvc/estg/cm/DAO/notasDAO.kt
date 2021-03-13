package ipvc.estg.cm.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ipvc.estg.cm.ENTIDADES.notasPessoais
@Dao
interface notasDAO {
    @Query("SELECT * from notasPessoais ORDER BY id ASC")
    fun getNotasAlfabeticamente(): LiveData<List<notasPessoais>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notas: notasPessoais)

    @Query("DELETE FROM notasPessoais")
    suspend fun deleteAll()


}