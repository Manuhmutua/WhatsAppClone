package turi.practice.whatsappclone.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_conversation.*
import turi.practice.whatsappclone.R
import turi.practice.whatsappclone.adapters.ConversationAdapter
import turi.practice.whatsappclone.util.Message

class ConversationActivity : AppCompatActivity() {

    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val conversationAdapter = ConversationAdapter(arrayListOf(), userId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)
        messagesRV.apply{
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context)
            adapter = conversationAdapter
        }
        conversationAdapter.addMessage(Message(userId, "Hello", 2))
        conversationAdapter.addMessage(Message("Turi", "Heyy", 3))
        conversationAdapter.addMessage(Message(userId, "How are you", 4))
        conversationAdapter.addMessage(Message("Turi", "I'm Great!", 5))
        conversationAdapter.addMessage(Message(userId, "Me too", 6))
    }

    fun onSend(v: View){

    }


    companion object{
        fun newIntent(context: Context?): Intent {
            val intent = Intent(context, ConversationActivity::class.java)
            return intent
        }
    }
}
