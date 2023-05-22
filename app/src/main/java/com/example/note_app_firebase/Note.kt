package com.example.note_app_firebase

data class Note (
    var noteId : String ?= null,
    var noteTitre : String ?= null,
    var noteDescription : String ?= null,
    var noteDate: String ?= null,

    )