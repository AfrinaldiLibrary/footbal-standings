package com.klikgazz.footballstandings.ui.clubs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.klikgazz.footballstandings.R
import com.klikgazz.footballstandings.databinding.ActivityInputClubsBinding
import com.klikgazz.footballstandings.model.ClubsModel

class InputClubsActivity : AppCompatActivity(), View.OnClickListener {
    private var _binding : ActivityInputClubsBinding? = null
    private val binding get() = _binding!!
    private var database : FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityInputClubsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database

        actionListener()
    }

    private fun actionListener() {
        binding.apply {
            btnSave.setOnClickListener(this@InputClubsActivity)
        }
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_save -> {
                saveClub()
            }
        }
    }

    private fun saveClub() {
        binding.apply {
            if (etClubName.text.isNullOrBlank()){
                etClubName.error = "Wajib di isi"
                return
            }
            if (etCityClub.text.isNullOrBlank()){
                etCityClub.error = "Wajib di isi"
                return
            }

            progressBar.visibility = View.VISIBLE
            val myRef = database?.reference?.child("Clubs")
            val dataValue = ClubsModel(etClubName.text.toString().lowercase(), etCityClub.text.toString().lowercase())
            val valueListener = object: ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    progressBar.visibility = View.GONE
                    if (snapshot.child(etClubName.text.toString().lowercase()).exists()) {
                        Toast.makeText(this@InputClubsActivity, "Nama klub tersebut sudah terdaftar", Toast.LENGTH_SHORT).show()
                    } else {
                        val key = myRef?.child(etClubName.text.toString().lowercase())
                        key?.setValue(dataValue)
                        Toast.makeText(this@InputClubsActivity, "Nama klub berhasil didaftarkan", Toast.LENGTH_SHORT).show()
                    }

                    myRef?.removeEventListener(this)
                }

                override fun onCancelled(error: DatabaseError) {
                    progressBar.visibility = View.GONE
                    myRef?.removeEventListener(this)
                    Toast.makeText(this@InputClubsActivity, error.message, Toast.LENGTH_SHORT).show()
                }
            }
            myRef?.addValueEventListener(valueListener)
        }
    }
}