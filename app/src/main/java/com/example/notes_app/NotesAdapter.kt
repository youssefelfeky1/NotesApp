package com.example.notes_app

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class NotesAdapter(private val context: Context, private val notes: ArrayList<Note>) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private val mRef = FirebaseDatabase.getInstance().getReference("Notes")


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        )
    }


    override fun getItemCount(): Int {
        return notes.size
    }


    @SuppressLint("InflateParams", "CutPasteId", "SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.title.text=notes[position].title
        holder.date.text=notes[position].timeStamp


        holder.itemView.setOnClickListener {
            val noteIntent = Intent(context,NoteActivity::class.java)
            noteIntent.putExtra("Title_Key",notes[position].title)
            noteIntent.putExtra("Note_Key",notes[position].note)
            context.startActivity(noteIntent)

        }

       holder.itemView.setOnLongClickListener {

           val alertBuilder = AlertDialog.Builder(context)
           val view = LayoutInflater.from(context).inflate(R.layout.delete_update_note,null,false)
           val alertDialog =alertBuilder.create()
           alertDialog.setView(view)
           alertDialog.show()

           val updatedTitle:EditText= view.findViewById(R.id.titleDeleteEdtTxt)
           val updatedNote:EditText = view.findViewById(R.id.noteDeleteEdtTxt)

           updatedTitle.setText(notes[position].title)
           updatedNote.setText(notes[position].note)

           val updateBtn:TextView = view.findViewById(R.id.updateNoteBtn)
           val deleteBtn:TextView = view.findViewById(R.id.deleteNoteBtn)



           // Update Button
           updateBtn.setOnClickListener {
               val title = view.findViewById<EditText>(R.id.titleDeleteEdtTxt).text.toString()
               val note = view.findViewById<EditText>(R.id.noteDeleteEdtTxt).text.toString()

               val calender = Calendar.getInstance()
               val mdFormat= SimpleDateFormat("EEEE hh:mm a ")
               val strData = mdFormat.format(calender.time)

               mRef.child(notes[position].id!!).setValue(Note(notes[position].id!!,title,note,strData))
               alertDialog.dismiss()
           }

           deleteBtn.setOnClickListener {

               mRef.child(notes[position].id!!).removeValue()
               alertDialog.dismiss()

           }




         false

       }
    }

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView =itemView.findViewById(R.id.titleTxtView)
        val date:TextView=itemView.findViewById(R.id.dateTxtView)


    }

}