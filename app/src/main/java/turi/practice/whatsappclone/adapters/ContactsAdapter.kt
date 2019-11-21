package turi.practice.whatsappclone.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import turi.practice.whatsappclone.R
import turi.practice.whatsappclone.listeners.ContactsClickListener
import turi.practice.whatsappclone.util.Contact

class ContactsAdapter(val contacts: ArrayList<Contact>) :
    RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {
    private var clickListerner: ContactsClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ContactsViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
    )

    override fun getItemCount() = contacts.size

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bind(contacts[position], clickListerner)
    }

    fun setOnItemClickedListener(listener: ContactsClickListener){
        clickListerner = listener
        notifyDataSetChanged()
    }
    class ContactsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var layout = view.findViewById<LinearLayout>(R.id.contactLayout)
        private var nameTV = view.findViewById<TextView>(R.id.contactNameTV)
        private var phoneTV = view.findViewById<TextView>(R.id.contactNumberTV)
        fun bind(contact: Contact, listener: ContactsClickListener?) {
            nameTV.text = contact.name
            phoneTV.text = contact.phone
            layout.setOnClickListener{ listener?.onContactClicked(contact.name, contact.phone)}
        }
    }
}