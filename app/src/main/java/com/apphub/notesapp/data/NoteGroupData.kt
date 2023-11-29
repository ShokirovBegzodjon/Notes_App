package com.apphub.notesapp.data

import com.apphub.notesapp.database.NoteEmpty

data class NoteGroupData(
    val date: String,
    val notes: MutableList<NoteEmpty>
)
