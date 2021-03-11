package com.example.kotlincoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.withCreated
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class TaskMainActivity : AppCompatActivity() {

    lateinit var button:Button
    lateinit var text:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_main)
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
                val result1: Deferred<String> = async {
                    Log.d(TAG, "fakeApiRequest: job1 ${Thread.currentThread().name}")
                    getResult1FomApi()
                }


                val result2: Deferred<String> = async {
                    Log.d(TAG, "fakeApiRequest: job2 ${Thread.currentThread().name}")
                    getResult2FomApi()
                }
                setTextOnMainThread("Got ${result1.await()}")
                setTextOnMainThread("Got ${result2.await()}")
            }
            Log.d(TAG, "fakeApiRequest: total time elapsed $executionTime")
        }
    }

    /*private fun fakeApiRequest() {
        val startTime = System.currentTimeMillis()

        val parentJob = CoroutineScope(Dispatchers.IO).launch {
            val job1 = launch {
                val time1 = measureTimeMillis {
                    Log.d(TAG, "fakeApiRequest: Launching job1 in thread: ${Thread.currentThread().name}")
                    val result1 = getResult1FomApi()
                    setTextOnMainThread(result1)
                }
                Log.d(TAG, "fakeApiRequest: completed job1 in $time1")
            }

            //This will make job calling sequential
            //job1.join()

            val job2 = launch {
                val time2 = measureTimeMillis {
                    Log.d(TAG, "fakeApiRequest: Launching job2 in thread: ${Thread.currentThread().name}")
                    val result2 = getResult2FomApi()
                    setTextOnMainThread(result2)
                }
                Log.d(TAG, "fakeApiRequest: completed job2 in $time2")
            }
        }
        parentJob.invokeOnCompletion {
            Log.d(
                TAG,
                "fakeApiRequest: Total elapsed time: ${System.currentTimeMillis() - startTime}"
            )
        }
    }*/

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

    private suspend fun getResult2FomApi(): String {
        delay(1700)
        return "Result #2"
    }

    companion object {
        private const val TAG = "TaskMainActivity"
    }

}