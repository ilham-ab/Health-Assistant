package com.example.note_health
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notesTable")
class Note(
    @ColumnInfo(name= "titre") val noteTitre:String,
    @ColumnInfo(name= "Descriptiom") val noteDescription:String,
    @ColumnInfo(name= "Date") val noteDate:String,
){
    @PrimaryKey(autoGenerate = true)
    var id = 0
}