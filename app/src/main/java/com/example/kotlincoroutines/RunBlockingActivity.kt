package com.example.kotlincoroutines

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.kotlincoroutines.databinding.ActivityRunBlockingBinding
import kotlinx.coroutines.*
import kotlin.random.Random

class RunBlockingActivity : AppCompatActivity() {

    lateinit var button: Button
    //lateinit var text: TextView

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val runBlockingViewModel = ViewModelProvider(this).get(RunBlockingViewModel::class.java)
        val binding = ActivityRunBlockingBinding.inflate(layoutInflater)
        binding.viewModel = runBlockingViewModel
        binding.lifecycleOwner = this

        button = binding.button
       // text = binding.text



        button.setOnClickListener {
            runBlockingViewModel.updateCounter()
            runBlockingViewModel.main()
            //text.text = (count++).toString()

        }

        setContentView(binding.root)
    }


    companion object {
        private const val TAG = "RunBlockingActivity"
    }

}