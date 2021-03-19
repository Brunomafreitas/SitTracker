package ipvc.estg.cm.ENTIDADES

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notasPessoais")
data class notasPessoais(@PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "tituloNota") val tituloNota: String,
    @ColumnInfo(name = "corpoNota") val corpoNota: String
)