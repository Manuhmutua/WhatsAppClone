package turi.practice.whatsappclone.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import turi.practice.whatsappclone.R

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }
    fun onApply(v: View){

    }
    fun onDelete    (v: View){

    }
    companion object {
        fun newIntent(context: Context) = Intent(context, ProfileActivity::class.java)
    }

}
