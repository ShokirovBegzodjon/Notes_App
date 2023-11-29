package com.apphub.notesapp.fragments

import android.os.Bundle
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.apphub.notesapp.R
import com.apphub.notesapp.databinding.FragmentWithoutNotesBinding

class WithoutNotesFragment : Fragment(R.layout.fragment_without_notes) {

    private var _binding : FragmentWithoutNotesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWithoutNotesBinding.bind(view)

        // addFragmentga o'tish
        binding.createNote.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container_view,AddNoteFragment()).commit()
        }

        val drawer: DrawerLayout? = requireActivity().findViewById(R.id.drawer_layout)
        binding.menuIcon.setOnClickListener {
            drawer?.open()
        }
    }

    //bindingni tozalash
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}