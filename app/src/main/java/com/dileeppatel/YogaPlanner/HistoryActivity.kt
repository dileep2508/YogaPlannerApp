package com.dileeppatel.YogaPlanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.dileeppatel.YogaPlanner.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private var binding: ActivityHistoryBinding? = null
    private var exerciseAdapter: ExerciseHistoryAdapter? = null
    private var exerciseDao: ExerciseDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.tbHistory)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            binding?.tbHistory?.setNavigationOnClickListener {
                finish()
                onBackPressed()
            }

            exerciseDao = (application as WorkoutApplication).db.exerciseDao()

            lifecycleScope.launch{
                exerciseDao!!.fetchAllExercises().collect{
                    val list= ArrayList(it)
                    setupHistoryExerciseRV(list, exerciseDao!!)
                }
            }
        }
    }

    private fun setupHistoryExerciseRV(list: ArrayList<ExerciseEntity>, exerciseDao:ExerciseDao) {

        exerciseAdapter = ExerciseHistoryAdapter(
            list
        )

        { itemId ->
            deleteExercise(itemId)
        }
        binding?.rvHistory?.adapter = exerciseAdapter
    }

    private fun deleteExercise(id: Int){
        lifecycleScope.launch{
            exerciseDao?.fetchExerciseById(id)?.collect{
                exerciseDao!!.delete(it)
            }
        }
    }

}