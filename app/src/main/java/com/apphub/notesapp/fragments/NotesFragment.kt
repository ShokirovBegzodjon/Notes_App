package com.apphub.notesapp.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Bundle
import android.provider.BaseColumns
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.apphub.notesapp.R
import com.apphub.notesapp.data.NoteGroupData
import com.apphub.notesapp.database.NoteDatabase
import com.apphub.notesapp.database.NoteEmpty
import com.apphub.notesapp.databinding.FragmentNotesBinding
import com.apphub.notesapp.rvadapter.RvNoteGroupAdapter
import java.text.DateFormatSymbols
import java.util.Locale

class NotesFragment : Fragment(R.layout.fragment_notes),RvNoteGroupAdapter.OnItemClickListener {
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private val noteGroupList: MutableList<NoteGroupData> = ArrayList()
    private var noteList: MutableList<NoteEmpty> = ArrayList()
    private val noteDatabase get() = NoteDatabase.createDatabase(requireContext())
    private val PERMISSION_REQUEST_CODE: Int = 100


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNotesBinding.bind(view)

        //recycler vievni chizish
        initNoteList()
        initNoteGroupList(noteList)
        drawRvNoteGroup(noteGroupList)
        // + button bosilganda addFragmentga o'tish
        binding.addNote.setOnClickListener {
            updateFragment(AddNoteFragment())
        }

        binding.menuIcon.setOnClickListener {
            val drawer: DrawerLayout? = requireActivity().findViewById(R.id.drawer_layout)
            drawer?.open()
        }

        //search barni yopish
        binding.cancelSearch.setOnClickListener {
            binding.searchLayout.visibility = View.GONE
        }

        binding.searchIcon.setOnClickListener {
            binding.searchLayout.visibility = View.VISIBLE
        }
        // search qismi yani qidirish logikasi yozilgan
        binding.txtSearch.addTextChangedListener( object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                drawRvNoteGroup(getSearchList(text.toString()))
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    drawRvNoteGroup(noteGroupList)
                }
            }
        } )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, set the image URI
                drawRvNoteGroup(noteGroupList)
            } else {
                activity?.finish()
            }
        }
    }

    private fun drawRvNoteGroup(noteGroupList: MutableList<NoteGroupData>){
        val rvNoteGroupAdapter = RvNoteGroupAdapter(noteGroupList)
        rvNoteGroupAdapter.setOnClickListener(this)
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is granted, set the image URI
            binding.rvNoteGroup.adapter = rvNoteGroupAdapter
        } else {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun updateFragment(fragment: Fragment){
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_view,fragment)
            .commit()
    }

    // title si shu bo'lgan listlarni qaytaradi
    private fun getSearchList(text: String): MutableList<NoteGroupData> {
        val searchList: MutableList<NoteEmpty> = ArrayList()

        for (list in noteList){
            if (list.title.contains(text, ignoreCase = true)){
                searchList.add(list)
            }
        }
        initNoteGroupList(searchList)
        searchList.clear()
        return noteGroupList
    }

    private fun initNoteGroupList(noteList: MutableList<NoteEmpty>){
        noteGroupList.clear()
        if (noteList.isNotEmpty()){
            var data: String = noteList[0].theDate
            val note : MutableList<NoteEmpty> = ArrayList()
            for (i in 0 until noteList.size){
                if (noteList[i].theDate == data) {
                    note.add(noteList[i])
                }
                else {
                    noteGroupList.add(NoteGroupData(getDateName(data),ArrayList(note)))
                    note.clear()
                    note.add(noteList[i])
                    data = noteList[i].theDate
                }
            }
            noteGroupList.add(NoteGroupData(getDateName(data),ArrayList(note)))
        }
        noteGroupList.sortBy { it.date }
    }

    private fun initNoteList(){
        noteList = noteDatabase.noteDao().getAllNotes()
    }

    //bugun bo'sa today yoki kunni qaytaradi
    private fun getDateName(data: String): String {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = getMonthName(c.get(Calendar.MONTH))
        val day = c.get(Calendar.DAY_OF_MONTH)

        return when(data) {
            "$month $day, $year" -> "Today"
            "$month ${day-1}, $year" -> "Yesterday"
            else -> data
        }
    }

    private fun getMonthName(month: Int): String {
        val symbols = DateFormatSymbols(Locale.ENGLISH)
        val monthNames = symbols.months
        return monthNames[month]
    }

    private fun getId(noteGroupPosition: Int, notePosition: Int): Int {
        return noteGroupList[noteGroupPosition].notes[notePosition].id!!
    }

    //bindingni tozalash
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //bosilgan titleni eshitadi va uni boshqa fragmentga ko'rsatish uchun uzatadi
    override fun setOnItemClickListener(noteGroupPosition: Int, notePosition: Int) {

        val id  = getId(noteGroupPosition,notePosition)
        val bundle = Bundle()
        bundle.putInt(BaseColumns._ID,id)
        val noteDetailFragment = NoteDetailFragment()
        noteDetailFragment.arguments = bundle

        updateFragment(noteDetailFragment)

    }
}