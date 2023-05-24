package com.example.health_ass

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NoteRVAdapter( val noteList: ArrayList<Note>) :
    RecyclerView.Adapter<NoteRVAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }



    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.note_rv_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.noteTV.setText(noteList.get(position).noteTitre)
        holder.timeTV.setText("Last Updated : "+noteList.get(position).noteDate)
        holder.deleteTV.setOnClickListener{
            deleteNote(noteList.get(position).noteId.toString())
        }
    }

    private fun deleteNote(noteId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val dbRef = FirebaseDatabase.getInstance().getReference("Notes").child(userId.orEmpty()).child(noteId)
        dbRef.removeValue()


    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val noteTV : TextView = itemView.findViewById(R.id.idTitre)
        val timeTV : TextView = itemView.findViewById(R.id.idDate)
        val deleteTV = itemView.findViewById<ImageView>(R.id.idDelete)



        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }

    }

}

interface NoteClickDeleteInterface{
    fun onDeleteClick(id : String)
}