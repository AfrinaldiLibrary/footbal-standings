package com.klikgazz.footballstandings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.klikgazz.footballstandings.adapter.TableAdapter
import com.klikgazz.footballstandings.databinding.ActivityMainBinding
import com.klikgazz.footballstandings.model.ClubsModel
import com.klikgazz.footballstandings.ui.clubs.InputClubsActivity
import com.klikgazz.footballstandings.ui.score.InputScoreActivity

class MainActivity : AppCompatActivity() {
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var database : FirebaseDatabase? = null
    private var listData : ArrayList<ClubsModel> = arrayListOf()
    private var adapter = TableAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setData()
    }

    private fun setData() {
        binding.apply {
            database = Firebase.database
            val myRef = database?.reference?.child("Clubs")

            myRef?.addValueEventListener(object: ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    listData.clear()
                    snapshot.children.forEach{
                        val data = it.getValue<ClubsModel>()
                        listData.add(data!!)
                    }

                    listData.sortByDescending {
                        it.point?.toInt()
                    }

                    if (listData.isEmpty()){
                        tvEmpty.visibility = View.VISIBLE
                    } else {
                        tvEmpty.visibility = View.GONE
                    }

                    adapter.setData(listData)
                    adapter.notifyDataSetChanged()
                    progressBar.visibility = View.GONE
                }

                override fun onCancelled(error: DatabaseError) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
                }
            })

            rvStandings.adapter = adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.to_input_club -> {
                toInputClub()
                true
            }
            R.id.to_input_score -> {
                toInputScore()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toInputScore() {
        Intent(this, InputScoreActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun toInputClub() {
        Intent(this, InputClubsActivity::class.java).also {
            startActivity(it)
        }
    }
}