package com.apphub.notesapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import com.apphub.notesapp.database.NoteDatabase
import com.apphub.notesapp.databinding.ActivityMainBinding
import com.apphub.notesapp.fragments.AddNoteFragment
import com.apphub.notesapp.fragments.NotesFragment
import com.apphub.notesapp.fragments.WithoutNotesFragment
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener

class MainActivity : AppCompatActivity() , OnNavigationItemSelectedListener{
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val noteDatabase = NoteDatabase.createDatabase(this)
        if (noteDatabase.noteDao().getAllNotes().isEmpty()){
            updateFragment(WithoutNotesFragment())
        }
        else{
            updateFragment(NotesFragment())
        }

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.open,
            R.string.close
        )

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationDrawer.setNavigationItemSelectedListener(this)
    }

    private fun updateFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container_view,fragment).commit()
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete_all_note -> {
                val noteData = NoteDatabase.createDatabase(this)
                noteData.noteDao().deleteAllNotes()
                updateFragment(WithoutNotesFragment())
            }
            R.id.app_info -> {}
            R.id.join_our_telegram ->{
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/my_app_project")))
            }
            R.id.contact_us -> {
                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:+998942002544")))
            }
            R.id.add_note -> updateFragment(AddNoteFragment())
        }
        binding.drawerLayout.close()
        return true
    }
}