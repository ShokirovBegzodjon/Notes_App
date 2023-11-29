package com.apphub.notesapp.rvadapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apphub.notesapp.data.NoteGroupData
import com.apphub.notesapp.databinding.NoteGroupItemBinding

class RvNoteGroupAdapter(
    private val noteGroupList: MutableList<NoteGroupData>
):
    RecyclerView.Adapter<RvNoteGroupAdapter.ViewHolder>(), RvNoteAdapter.OnItemClickListener {
    private var listener: OnItemClickListener? = null
    private val con = this
    private var noteGroupPosition: Int? = null
    inner class ViewHolder(private val binding: NoteGroupItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(noteGroupList: NoteGroupData, position: Int) {
            binding.date.text = noteGroupList.date

            val adapter = RvNoteAdapter(noteGroupList.notes,position)
            adapter.setOnClickListener(con)
            binding.rvNote.adapter = adapter

            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION){
                    noteGroupPosition = adapterPosition
                }
            }
        }
    }

    fun setOnClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun setOnItemClickListener(noteGroupPosition: Int, notePosition: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            NoteGroupItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = noteGroupList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(noteGroupList[position],position)
    }

    override fun setOnItemClickListener(notePosition: Int, bigPosition: Int) {
        listener?.setOnItemClickListener(bigPosition,notePosition)
    }

}