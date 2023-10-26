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


        binding.button.setOnClickListener {
            val stuid = binding.studentId.text.toString()
            val name = binding.studentName.text.toString()
            val email = binding.studentEmail.text.toString()
            val subject = binding.studentSub.text.toString()
            val birthdate = binding.studentBirthdate.text.toString()

            if (stuid.isNotEmpty() && name.isNotEmpty() && email.isNotEmpty() && subject.isNotEmpty() && birthdate.isNotEmpty()) {
                addData(stuid, name, email, subject, birthdate)
            }

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
        }
    }
}