package com.example.notes_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.notes_app.databinding.ActivityNoteBinding

class NoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val title = intent.getStringExtra("Title_Key")
        val note = intent.getStringExtra("Note_Key")

        binding.titleShowTxtView.text=title
        binding.noteTxtView.text=note

    }
}