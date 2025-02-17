package com.dileeppatel.YogaPlanner

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.dileeppatel.YogaPlanner.databinding.ActivityFinishBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FinishActivity : AppCompatActivity() {
    private var binding : ActivityFinishBinding? = null
    private var player: MediaPlayer? = null
    private var exerciseDao: ExerciseDao? = null
    private val cal = Calendar.getInstance()
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.tbFinishPage)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.tbFinishPage?.setNavigationOnClickListener { view->
            stopPlayer()
            onBackPressed()
        }

        exerciseDao =(application as WorkoutApplication).db.exerciseDao()

        binding?.btnFinish?.setOnClickListener{ view ->

            if( binding?.etName?.text?.isNotEmpty() == true ){
                val name = binding?.etName?.text.toString()

                lifecycleScope.launch{
                    exerciseDao!!.insert( ExerciseEntity( title = name, time = sdf.format(cal.time).toString() ) )
                }
                stopPlayer()
                finish()
            }else{
                Snackbar.make(view, "Enter your name to finish", Snackbar.LENGTH_LONG).show()
            }
        }

    }

    private fun stopPlayer() {
        if(player != null){
            player?.stop()
            player = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        stopPlayer()

        if(exerciseDao != null){
            exerciseDao = null
        }
    }
}