package org.techtales.studentdetailsappassessment_04

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class DataAdapter(private val data:List<Data>, private val itemClickListener: MainActivity): RecyclerView.Adapter<DataAdapter.ViewHolder>(){

    interface ItemClickListener{
        fun onEditClick(data:Data)
        fun onDeleteItemClick(data:Data)
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val id = itemView.findViewById<TextView>(R.id.idTxt)
        val name = itemView.findViewById<TextView>(R.id.nameTxt)
        val email = itemView.findViewById<TextView>(R.id.emailTxt)
        val subject = itemView.findViewById<TextView>(R.id.subjectTxt)
        val birthdate = itemView.findViewById<TextView>(R.id.birthdateTxt)
        val edit = itemView.findViewById<ImageButton>(R.id.editBtn)
        val delete = itemView.findViewById<ImageButton>(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_list,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.id.text = item.id
        holder.name.text = item.name
        holder.email.text = item.email
        holder.subject.text = item.subject
        holder.birthdate.text = item.birthdate

        holder.edit.setOnClickListener{
            itemClickListener.onEditClick(item)

        }
        holder.delete.setOnClickListener{
            itemClickListener.onDeleteItemClick(item)
        }
    }
}