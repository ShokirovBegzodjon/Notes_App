package com.apphub.notesapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class NoteEmpty(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "theDate")
    val theDate: String,
    @ColumnInfo(name = "color")
    val color: Int,
    @ColumnInfo(name = "picUri")
    val pic : String
)
