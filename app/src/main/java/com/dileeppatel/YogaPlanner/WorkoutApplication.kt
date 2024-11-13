package com.dileeppatel.YogaPlanner

import android.app.Application

class WorkoutApplication: Application() {
    val db by lazy{
        ExerciseDatabase.getInstance(applicationContext)
    }
}