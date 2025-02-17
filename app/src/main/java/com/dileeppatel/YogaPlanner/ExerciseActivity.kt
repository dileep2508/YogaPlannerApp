package com.dileeppatel.YogaPlanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import com.dileeppatel.YogaPlanner.databinding.ActivityExerciseBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivityExerciseBinding? = null
    private var restTimer: CountDownTimer? = null
    private var restProgress = 0

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null

    private var rvExerciseStatusAdapter: ExerciseIndexAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        exerciseList = Constants.defaultExerciseList()

        tts = TextToSpeech(this, this)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarExercise?.setNavigationOnClickListener {view->
            onBackPressed()
        }
        binding?.ibPrev?.setOnClickListener {
            setExercise(currentExercisePosition+1-1)
        }
        binding?.ibNext?.setOnClickListener{
            setExercise(currentExercisePosition+1+1)
        }

        setupExerciseStatusAdapter()
        setupRestView()


    }

    private fun setupExerciseStatusAdapter() {
        rvExerciseStatusAdapter = exerciseList?.let { ExerciseIndexAdapter(
            it,
            {exerciseNumber ->
                setExercise(exerciseNumber)
            }
        ) }
        binding?.rvExerciseIndex?.adapter = rvExerciseStatusAdapter
    }

    private fun setupRestView(){
        currentExercisePosition++

        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.flProgressBarGR?.visibility = View.VISIBLE
        binding?.tvNextExercise?.visibility = View.VISIBLE
        binding?.tvNextExerciseName?.visibility = View.VISIBLE


        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.ivExercise?.visibility = View.INVISIBLE
        binding?.flProgressBarEx?.visibility = View.INVISIBLE

        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
        binding?.tvTitle?.text = "GET READY FOR"
        binding?.tvNextExerciseName?.text = exerciseList!![currentExercisePosition].getName()

        exerciseList!![currentExercisePosition].setIsSelected(true)
        rvExerciseStatusAdapter?.notifyDataSetChanged()
        setRestTimer()
    }

    private fun setRestTimer(){

        binding?.progressBarGR?.progress = restProgress

        restTimer = object : CountDownTimer(10000, 100){
            override fun onTick(p0: Long) {
                restProgress++
                binding?.progressBarGR?.progress = 100 - restProgress
                binding?.tvTimerGR?.text = (10-(restProgress/10)).toString()
            }
            override fun onFinish() {
                restProgress++
                binding?.progressBarGR?.progress = 0
                binding?.tvTimerGR?.text = (0).toString()

                setupExerciseTimerView()
            }
        }.start()
    }

    private fun setupExerciseTimerView(){
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.flProgressBarGR?.visibility = View.INVISIBLE
        binding?.tvNextExercise?.visibility = View.INVISIBLE
        binding?.tvNextExerciseName?.visibility = View.INVISIBLE

        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.ivExercise?.visibility = View.VISIBLE
        binding?.flProgressBarEx?.visibility = View.VISIBLE

        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()
        binding?.ivExercise?.setImageResource(exerciseList!![currentExercisePosition].getImage())

        setupExerciseTimer()
    }

    private fun setupExerciseTimer(){

        binding?.progressBarEx?.progress = restProgress
        restTimer = object : CountDownTimer(30000, 100){
            override fun onTick(p0: Long) {
                restProgress++
                binding?.progressBarEx?.progress = 300 - restProgress
                binding?.tvTimerEx?.text = (30-(restProgress/10)).toString()
            }
            override fun onFinish() {
                restProgress++
                binding?.progressBarEx?.progress = 0
                binding?.tvTimerEx?.text = (0).toString()
//                player?.stop()
                exerciseList!![currentExercisePosition].setIsSelected(false)
                exerciseList!![currentExercisePosition].setIsCompleted(true)
                rvExerciseStatusAdapter?.notifyDataSetChanged()
                if(currentExercisePosition == (exerciseList?.size!! - 1)){
                    val finishIntent = Intent(this@ExerciseActivity,FinishActivity::class.java)
                    startActivity(finishIntent)
                    finish()
                }else{
                    setupRestView()
                }
            }
        }.start()
    }

    private fun setExercise(exerciseNumber: Int){

        if(exerciseNumber >=1 && exerciseNumber <= 12){
            currentExercisePosition = exerciseNumber - 2
            restTimer?.cancel()
            setupRestView()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
        binding = null

        if(tts != null){
            tts?.stop()
            tts?.shutdown()
        }

//        if(player != null){
//            player?.stop()
//        }
        if(rvExerciseStatusAdapter != null){
            rvExerciseStatusAdapter = null
        }
    }

    private fun speakOut(text:String){
        tts?.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts?.setLanguage(Locale.ENGLISH)

            if(result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS Error","Language not suppored.")
            }
            if(result == TextToSpeech.LANG_MISSING_DATA){
                Log.e("TTS Error","Language missing data.")
            }
        }
    }
}