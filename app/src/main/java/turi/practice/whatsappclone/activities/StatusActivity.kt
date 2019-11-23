package turi.practice.whatsappclone.activities

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_status.*
import turi.practice.whatsappclone.R
import turi.practice.whatsappclone.listeners.ProgressListener
import turi.practice.whatsappclone.util.StatusListElement
import turi.practice.whatsappclone.util.populateImage

class StatusActivity : AppCompatActivity(), ProgressListener {

    private lateinit var statusElement: StatusListElement

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)
        if(intent.hasExtra(PARAM_STATUS_ELEMENT)){
            statusElement =intent.getParcelableExtra(PARAM_STATUS_ELEMENT)
        } else {
            Toast.makeText(this,"Unable to fetch status", Toast.LENGTH_LONG).show()
            finish()
        }
        statusTV.text = statusElement.status
        populateImage(this, statusElement.statusUrl, statusIV)
        progressBar.max = 100
        TimerTask(this).execute("")
    }

    override fun onProgressUpdate(progress: Int) {
        progressBar.progress = progress
        if(progress == 100){
            finish()
        }
    }

    private class  TimerTask(val listener: ProgressListener): AsyncTask<String, Int, Any>(){
        override fun doInBackground(vararg params: String?){
            var i = 0
            val sleep = 10L
            while (i < 100){
                i++
                publishProgress(i)
                Thread.sleep(sleep)
            }
        }

        override fun onProgressUpdate(vararg values: Int?) {
            if(values[0] != null){
                listener.onProgressUpdate(values[0]!!)
            }
        }
    }

    companion object{
        val PARAM_STATUS_ELEMENT = "element"
        fun newIntent(context: Context?, statusElement: StatusListElement): Intent {
            val intent = Intent(context, StatusActivity::class.java)
            intent.putExtra(PARAM_STATUS_ELEMENT, statusElement)
            return intent
        }
    }
}
