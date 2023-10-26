package org.techtales.studentdetailsappassessment_04

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import org.techtales.studentdetailsappassessment_04.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), DataAdapter.ItemClickListener {

    private lateinit var binding: ActivityMainBinding

    private val db = FirebaseFirestore.getInstance()
    private val dataCollection = db.collection("data")
    private val data = mutableListOf<Data>()
    private lateinit var adapter: DataAdapter
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = DataAdapter(data, this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.button.setOnClickListener {
            val stuid = binding.studentId.text.toString()
            val name = binding.studentName.text.toString()
            val email = binding.studentEmail.text.toString()
            val subject = binding.studentSub.text.toString()
            val birthdate = binding.studentBirthdate.text.toString()

            if (stuid.isNotEmpty() && name.isNotEmpty() && email.isNotEmpty() && subject.isNotEmpty() && birthdate.isNotEmpty()) {
                addData(stuid, name, email, subject, birthdate)


            }


        }
        fetchData()

    }



private fun fetchData() {
    dataCollection
        .orderBy("timestamp", Query.Direction.DESCENDING)
        .get()
        .addOnSuccessListener {
            data.clear()
            for (document in it) {
                val item = document.toObject(Data::class.java)
                item.id = document.id
                data.add(item)
            }
            adapter.notifyDataSetChanged()
        }
        .addOnFailureListener {
            Toast.makeText(this, "Data Fetch Failed", Toast.LENGTH_SHORT).show()
        }
}

private fun addData(stuid: String, name: String, email: String, subject: String, birthdate: String) {
    val newData = Data(studendtid = stuid, name = name,email= email,subject=subject, birthdate = birthdate, timestamp = Timestamp.now())
    dataCollection.add(newData)
        .addOnSuccessListener {
            newData.id = it.id
            data.add(newData)
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Data added Successfully", Toast.LENGTH_SHORT).show()
            binding.studentId.text?.clear()
            binding.studentName.text?.clear()
            binding.studentEmail.text?.clear()
            binding.studentSub.text?.clear()
            binding.studentBirthdate.text?.clear()
            fetchData()
        }
        .addOnFailureListener {
            Toast.makeText(this, "Data added Failed", Toast.LENGTH_SHORT).show()
        }

}


    override fun onEditClick(data: Data) {
        binding.studentId.setText(data.id)
        binding.studentName.setText(data.name)
        binding.studentEmail.setText(data.email)
        binding.studentSub.setText(data.subject)
        binding.studentBirthdate.setText(data.birthdate)
        binding.button.text = "Update"
        binding.button.setOnClickListener {
            val updateStuid = binding.studentId.text.toString()
            val updateName = binding.studentName.text.toString()
            val updateEmail = binding.studentEmail.text.toString()
            val updateSubject = binding.studentSub.text.toString()
            val updateBirthdate = binding.studentBirthdate.text.toString()

            if (updateStuid.isNotEmpty() && updateName.isNotEmpty() && updateEmail.isNotEmpty() && updateSubject.isNotEmpty() && updateBirthdate.isNotEmpty()) {
                val updateData = Data(data.id, updateStuid, updateName,updateEmail,updateSubject,updateBirthdate,Timestamp.now())
                dataCollection.document(data.id!!)
                    .set(updateData)
                    .addOnSuccessListener {
                        binding.studentId.text?.clear()
                        binding.studentName.text?.clear()
                        binding.studentEmail.text?.clear()
                        binding.studentSub.text?.clear()
                        binding.studentBirthdate.text?.clear()
                        adapter.notifyDataSetChanged()
                        Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show()
                        startActivity((Intent(this, MainActivity::class.java)))
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Data updated Failed", Toast.LENGTH_SHORT).show()
                    }
            }


        }
    }

    override fun onDeleteItemClick(data: Data) {
    val dialog = AlertDialog.Builder(this)
    dialog.setTitle("Delete Files")
    dialog.setMessage("Do You want to Delete Files")
    dialog.setIcon(R.drawable.baseline_delete_24)
    dialog.setPositiveButton("YES") { dialogInterface, which ->
        dataCollection.document(data.id!!)
            .delete()
            .addOnSuccessListener {
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Data Delete Successfully", Toast.LENGTH_SHORT).show()
                fetchData()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Data Deletion Failed", Toast.LENGTH_SHORT).show()
            }
    }
    dialog.setNegativeButton("No") { dialogInterface, which ->
        //startActivity((Intent(this,MainActivity::class.java)))
    }

    val alertDialog: AlertDialog = dialog.create()
    alertDialog.setCancelable(false)
    alertDialog.show()
}
}