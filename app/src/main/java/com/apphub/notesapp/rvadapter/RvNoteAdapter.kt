package com.apphub.notesapp.rvadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apphub.notesapp.database.NoteEmpty
import com.apphub.notesapp.databinding.NoteItemBinding
import com.bumptech.glide.Glide

class RvNoteAdapter(private val noteList: MutableList<NoteEmpty>,val positionBig: Int):
    RecyclerView.Adapter<RvNoteAdapter.ViewHolder>(){
    private var listener: OnItemClickListener? = null

    inner class ViewHolder(private val binding: NoteItemBinding): RecyclerView.ViewHolder(binding.root){
        fun onBind(noteData: NoteEmpty) {
            binding.title.text = noteData.title
            binding.description.text = noteData.description
            binding.colorView.setBackgroundColor(binding.root.context.getColor(noteData.color))
            if (noteData.pic.isNotEmpty()){
                Glide.with(binding.root.context).load(noteData.pic).into(binding.notePic)
            }
            else{
                binding.picLayout.visibility = View.INVISIBLE
            }
            binding.root.setOnClickListener{
                if (adapterPosition != RecyclerView.NO_POSITION){
                    listener?.setOnItemClickListener(adapterPosition,positionBig)
                }
            }
        }
    }

    fun setOnClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    interface OnItemClickListener{
        fun setOnItemClickListener(notePosition: Int,bigPosition:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        )
    }

    override fun getItemCount(): Int = noteList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(noteList[position])
    }
}