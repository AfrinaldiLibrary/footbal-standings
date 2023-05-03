package com.klikgazz.footballstandings.ui.score

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
import com.klikgazz.footballstandings.adapter.ScoreAdapter
import com.klikgazz.footballstandings.databinding.ActivityInputScoreBinding
import com.klikgazz.footballstandings.model.ResultModel

class InputScoreActivity : AppCompatActivity(), View.OnClickListener {
    private var _binding : ActivityInputScoreBinding? = null
    private val binding get() = _binding!!
    private val adapter = ScoreAdapter()
    private var database : FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityInputScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database

        setData()
        actionListener()
    }

    private fun setData() {
        binding.apply {
            adapter.itemList.add("First Row")
            recyclerView.adapter = adapter
        }
    }

    private fun actionListener() {
        binding.apply {
            tvAdd.setOnClickListener(this@InputScoreActivity)
            btnSave.setOnClickListener(this@InputScoreActivity)
        }
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.tv_add -> {
                adapter.itemList.add("New Row")
                adapter.notifyItemInserted(adapter.itemList.size - 1)
            }
            R.id.btn_save -> {
                val data = adapter.dataScore

                if (data.isEmpty()){
                    Toast.makeText(this@InputScoreActivity, "Masukan minimal satu data", Toast.LENGTH_SHORT).show()
                }

                data.forEach{
                    if (it.value.clubHome.isNullOrBlank()){
                        Toast.makeText(this@InputScoreActivity, "Field tidak boleh kosong", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (it.value.scoreHome.isNullOrBlank()){
                        Toast.makeText(this@InputScoreActivity, "Field tidak boleh kosong", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (it.value.clubAway.isNullOrBlank()){
                        Toast.makeText(this@InputScoreActivity, "Field tidak boleh kosong", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (it.value.scoreAway.isNullOrBlank()){
                        Toast.makeText(this@InputScoreActivity, "Field tidak boleh kosong", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (it.value.clubHome.toString() == it.value.clubAway.toString()){
                        Toast.makeText(this@InputScoreActivity, "Nama klub tidak boleh sama", Toast.LENGTH_SHORT).show()
                        return
                    }
                }
                saveData(adapter.dataScore)
//                saveScore()

            }
        }
    }

    private fun saveData(dataScore: HashMap<String, ResultModel>) {
        dataScore.forEach{
            val myRef = database?.reference?.child("Clubs")

            myRef?.addListenerForSingleValueEvent(object: ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(it.value.clubHome.toString().lowercase()).exists() && snapshot.child(it.value.clubAway.toString().lowercase()).exists()) {
                        val match = snapshot.child(it.value.clubHome.toString().lowercase()).child("match").getValue(String::class.java)
                        val win = snapshot.child(it.value.clubHome.toString().lowercase()).child("win").getValue(String::class.java)
                        val lose = snapshot.child(it.value.clubHome.toString().lowercase()).child("lose").getValue(String::class.java)
                        val draw = snapshot.child(it.value.clubHome.toString().lowercase()).child("draw").getValue(String::class.java)
                        val goalWin = snapshot.child(it.value.clubHome.toString().lowercase()).child("goalWin").getValue(String::class.java)
                        val goalLose = snapshot.child(it.value.clubHome.toString().lowercase()).child("goalLose").getValue(String::class.java)
                        val point = snapshot.child(it.value.clubHome.toString().lowercase()).child("point").getValue(String::class.java)

                        val dataMatchAway = snapshot.child(it.value.clubAway.toString().lowercase()).child("match").getValue(String::class.java)
                        val dataWinAway = snapshot.child(it.value.clubAway.toString().lowercase()).child("win").getValue(String::class.java)
                        val dataLoseAway = snapshot.child(it.value.clubAway.toString().lowercase()).child("lose").getValue(String::class.java)
                        val dataDrawAway = snapshot.child(it.value.clubAway.toString().lowercase()).child("draw").getValue(String::class.java)
                        val dataGoalWinAway = snapshot.child(it.value.clubAway.toString().lowercase()).child("goalWin").getValue(String::class.java)
                        val dataGoalLoseAway = snapshot.child(it.value.clubAway.toString().lowercase()).child("goalLose").getValue(String::class.java)
                        val dataPointAway = snapshot.child(it.value.clubAway.toString().lowercase()).child("point").getValue(String::class.java)

                        var homeMatch : String? = null
                        var homeWin : String? = null
                        var homeLose : String? = null
                        var homeDraw : String? = null
                        var homeGoalWin : String? = null
                        var homeGoalLose : String? = null
                        var homePoint : String? = null

                        var awayMatch : String? = null
                        var awayWin : String? = null
                        var awayLose : String? = null
                        var awayDraw : String? = null
                        var awayGoalWin : String? = null
                        var awayGoalLose : String? = null
                        var awayPoint : String? = null


                        if (it.value.scoreHome.toString().toInt() > it.value.scoreAway.toString().toInt()) {
                            homeMatch = (match!!.toInt() + 1).toString()
                            homeWin = (win!!.toInt() + 1).toString()
                            homeLose = lose
                            homeDraw = draw
                            homeGoalWin = (goalWin!!.toInt() + it.value.scoreHome.toString().toInt()).toString()
                            homeGoalLose = (goalLose!!.toInt() + it.value.scoreAway.toString().toInt()).toString()
                            homePoint = (point!!.toInt() + 3).toString()

                            awayMatch = (dataMatchAway!!.toInt() + 1).toString()
                            awayWin = dataWinAway
                            awayLose = (dataLoseAway!!.toInt() + 1).toString()
                            awayDraw = dataDrawAway
                            awayGoalWin = (dataGoalWinAway!!.toInt() + it.value.scoreAway.toString().toInt()).toString()
                            awayGoalLose = (dataGoalLoseAway!!.toInt() + it.value.scoreHome.toString().toInt()).toString()
                            awayPoint = dataPointAway
                        } else if (it.value.scoreHome.toString().toInt() < it.value.scoreAway.toString().toInt()) {
                            homeMatch = (match!!.toInt() + 1).toString()
                            homeWin = win
                            homeLose = (lose!!.toInt() + 1).toString()
                            homeDraw = draw
                            homeGoalWin = (goalWin!!.toInt() + it.value.scoreHome.toString().toInt()).toString()
                            homeGoalLose = (goalLose!!.toInt() + it.value.scoreAway.toString().toInt()).toString()
                            homePoint = point

                            awayMatch = (dataMatchAway!!.toInt() + 1).toString()
                            awayWin = (dataWinAway!!.toInt() + 3).toString()
                            awayLose = dataLoseAway
                            awayDraw = dataDrawAway
                            awayGoalWin = (dataGoalWinAway!!.toInt() + it.value.scoreAway.toString().toInt()).toString()
                            awayGoalLose = (dataGoalLoseAway!!.toInt() + it.value.scoreHome.toString().toInt()).toString()
                            awayPoint = (dataPointAway!!.toInt() + 3).toString()
                        } else if (it.value.scoreHome.toString().toInt() == it.value.scoreAway.toString().toInt()) {
                            homeMatch = (match!!.toInt() + 1).toString()
                            homeWin = win
                            homeLose = lose
                            homeDraw = (draw!!.toInt() + 1).toString()
                            homeGoalWin = (goalWin!!.toInt() + it.value.scoreHome.toString().toInt()).toString()
                            homeGoalLose = (goalLose!!.toInt() + it.value.scoreAway.toString().toInt()).toString()
                            homePoint = (point!!.toInt() + 1).toString()

                            awayMatch = (dataMatchAway!!.toInt() + 1).toString()
                            awayWin = dataWinAway
                            awayLose = dataLoseAway
                            awayDraw = (dataDrawAway!!.toInt() + 1).toString()
                            awayGoalWin = (dataGoalWinAway!!.toInt() + it.value.scoreAway.toString().toInt()).toString()
                            awayGoalLose = (dataGoalLoseAway!!.toInt() + it.value.scoreHome.toString().toInt()).toString()
                            awayPoint = (dataPointAway!!.toInt() + 1).toString()
                        }

                        myRef.child(it.value.clubHome.toString().lowercase()).child("match").setValue(homeMatch)
                        myRef.child(it.value.clubHome.toString().lowercase()).child("win").setValue(homeWin)
                        myRef.child(it.value.clubHome.toString().lowercase()).child("lose").setValue(homeLose)
                        myRef.child(it.value.clubHome.toString().lowercase()).child("draw").setValue(homeDraw)
                        myRef.child(it.value.clubHome.toString().lowercase()).child("goalWin").setValue(homeGoalWin)
                        myRef.child(it.value.clubHome.toString().lowercase()).child("goalLose").setValue(homeGoalLose)
                        myRef.child(it.value.clubHome.toString().lowercase()).child("point").setValue(homePoint)

                        myRef.child(it.value.clubAway.toString().lowercase()).child("match").setValue(awayMatch)
                        myRef.child(it.value.clubAway.toString().lowercase()).child("win").setValue(awayWin)
                        myRef.child(it.value.clubAway.toString().lowercase()).child("lose").setValue(awayLose)
                        myRef.child(it.value.clubAway.toString().lowercase()).child("draw").setValue(awayDraw)
                        myRef.child(it.value.clubAway.toString().lowercase()).child("goalWin").setValue(awayGoalWin)
                        myRef.child(it.value.clubAway.toString().lowercase()).child("goalLose").setValue(awayGoalLose)
                        myRef.child(it.value.clubAway.toString().lowercase()).child("point").setValue(awayPoint)

                        Toast.makeText(this@InputScoreActivity, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@InputScoreActivity, "Nama klub tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@InputScoreActivity, error.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


//    private fun saveScore() {
//        binding.apply {
//            if (etClubNameHome.text.isNullOrBlank()){
//                etClubNameHome.error = "Wajib di isi"
//                return
//            }
//
//            if (etClubNameAway.text.isNullOrBlank()){
//                etClubNameAway.error = "Wajib di isi"
//                return
//            }
//
//            if (etScoreHome.text.isNullOrBlank()){
//                etScoreHome.error = "Wajib di isi"
//                return
//            }
//
//            if (etScoreAway.text.isNullOrBlank()){
//                etScoreAway.error = "Wajib di isi"
//                return
//            }
//            if (etClubNameHome.text.toString().lowercase() == etClubNameAway.text.toString().lowercase()){
//                Toast.makeText(this@InputScoreActivity, "Nama klub tidak boleh sama", Toast.LENGTH_SHORT).show()
//                return
//            }
//            setScore()
//        }
//    }

//    private fun setScore() {
//        binding.apply {
//            val myRef = database?.reference?.child("Clubs")
//
//            myRef?.addListenerForSingleValueEvent(object: ValueEventListener {
//
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.child(etClubNameHome.text.toString().lowercase()).exists() && snapshot.child(etClubNameAway.text.toString().lowercase()).exists()) {
//                        val match = snapshot.child(etClubNameHome.text.toString().lowercase()).child("match").getValue(String::class.java)
//                        val win = snapshot.child(etClubNameHome.text.toString().lowercase()).child("win").getValue(String::class.java)
//                        val lose = snapshot.child(etClubNameHome.text.toString().lowercase()).child("lose").getValue(String::class.java)
//                        val draw = snapshot.child(etClubNameHome.text.toString().lowercase()).child("draw").getValue(String::class.java)
//                        val goalWin = snapshot.child(etClubNameHome.text.toString().lowercase()).child("goalWin").getValue(String::class.java)
//                        val goalLose = snapshot.child(etClubNameHome.text.toString().lowercase()).child("goalLose").getValue(String::class.java)
//                        val point = snapshot.child(etClubNameHome.text.toString().lowercase()).child("point").getValue(String::class.java)
//
//                        val dataMatchAway = snapshot.child(etClubNameAway.text.toString().lowercase()).child("match").getValue(String::class.java)
//                        val dataWinAway = snapshot.child(etClubNameAway.text.toString().lowercase()).child("win").getValue(String::class.java)
//                        val dataLoseAway = snapshot.child(etClubNameAway.text.toString().lowercase()).child("lose").getValue(String::class.java)
//                        val dataDrawAway = snapshot.child(etClubNameAway.text.toString().lowercase()).child("draw").getValue(String::class.java)
//                        val dataGoalWinAway = snapshot.child(etClubNameAway.text.toString().lowercase()).child("goalWin").getValue(String::class.java)
//                        val dataGoalLoseAway = snapshot.child(etClubNameAway.text.toString().lowercase()).child("goalLose").getValue(String::class.java)
//                        val dataPointAway = snapshot.child(etClubNameAway.text.toString().lowercase()).child("point").getValue(String::class.java)
//
//                        var homeMatch : String? = null
//                        var homeWin : String? = null
//                        var homeLose : String? = null
//                        var homeDraw : String? = null
//                        var homeGoalWin : String? = null
//                        var homeGoalLose : String? = null
//                        var homePoint : String? = null
//
//                        var awayMatch : String? = null
//                        var awayWin : String? = null
//                        var awayLose : String? = null
//                        var awayDraw : String? = null
//                        var awayGoalWin : String? = null
//                        var awayGoalLose : String? = null
//                        var awayPoint : String? = null
//
//
//                        if (calculating() == 1) {
//                            homeMatch = (match!!.toInt() + 1).toString()
//                            homeWin = (win!!.toInt() + 1).toString()
//                            homeLose = lose
//                            homeDraw = draw
//                            homeGoalWin = (goalWin!!.toInt() + etScoreHome.text.toString().toInt()).toString()
//                            homeGoalLose = (goalLose!!.toInt() + etScoreAway.text.toString().toInt()).toString()
//                            homePoint = (point!!.toInt() + 3).toString()
//
//                            awayMatch = (dataMatchAway!!.toInt() + 1).toString()
//                            awayWin = dataWinAway
//                            awayLose = (dataLoseAway!!.toInt() + 1).toString()
//                            awayDraw = dataDrawAway
//                            awayGoalWin = (dataGoalWinAway!!.toInt() + etScoreAway.text.toString().toInt()).toString()
//                            awayGoalLose = (dataGoalLoseAway!!.toInt() + etScoreHome.text.toString().toInt()).toString()
//                            awayPoint = dataPointAway
//                        } else if (calculating() == 2) {
//                            homeMatch = (match!!.toInt() + 1).toString()
//                            homeWin = win
//                            homeLose = (lose!!.toInt() + 1).toString()
//                            homeDraw = draw
//                            homeGoalWin = (goalWin!!.toInt() + etScoreHome.text.toString().toInt()).toString()
//                            homeGoalLose = (goalLose!!.toInt() + etScoreAway.text.toString().toInt()).toString()
//                            homePoint = point
//
//                            awayMatch = (dataMatchAway!!.toInt() + 1).toString()
//                            awayWin = (dataWinAway!!.toInt() + 3).toString()
//                            awayLose = dataLoseAway
//                            awayDraw = dataDrawAway
//                            awayGoalWin = (dataGoalWinAway!!.toInt() + etScoreAway.text.toString().toInt()).toString()
//                            awayGoalLose = (dataGoalLoseAway!!.toInt() + etScoreHome.text.toString().toInt()).toString()
//                            awayPoint = (dataPointAway!!.toInt() + 3).toString()
//                        } else if (calculating() == 3) {
//                            homeMatch = (match!!.toInt() + 1).toString()
//                            homeWin = win
//                            homeLose = lose
//                            homeDraw = (draw!!.toInt() + 1).toString()
//                            homeGoalWin = (goalWin!!.toInt() + etScoreHome.text.toString().toInt()).toString()
//                            homeGoalLose = (goalLose!!.toInt() + etScoreAway.text.toString().toInt()).toString()
//                            homePoint = (point!!.toInt() + 1).toString()
//
//                            awayMatch = (dataMatchAway!!.toInt() + 1).toString()
//                            awayWin = dataWinAway
//                            awayLose = dataLoseAway
//                            awayDraw = (dataDrawAway!!.toInt() + 1).toString()
//                            awayGoalWin = (dataGoalWinAway!!.toInt() + etScoreAway.text.toString().toInt()).toString()
//                            awayGoalLose = (dataGoalLoseAway!!.toInt() + etScoreHome.text.toString().toInt()).toString()
//                            awayPoint = (dataPointAway!!.toInt() + 1).toString()
//                        }
//
//                        myRef.child(etClubNameHome.text.toString().lowercase()).child("match").setValue(homeMatch)
//                        myRef.child(etClubNameHome.text.toString().lowercase()).child("win").setValue(homeWin)
//                        myRef.child(etClubNameHome.text.toString().lowercase()).child("lose").setValue(homeLose)
//                        myRef.child(etClubNameHome.text.toString().lowercase()).child("draw").setValue(homeDraw)
//                        myRef.child(etClubNameHome.text.toString().lowercase()).child("goalWin").setValue(homeGoalWin)
//                        myRef.child(etClubNameHome.text.toString().lowercase()).child("goalLose").setValue(homeGoalLose)
//                        myRef.child(etClubNameHome.text.toString().lowercase()).child("point").setValue(homePoint)
//
//                        myRef.child(etClubNameAway.text.toString().lowercase()).child("match").setValue(awayMatch)
//                        myRef.child(etClubNameAway.text.toString().lowercase()).child("win").setValue(awayWin)
//                        myRef.child(etClubNameAway.text.toString().lowercase()).child("lose").setValue(awayLose)
//                        myRef.child(etClubNameAway.text.toString().lowercase()).child("draw").setValue(awayDraw)
//                        myRef.child(etClubNameAway.text.toString().lowercase()).child("goalWin").setValue(awayGoalWin)
//                        myRef.child(etClubNameAway.text.toString().lowercase()).child("goalLose").setValue(awayGoalLose)
//                        myRef.child(etClubNameAway.text.toString().lowercase()).child("point").setValue(awayPoint)
//                    } else {
//                        Toast.makeText(this@InputScoreActivity, "Nama klub tidak ditemukan", Toast.LENGTH_SHORT).show()
//                    }
//
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Toast.makeText(this@InputScoreActivity, error.message, Toast.LENGTH_SHORT).show()
//                }
//            })
//        }
//    }

//    private fun calculating() : Int {
//        binding.apply {
//            return if (etScoreHome.text.toString().toInt() > etScoreAway.text.toString().toInt()){
//                1
//            } else if (etScoreHome.text.toString().toInt() < etScoreAway.text.toString().toInt()) {
//                2
//            } else if (etScoreHome.text.toString().toInt() == etScoreAway.text.toString().toInt()){
//                3
//            } else {
//                0
//            }
//        }
//    }
}