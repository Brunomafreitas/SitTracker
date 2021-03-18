package ipvc.estg.cm.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ipvc.estg.cm.ENTIDADES.notasPessoais
import ipvc.estg.cm.db.NotasDB
import ipvc.estg.cm.db.notasRepository
import kotlinx.coroutines.launch


class NotasViewModel (application: Application) : AndroidViewModel(application) {
    private val repository: notasRepository
    var allNotes: LiveData<List<notasPessoais>>;

     init {
        val notasDAOVAR = NotasDB.getDatabase(application, viewModelScope).wordDao()
        repository = notasRepository(notasDAOVAR);
        allNotes = repository.allNotes;
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(notas: notasPessoais) = viewModelScope.launch {
        repository.insert(notas)
    }

    fun getNotasById(id: Int) :LiveData<List<notasPessoais>>{
        return repository.getNotasById(id);
    }

     fun deleteNotaById(id:Int)  = viewModelScope.launch{
         repository.deleteNotasById(id);
    }

    fun updateNotaById(corpoNota : String, tituloNota :String, id: Int ) = viewModelScope.launch{
        repository.updateNotasById(corpoNota,tituloNota,id);
    }

}



