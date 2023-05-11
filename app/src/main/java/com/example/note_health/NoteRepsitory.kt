package com.example.note_health

import androidx.lifecycle.LiveData

class NoteRepsitory(private val notesDao : NoteDao) {
    val allNotes : LiveData<List<Note>> = notesDao.getAllNotes()
    suspend fun insert(note : Note){
        notesDao.insert(note)
    }
    suspend fun delete(note : Note){
        notesDao.delete(note)
    }
    suspend fun update(note: Note){
        notesDao.update(note)
    }
}