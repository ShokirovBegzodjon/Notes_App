package com.apphub.notesapp.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.BaseColumns
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.apphub.notesapp.R
import com.apphub.notesapp.database.NoteDatabase
import com.apphub.notesapp.database.NoteEmpty
import com.apphub.notesapp.databinding.FragmentEditNoteBinding
import com.apphub.notesapp.lists.ColorList
import com.apphub.notesapp.rvadapter.RvColorAdapter
import com.bumptech.glide.Glide

class EditNoteFragment : Fragment(R.layout.fragment_edit_note), RvColorAdapter.OnItemClickListener {
    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!
    private var id: Int = 0
    private val noteDatabase get() = NoteDatabase.createDatabase(requireContext())
    private var color:Int = ColorList.getColor(1).color
    private var selectedImageUri: String =""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditNoteBinding.bind(view)

        //ranglarni lisga o'zlashtirish
        id = arguments?.getInt(BaseColumns._ID)!!
        val note = noteDatabase.noteDao().getNotesById(id)
        color = note.color

        //malumotlarni viewga uzatish
        binding.title.setText(note.title)
        binding.description.setText(note.description)

        // rasm bor bo'lsa qo'yish yo'q bo'lsa ko'rsatmaslik
        selectedImageUri = note.pic
        if (selectedImageUri.isNotEmpty()){
            Glide.with(binding.root.context).load(selectedImageUri).into(binding.pic)
            binding.deletePic.visibility = View.VISIBLE
        }

        // ranglarni co'rsatish
        val rvColorAdapter = RvColorAdapter(ColorList.getColors(color))
        binding.colorRv.adapter = rvColorAdapter
        rvColorAdapter.setOnClickListener(this)

        binding.backIcon.setOnClickListener {
            updateFragment(NotesFragment())
        }
        // malumotni saqlab note fragmentga qaytish
        binding.updateNote.setOnClickListener {
            val noteEmpty = NoteEmpty(
                note.id,
                binding.title.text.toString(),
                binding.description.text.toString(),
                note.theDate,
                color,
                selectedImageUri
            )
            noteDatabase.noteDao().updateNote(noteEmpty)
            updateFragment(NotesFragment())
        }

        // rasm tanlash
        binding.pic.setOnClickListener {
            if (selectedImageUri.isEmpty()) {
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, 0)
            }
        }

        // rasmni o'chirib tashlash
        binding.deletePic.setOnClickListener {
            binding.pic.setImageResource(R.drawable.icons_flat_add_image)
            binding.deletePic.visibility = View.GONE
            selectedImageUri = ""
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data.toString()
        }
        if (selectedImageUri.isNotEmpty()){
            Glide.with(binding.root.context).load(selectedImageUri).into(binding.pic)
            binding.deletePic.visibility = View.VISIBLE
        }
        else{
            Log.d("uri invalid", "Uri = $selectedImageUri")
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

    //bosilgan rangni aniqlash
    override fun setonItemClickListener(color: Int) {
        this.color = color
    }
}
