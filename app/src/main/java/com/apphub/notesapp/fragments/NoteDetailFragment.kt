package com.apphub.notesapp.fragments

import android.os.Bundle
import android.provider.BaseColumns
import android.view.View
import androidx.fragment.app.Fragment
import com.apphub.notesapp.R
import com.apphub.notesapp.database.NoteDatabase
import com.apphub.notesapp.databinding.FragmentNoteDetailBinding
import com.bumptech.glide.Glide

class NoteDetailFragment : Fragment(R.layout.fragment_note_detail) {
    private var _binding: FragmentNoteDetailBinding? = null
    private val binding get() = _binding!!
    private val noteDatabase get() = NoteDatabase.createDatabase(requireContext())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNoteDetailBinding.bind(view)

        // argumentlarni o'zlashtirib olish
        val id = arguments?.getInt(BaseColumns._ID)
        val note = noteDatabase.noteDao().getNotesById(id!!)

        binding.backIcon.setOnClickListener {
            updateFragment(NotesFragment())
        }

        // malumotlarni uzatish
        binding.noteInfo.visibility = View.VISIBLE
        binding.title.text = note.title
        binding.description.text = note.description
        binding.theDate.text = note.theDate
        binding.colorView.setBackgroundColor(binding.root.context.getColor(note.color))
        if (note.pic.isNotEmpty()){
            Glide
                .with(requireContext())
                .load(note.pic)
                .placeholder(R.drawable.icons_flat_add_image)
                .into(binding.pic)

            binding.picLayout.visibility =View.VISIBLE
        }

        binding.editorIcon.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(BaseColumns._ID,id)

            val editNoteFragment = EditNoteFragment()
            editNoteFragment.arguments = bundle

            updateFragment(editNoteFragment)
        }
    }

    private fun updateFragment(fragment: Fragment){
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_view,fragment)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}