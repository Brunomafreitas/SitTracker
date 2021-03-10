package ipvc.estg.cm.db

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import ipvc.estg.cm.DAO.notasDAO

import ipvc.estg.cm.ENTIDADES.notasPessoais

class notasRepository(private val notasNoDao: notasDAO) {

    val allNotes: LiveData<List<notasPessoais>> = notasNoDao.getNotasAlfabeticamente()


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(notas: notasPessoais) {
        notasNoDao.insert(notas)
    }
}