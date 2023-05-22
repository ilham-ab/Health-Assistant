package com.example.health_ass
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class MyAdapter(private val contactlist: ArrayList<Contact>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    val dbref = FirebaseDatabase.getInstance().getReference("contacts");
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nomContact: TextView = itemView.findViewById(R.id.nameContact)
        val teleContact: TextView = itemView.findViewById(R.id.teleContact)
        val delt:ImageView = itemView.findViewById(R.id.dlt)
        val phonebtn:ImageView = itemView.findViewById(R.id.phone)
        val smsbtn:ImageView = itemView.findViewById(R.id.sms)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.contact_item,parent,false)
        return MyViewHolder(itemView)    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.nomContact.text = contactlist[position].nomcontact
        holder.teleContact.text = contactlist[position].telecontact
        val item = contactlist[position]

        holder.delt.setOnClickListener{
            val itemId = item.idcontact
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("supprimer contact d'urgence")
            builder.setMessage("Tu es sûre ! ")
            builder.setIcon(R.drawable.delete)
            builder.setCancelable(false)
            builder.setPositiveButton("Oui"){_,_->
                //  Log.d("TAG", "itemId = " + itemId.toString())
                dbref.child(itemId.toString()).removeValue()
                // dbref.child("-NSinvITqamAZ5bm2Jxx").removeValue()
                Toast.makeText(holder.itemView.context, "Contact Supprimé !", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("Non") { _, _ ->

            }
            val alertDialog = builder.create()
            alertDialog.show()

        }
        holder.phonebtn.setOnClickListener{
            // val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel: "+ item.telecontact))
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel: "+ item.telecontact)) //need user permission
            holder.itemView.context.startActivity(intent)
        }
        if (ContextCompat.checkSelfPermission(holder.itemView.context, Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(holder.itemView.context as Activity, arrayOf(Manifest.permission.CALL_PHONE), 100)
        }
        holder.smsbtn.setOnClickListener{
            val message = "Message d'alert !!!" // default message to send

            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(item.telecontact, null, message, null, null)
        }
        val permission = Manifest.permission.SEND_SMS
        if (ContextCompat.checkSelfPermission(holder.itemView.context, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(holder.itemView.context as Activity, arrayOf(permission), 101)
        }
    }

    override fun getItemCount(): Int {
        return contactlist.size
    }

    override fun getItemId(position: Int): Long {
        return contactlist[position].idcontact.hashCode().toLong()
    }
}