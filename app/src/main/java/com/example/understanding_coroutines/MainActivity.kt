package com.example.understanding_coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MainActivity : AppCompatActivity() {

    private val RESULT_1 = "Result #1"
    private val RESULT_2 = "Result #2"
    val JOB_TIMEOUT = 1900L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            /*IO -> Network and Database Interactions,
             Main -> In main Threads and interacting with the UI,
             Default -> heavy computational work
             */
            setNewText("Click!")
            CoroutineScope(Main).launch {
                    fakeApiRequest()
            }

        }
    }

    //For Network Time-outs
    private suspend fun fakeApiRequest(){
        withContext(IO) {
            val job = withTimeoutOrNull(JOB_TIMEOUT) {

                //timeout bcz we set limit for 1900 and we are
                // requesting two req for 1000s each.

                val result1 = getResult1FromApi()
                setTextOnMainThread("Got $result1")

                val result2 = getResult2FromApi("result2")
                setTextOnMainThread("Got $result2")

                println("result #1: ${result1}")
            }

            //checking about job status
            if(job == null){
                val cancelMessage = "Cancelling job... Job took longer than $JOB_TIMEOUT ms"
                println("debug: $cancelMessage")
                setTextOnMainThread(cancelMessage)
            }
        }
    }


    private fun setNewText(input: String){
        val newText = text.text.toString() + "\n$input"
        text.text = newText
    }

    private suspend fun setTextOnMainThread(input: String){
        withContext(Main){
            setNewText(input)
        }
    }
//    private suspend fun fakeApiRequest(){
//        val result1 = getResult1FromApi()
//        print("debug: $result1")
//        setTextOnMainThread(result1)
//
//        val result2 = getResult2FromApi(result1)
//        setTextOnMainThread(result2)
//    }
    private suspend fun getResult1FromApi(): String{
        logThread("getResultFromApi")
        delay(1000) //delay current co-routine
        return RESULT_1
    }

    private suspend fun getResult2FromApi(result1 : String): String{
        logThread("getResult2FromApi")
        delay(1000)
        return result1
    }
    private fun logThread(methodName: String){
        println("debug: ${methodName}: ${Thread.currentThread().name}")
    }
}
