package com.apphub.notesapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.DeleteTable
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {
    @Insert
    fun insertNote(note: NoteEmpty)

    @Update
    fun updateNote(note: NoteEmpty)

    @Delete
    fun deleteNote(note: NoteEmpty)

    @Query("SELECT * FROM notes_table")
    fun getAllNotes(): MutableList<NoteEmpty>

    @Query("SELECT * FROM notes_table WHERE id = :id")
    fun getNotesById(id: Int): NoteEmpty

    @Query("DELETE FROM notes_table")
    fun deleteAllNotes()
}