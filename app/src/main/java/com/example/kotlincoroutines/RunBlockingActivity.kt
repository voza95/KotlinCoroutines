package com.example.kotlincoroutines

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlin.random.Random

class RunBlockingActivity : AppCompatActivity() {

    lateinit var button: Button
    lateinit var text: TextView

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_run_blocking)

        button = findViewById(R.id.button)
        text = findViewById(R.id.text)

        main()

        button.setOnClickListener {
            text.text = (count++).toString()

        }

    }
    /**runBlocing is not like suspend function.
    A suspend function is function that who's job is to do something and run inside a coroutine
    runBlocking is more similar to  coroutine scope(Runs a new coroutine and **blocks** the current thread _interruptibly_ until its completion.)
    runBlocking is not very useful.
    **/
    fun main(){
        CoroutineScope(Dispatchers.Main).launch {
            Log.d(TAG, "main: ${Thread.currentThread().name}")
            val result1 = getResult()
            Log.d(TAG, "main: $result1")
            val result2 = getResult()
            Log.d(TAG, "main: $result2")
            val result3 = getResult()
            Log.d(TAG, "main: $result3")
            val result4 = getResult()
            Log.d(TAG, "main: $result4")
            val result5 = getResult()
            Log.d(TAG, "main: $result5")
        }

        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            runBlocking {
                Log.d(TAG, "main: Blocking thread ${Thread.currentThread().name}")
                delay(4000)
                Log.d(TAG, "main:Done blocking thread ${Thread.currentThread().name}")
            }
        }

    }

    companion object {
        private const val TAG = "RunBlockingActivity"
    }

    private suspend fun getResult(): Int{
        delay(1000)
        return Random.nextInt(0,100)
    }
}