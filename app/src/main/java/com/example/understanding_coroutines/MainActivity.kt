package com.example.understanding_coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val RESULT_1 = "Result #1"
    private val RESULT_2 = "Result #2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            /*IO -> Network and Database Interactions,
             Main -> In main Threads and interacting with the UI,
             Default -> heavy computational work
             */
            CoroutineScope(IO).launch {
                fakeApiRequest()
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
    private suspend fun fakeApiRequest(){
        val result1 = getResult1FromApi()
        print("debug: $result1")
        setTextOnMainThread(result1)

        val result2 = getResult2FromApi()
        setTextOnMainThread(result2)
    }
    private suspend fun getResult1FromApi(): String{
        logThread("getResultFromApi")
        delay(1000) //delay current co-routine
//        Thread.sleep(1000) //delay whole thread
        return RESULT_1
    }

    private suspend fun getResult2FromApi(): String{
        logThread("getResult2FromApi")
        delay(1000)
        return RESULT_2
    }
    private fun logThread(methodName: String){
        println("debug: ${methodName}: ${Thread.currentThread().name}")
    }
}
