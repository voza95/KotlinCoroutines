package com.example.kotlincoroutines

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class StarterActivity : AppCompatActivity() {

    private val PROGRESS_MAX = 100
    private val PROGRESS_START = 0
    private val JOB_TIME = 4000

    private lateinit var job: CompletableJob

    private lateinit var jobProgressBar: ProgressBar
    private lateinit var jobButton: Button
    private lateinit var jobCompleteText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starter)
        jobProgressBar = findViewById(R.id.job_progress_bar)
        jobButton = findViewById(R.id.job_button)
        jobCompleteText = findViewById(R.id.job_complete_text)

        jobButton.setOnClickListener {
            if (!::job.isInitialized) {
                initJob()
            }
            jobProgressBar.startJobOrCancel(job)
        }
    }

    fun ProgressBar.startJobOrCancel(job: Job) {
        if (this.progress > 0) {
            Log.d(TAG, "startJobOrCancel: cancelled")
            resetJob()
        } else {
            jobButton.text = "Cancel job #1"
            //Add the job to coroutine scope
            CoroutineScope(Dispatchers.IO + job).launch {
                Log.d(TAG, "startJobOrCancel: $this is activited with job $job")
                for (i in PROGRESS_START..PROGRESS_MAX) {
                    delay((JOB_TIME / PROGRESS_MAX).toLong())
                    this@startJobOrCancel.progress = i
                }
                updateJobCompleteTextView("Job is complete")
            }
            Log.d(TAG, "startJobOrCancel: ")
        }
    }

    fun updateJobCompleteTextView(text: String) {
        GlobalScope.launch(Dispatchers.Main) {
            jobCompleteText.text = text
        }
    }

    private fun resetJob() {
        if (job.isActive || job.isCompleted){
            job.cancel(CancellationException("Resetting job"))
        }
        initJob()
    }

    fun initJob() {
        jobButton.text = "Start Job #1"
        jobCompleteText.text = ""
        updateJobCompleteTextView("")
        jobCompleteText.text = ""
        job = Job()
        job.invokeOnCompletion { it ->
            it?.message.let {
                var msg = it
                if (msg.isNullOrBlank()) {
                    msg = "Unknown cancellation error."
                }
                Log.d(TAG, "$job initJob: $msg")
                showToast(msg)
            }
        }
        jobProgressBar.max = PROGRESS_MAX
        jobProgressBar.progress = PROGRESS_START
    }

    fun showToast(text: String) {
        GlobalScope.launch(Dispatchers.Main) {
            Toast.makeText(this@StarterActivity, text, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "StarterActivity"
    }
}