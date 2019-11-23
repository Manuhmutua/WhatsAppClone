package turi.practice.whatsappclone.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_status_list.*

import turi.practice.whatsappclone.R
import turi.practice.whatsappclone.activities.StatusActivity
import turi.practice.whatsappclone.adapters.StatusListAdapter
import turi.practice.whatsappclone.listeners.StatusItemClickListener
import turi.practice.whatsappclone.util.DATA_USERS
import turi.practice.whatsappclone.util.DATA_USER_CHATS
import turi.practice.whatsappclone.util.StatusListElement
import turi.practice.whatsappclone.util.User

class StatusListFragment : Fragment(), StatusItemClickListener {

    private val firebaseDB = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private var statusListAdapter = StatusListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_status_list, container, false)
    }

    override fun onItemClicked(statusElement: StatusListElement) {
        startActivity(StatusActivity.newIntent(context, statusElement))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statusListAdapter.setOnItemClickedListener(this)
        statusListRV.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context)
            adapter = statusListAdapter
            addItemDecoration(DividerItemDecoration(this@StatusListFragment.context, DividerItemDecoration.VERTICAL))
        }
    }

    fun onVisible(){
        statusListAdapter.onRefresh()
        refreshList()
    }

    fun refreshList(){
        firebaseDB.collection(DATA_USERS)
            .document(userId!!)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.contains(DATA_USER_CHATS)){
                    val partners = documentSnapshot[DATA_USER_CHATS]
                    for(partner in (partners as HashMap<String, String>).keys){
                        firebaseDB.collection(DATA_USERS)
                            .document(partner)
                            .get()
                            .addOnSuccessListener { documentSnapshot ->
                                val partner = documentSnapshot.toObject(User::class.java)
                                if (partner != null){
                                    if (!partner.status.isNullOrEmpty() || !partner.statusUrl.isNullOrEmpty()){
                                        val newElement = StatusListElement(partner.name, partner.imageUrl, partner.status, partner.statusUrl, partner.statusTime)
                                        statusListAdapter.addElement(newElement)
                                    }
                                }

                            }
                    }
                }
            }
    }
}
