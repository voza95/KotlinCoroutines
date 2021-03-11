package com.example.kotlincoroutines

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    lateinit var btnStartActivity:Button
    lateinit var button:Button
    lateinit var tvDummy:TextView

    val JOB_TIMEOUT = 1900L

    private suspend fun fakeApiRequest(){
        withContext(IO){
            val job = withTimeoutOrNull(JOB_TIMEOUT) {
                val result1 = getResult1FromApi()
                Log.d(TAG, "fakeApiRequest: $result1")
                withContext(Main){
                    tvDummy.text = tvDummy.text.toString() + result1 +"\n"
                }

                val result2 = getResult2FromApi()
                Log.d(TAG, "fakeApiRequest: $result2")
                withContext(Main){
                    tvDummy.text = tvDummy.text.toString() + result2 +"\n"
                }
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStartActivity = findViewById(R.id.btnStartActivity)
        button = findViewById(R.id.button)
        tvDummy = findViewById(R.id.tvDummy)

        button.setOnClickListener {
            CoroutineScope(IO).launch {
                //setResult()
                fakeApiRequest()
            }
        }

        btnStartActivity.setOnClickListener {
            lifecycleScope.launch {
                while (true){
                    delay(1000L)
                    Log.d(TAG, "onCreate: Still Running....")
                }
            }
            GlobalScope.launch {
                delay(5000L)
                Intent(this@MainActivity, SecondActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }
        Log.e("MyCall","Main program starts: ${Thread.currentThread().name}")

        /*GlobalScope.launch(Dispatchers.IO) {
            val  networkCallAns1 = doNetWorkCall1()

            Log.d(TAG, "onCreate: $networkCallAns1")
            withContext(Dispatchers.Main){
                val tvDummyTxt = findViewById<TextView>(R.id.tvDummy)
                tvDummyTxt.text = networkCallAns1
            }

        }*/

      /*  val job = GlobalScope.launch(Dispatchers.Default) {
            *//*repeat(5){
                Log.d(TAG, "onCreate: Coroutine is still working...")
                delay(1000L)
            }*//*
            Log.d(TAG, "onCreate: Starting long running calculation...")
            withTimeout(3000L){
                for (i in 30..40){
                    if (isActive)
                        Log.d(TAG, "onCreate: Result for  i = $i: ${fib(i)}")
                }
            }
            Log.d(TAG, "onCreate: Ending Long running calculation...")
        }*/
        /*runBlocking {
            //This will block out thread un-till our task is finished
            //job.join()
            delay(2000L)
            job.cancel()
            Log.d(TAG, "onCreate: Canceled Job")
        }*/

        /*GlobalScope.launch(Dispatchers.IO) {
            val time = measureTimeMillis {
//                val doNetWorkCall1 = doNetWorkCall1()
//                val doNetWorkCall2 = doNetWorkCall2()
//                Log.d(TAG, "onCreate: Answer1 is  $doNetWorkCall1")
//                Log.d(TAG, "onCreate: Answer2 is  $doNetWorkCall2")
                var answer1: String? = null
                var answer2: String? = null
                val job1 = launch { answer1 = doNetWorkCall1() }
                val job2 = launch { answer2 = doNetWorkCall2() }
                job1.join()
                job2.join()
                Log.d(TAG, "onCreate: Answer1 is  $answer1")
                Log.d(TAG, "onCreate: Answer2 is  $answer2")
            }
            Log.d(TAG, "onCreate: $time")
        }*/
        /*GlobalScope.launch(Dispatchers.IO) {
            val time = measureTimeMillis {
                val answer1 = async { doNetWorkCall1() }
                val answer2 = async { doNetWorkCall2() }
                Log.d(TAG, "onCreate: Answer1 is  ${answer1.await()}")
                Log.d(TAG, "onCreate: Answer2 is  ${answer2.await()}")
            }
            Log.d(TAG, "onCreate: $time")
        }*/

        /*runBlocking {
            delay(100L)
        }*/

        Log.e("MyCall","Main program end: ${Thread.currentThread().name}")
    }

    private suspend fun setResult(){
        val result1 = getResult1FromApi()
        Log.d(TAG, "onCreate: $result1")
        withContext(Main){
            tvDummy.text = tvDummy.text.toString() + result1 +"\n"
        }
        val result2 = getResult2FromApi()
        Log.d(TAG, "onCreate: $result2")
        withContext(Main){
            tvDummy.text =  tvDummy.text.toString() + result2 +"\n"
        }
    }

    private suspend fun getResult1FromApi(): String{
        logThread("getResult1FromApi")
        delay(1000)
        return "Result #1"
    }

    private suspend fun getResult2FromApi(): String{
        logThread("getResult2FromApi")
        delay(1000)
        return "Result #2"
    }

    private fun logThread(methodName: String){
        Log.d(TAG, "logThread: $methodName : ${Thread.currentThread().name}")
    }

    suspend fun doNetWorkCall1(): String{
        delay(3000L)
        return "This is the answer 1."
    }

    suspend fun doNetWorkCall2(): String{
        delay(3000L)
        return "This is the answer 2."
    }


    companion object {
        private const val TAG = "MainActivity"
    }

    fun fib(n: Int): Long{
        return if(n == 0) 0
        else if(n == 1) 1
        else fib(n-1) + fib(n-2)
    }
}