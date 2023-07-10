package com.example.notes_app

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notes_app.databinding.ActivityMainBinding
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database:FirebaseDatabase
    private lateinit var mRef:DatabaseReference
    private lateinit var mNoteList:ArrayList<Note>
    private lateinit var  notesAdapter:NotesAdapter

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database= FirebaseDatabase.getInstance()
        mRef=database.getReference("Notes")
        mNoteList= ArrayList()

        notesAdapter = NotesAdapter(this@MainActivity,mNoteList)
        binding.notesRecyclerView.layoutManager=LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
        binding.notesRecyclerView.adapter=notesAdapter





        binding.addBtn.setOnClickListener {
            showDialogAddNote()

        }

    }


    override fun onStart() {
        super.onStart()

        mRef.addValueEventListener(object :ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                mNoteList.clear()
                for(n in snapshot.children)
                {
                    val note = n.getValue(Note::class.java)
                    mNoteList.add(0,note!!)
                }
                notesAdapter.notifyDataSetChanged()



            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }


    @SuppressLint("SimpleDateFormat")
    private fun showDialogAddNote(){
        val alertBuilder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.add_note,null)
        alertBuilder.setView(view)
        val alertDialog = alertBuilder.create()
        alertDialog.show()


        val addNoteBtn = view.findViewById<TextView>(R.id.AddNoteBtn)


        addNoteBtn.setOnClickListener {

            val titleEdtTxt = view.findViewById<EditText>(R.id.titleEdtTxt)
            val noteEdtTxt = view.findViewById<EditText>(R.id.noteEdtTxt)

            val title =titleEdtTxt.text.toString()
            val note = noteEdtTxt.text.toString()

            if(title.isNotEmpty() and note.isNotEmpty()){

                val id:String? = mRef.push().key


                val calender = Calendar.getInstance()
                val mdFormat=SimpleDateFormat("EEEE hh:mm a ")
                val strData = mdFormat.format(calender.time)



                val myNote=Note(id!!,title,note,strData)

                mRef.child(id).setValue(myNote)
                titleEdtTxt.text.clear()
                noteEdtTxt.text.clear()
                Toast.makeText(this@MainActivity,"Note Added Successfully",Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()

            }
            else{ Toast.makeText(this@MainActivity,"Please, Enter  title and note!",Toast.LENGTH_LONG).show() }



        }

    }
}