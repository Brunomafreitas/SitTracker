package ipvc.estg.cm.db

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import ipvc.estg.cm.DAO.notasDAO

import ipvc.estg.cm.ENTIDADES.notasPessoais

class notasRepository(private val notasNoDao: notasDAO) {

    val allNotes: LiveData<List<notasPessoais>> = notasNoDao.getNotasId()


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(notas: notasPessoais) {
        notasNoDao.insert(notas)
    }

    fun getNotasById(id: Int): LiveData<List<notasPessoais>>{
        return notasNoDao.getNotasPeloId(id)
    }

    suspend fun deleteNotasById(id:Int){
        notasNoDao.deleteNotaById(id);
    }

    suspend fun updateNotasById(corpoNota: String, tituloNota:String, id:Int){
        notasNoDao.updateNotaById(corpoNota, tituloNota, id);
    }
}