package com.apphub.notesapp.fragments

import android.app.Activity
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.apphub.notesapp.R
import com.apphub.notesapp.rvadapter.RvColorAdapter
import com.apphub.notesapp.database.NoteDatabase
import com.apphub.notesapp.database.NoteEmpty
import com.apphub.notesapp.databinding.FragmentAddNoteBinding
import com.apphub.notesapp.lists.ColorList
import com.bumptech.glide.Glide
import java.text.DateFormatSymbols
import java.util.Locale

class AddNoteFragment : Fragment(R.layout.fragment_add_note), RvColorAdapter.OnItemClickListener {
    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!
    private val noteDatabase get()  = NoteDatabase.createDatabase(binding.root.context)
    private var color:Int = ColorList.getColor(1).color
    private var selectedImageUri: String =""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddNoteBinding.bind(view)


        //ranglarni ko'rsatadigon rv
        val rvColorAdapter = RvColorAdapter(ColorList.getColors(color))
        binding.rvColor.adapter = rvColorAdapter
        rvColorAdapter.setOnClickListener(this)

        // malumotlarni daqlash
        binding.addNote.setOnClickListener {
            if (binding.title.text.toString().isNotEmpty()
                && binding.description.text.toString().isNotEmpty()
                ){
                val noteEmpty = NoteEmpty(
                    null,
                    binding.title.text.toString(),
                    binding.description.text.toString(),
                    getDateName(),
                    color,
                    selectedImageUri
                )
                noteDatabase.noteDao().insertNote(noteEmpty)
                Toast.makeText(context, "Qo'shildi", Toast.LENGTH_SHORT).show()
            }
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
            selectedImageUri =""
        }

        //qaytish
        binding.backIcon.setOnClickListener {
            updateFragment(NotesFragment())
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


    //oy indexini bersa oy nomini qaytaradi
    private fun getMonthName(month: Int): String {
        val symbols = DateFormatSymbols(Locale.ENGLISH)
        val monthNames = symbols.months
        return monthNames[month]
    }

    private fun getDateName(): String{
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = getMonthName(c.get(Calendar.MONTH))
        val day = c.get(Calendar.DAY_OF_MONTH)
        return "$month $day, $year"
    }

    // bosilgan rangni eslab qolish
    override fun setonItemClickListener(color: Int) {
        this.color = color
    }
}