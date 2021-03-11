package com.example.kotlincoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class SequentalTaskActivity : AppCompatActivity() {

    lateinit var button: Button
    lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sequental_task)

        button = findViewById(R.id.button)
        text = findViewById(R.id.text)

        button.setOnClickListener {
            setNewText("Clicked!")
            fakeApiRequest()
        }
    }

    private fun fakeApiRequest(){
        CoroutineScope(Dispatchers.IO).launch {
            val executionTime = measureTimeMillis {
                val result1 = async {
                    Log.d(TAG, "fakeApiRequest: job1 ${Thread.currentThread().name}")
                    getResult1FomApi()
                }.await()

                val result2 = async {
                    Log.d(TAG, "fakeApiRequest: job1 ${Thread.currentThread().name}")
                    try {
                        getResult2FomApi(result1 = result1)
//                        getResult2FomApi(result1 = "result1")
                    }catch (e:Exception){
                        Log.d(TAG, "fakeApiRequest: ${e.message}")
                    }
                }.await()
                Log.d(TAG, "fakeApiRequest: $result2")
            }
            Log.d(TAG, "fakeApiRequest: total time elapsed $executionTime ms.")
        }
    }

    private fun setNewText(input: String){
        val newText = text.text.toString() + "\n$input"
        text.text = newText
    }

    private suspend fun setTextOnMainThread(input: String){
        withContext(Dispatchers.Main) {
            setNewText(input)
        }
    }

    private suspend fun getResult1FomApi(): String {
        delay(1000)
        return "Result #1"
    }

    private suspend fun getResult2FomApi(result1: String): String {
        delay(1700)
        if (result1 == "Result #1"){
            return "Result #2"
        }
        throw CancellationException("Result not found...")
    }

    companion object {
        private const val TAG = "SequentalTaskActivity"
    }
}