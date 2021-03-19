package ipvc.estg.cm.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import ipvc.estg.cm.ENTIDADES.notasPessoais
@Dao
interface notasDAO {
    @Query("SELECT * from notasPessoais ORDER BY id ASC")
    fun getNotasId(): LiveData<List<notasPessoais>>


    @Query("SELECT * from notasPessoais WHERE id == :id")
    fun getNotasPeloId(id:Int):LiveData<List<notasPessoais>>;


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notas: notasPessoais)

    @Query("DELETE FROM notasPessoais")
    suspend fun deleteAll()

    @Query("DELETE FROM notasPessoais WHERE id == :id")
     suspend fun deleteNotaById(id: Int);

    @Update
    suspend fun updateNota(notas: notasPessoais)

    @Query("UPDATE notasPessoais SET corpoNota =:corpoNota, tituloNota =:tituloNota WHERE id == :id")
    suspend fun updateNotaById(corpoNota: String, tituloNota: String, id: Int);

}