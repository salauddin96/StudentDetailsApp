package org.techtales.studentdetailsappassessment_04

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import org.techtales.studentdetailsappassessment_04.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

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
    val newData = Data(stuid = stuid, name = name,email= email,subject=subject, birthday = birthdate, timestamp = Timestamp.now())
    dataCollection.add(newData)
        .addOnSuccessListener {
            newData.id = it.id
            data.add(newData)
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Data added Successfully", Toast.LENGTH_SHORT).show()
            binding.idEtxt.text?.clear()
            binding.nameEtxt.text?.clear()
            binding.emailEtxt.text?.clear()
            binding.subjectEtxt.text?.clear()
            binding.birthdateEtxt.text?.clear()
            fetchData()
        }
        .addOnFailureListener {
            Toast.makeText(this, "Data added Failed", Toast.LENGTH_SHORT).show()
        }

}

override fun onEditItemClick(data: Data) {
    binding.idEtxt.setText(data.stuid)
    binding.nameEtxt.setText(data.name)
    binding.emailEtxt.setText(data.email)
    binding.subjectEtxt.setText(data.subject)
    binding.birthdateEtxt.setText(data.birthday)
    binding.addBtn.text = "Update"
    binding.addBtn.setOnClickListener {
        val updateStuid = binding.idEtxt.text.toString()
        val updateName = binding.nameEtxt.text.toString()
        val updateEmail = binding.emailEtxt.text.toString()
        val updateSubject = binding.subjectEtxt.text.toString()
        val updateBirthdate = binding.birthdateEtxt.text.toString()

        if (updateStuid.isNotEmpty() && updateName.isNotEmpty() && updateEmail.isNotEmpty() && updateSubject.isNotEmpty() && updateBirthdate.isNotEmpty()) {
            val updateData = Data(data.id, updateStuid, updateName,updateEmail,updateSubject,updateBirthdate,Timestamp.now())
            dataCollection.document(data.id!!)
                .set(updateData)
                .addOnSuccessListener {
                    binding.idEtxt.text?.clear()
                    binding.nameEtxt.text?.clear()
                    binding.emailEtxt.text?.clear()
                    binding.subjectEtxt.text?.clear()
                    binding.birthdateEtxt.text?.clear()
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
    dialog.setIcon(R.drawable.img)
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