package com.example.health_ass

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class MedecineAdapter(private val MedList: ArrayList<MedecineModel>) :
    RecyclerView.Adapter<MedecineAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.medecines_items, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMed = MedList[position]
        holder.tvMedName.text = currentMed.medecineName
        holder.tvMedtime1.text = currentMed.medecineTime1
        holder.tvMedtime2.text = currentMed.medecineTime2
        holder.tvMedtime3.text = currentMed.medecineTime3



    }

    override fun getItemCount(): Int {
        return MedList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val tvMedName : TextView = itemView.findViewById(R.id.mra_med_name)
        val tvMedtime1 : TextView = itemView.findViewById(R.id.mra_med_time1)
        val tvMedtime2 : TextView = itemView.findViewById(R.id.mra_med_time2)
        val tvMedtime3 : TextView = itemView.findViewById(R.id.mra_med_time3)



        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }

    }

}
