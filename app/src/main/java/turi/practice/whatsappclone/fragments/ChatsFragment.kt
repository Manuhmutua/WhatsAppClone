package turi.practice.whatsappclone.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_chats.*

import turi.practice.whatsappclone.R
import turi.practice.whatsappclone.adapters.ChatsAdapter
import turi.practice.whatsappclone.listeners.ChatClickListener

class ChatsFragment : Fragment(), ChatClickListener {
    private var chatsAdapter = ChatsAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatsAdapter.setOnItemClickListerner(this)
        chatsRV.apply{
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context)
            adapter = chatsAdapter
            addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        }
        var chatList = arrayListOf<String>("Chat 1 ", "Chat 2", "Chat 3","Chat 4 ", "Chat 5", "Chat 6","Chat 7 ", "Chat 8", "Chat 9")
        chatsAdapter.updateChats(chatList)
    }

    override fun onChatClicked(
        name: String?,
        otherUserId: String?,
        chatImageUrl: String?,
        chatName: String?
    ) {
        Toast.makeText(context,"$name clicked", Toast.LENGTH_LONG).show()
    }


}
