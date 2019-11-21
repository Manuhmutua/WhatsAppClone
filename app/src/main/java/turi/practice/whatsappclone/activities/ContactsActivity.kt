package turi.practice.whatsappclone.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import turi.practice.whatsappclone.R
import turi.practice.whatsappclone.util.Contact

class ContactsActivity : AppCompatActivity() {
    private val contactsList = ArrayList<Contact>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        getContacts()
    }

    private fun getContacts(){
        contactsList.clear()
        val newList = ArrayList<Contact>()
        val phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null)
            while (phones!!.moveToNext()){
                val name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                newList.add(Contact(name, phoneNumber))
            }
        contactsList.addAll(newList)
        phones.close()
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, ContactsActivity::class.java)
    }

}
