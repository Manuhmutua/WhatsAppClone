package turi.practice.whatsappclone.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import turi.practice.whatsappclone.R
import turi.practice.whatsappclone.fragments.ChatsFragment
import turi.practice.whatsappclone.fragments.StatusFragment
import turi.practice.whatsappclone.fragments.StatusUpdateFragment
import turi.practice.whatsappclone.listeners.FailureCallback
import turi.practice.whatsappclone.util.DATA_USERS
import turi.practice.whatsappclone.util.DATA_USER_PHONE
import turi.practice.whatsappclone.util.PERMISSION_REQUEST_READ_CONTACTS
import turi.practice.whatsappclone.util.REQUEST_NEW_CHAT

class MainActivity : AppCompatActivity(), FailureCallback {

    private val firebaseDB = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private val chatsFragment = ChatsFragment()
    private val statusUpdateFragment = StatusUpdateFragment()
    private val statusFragment = StatusFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chatsFragment.setFailureCallbackListener(this)
        setSupportActionBar(toolbar)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        container.adapter = mSectionsPagerAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
        resizeTabs()
        tabs.getTabAt(1)?.select()
        tabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position) {
                    0 -> {fab.hide()}
                    1 -> {fab.show()}
                    2 -> {fab.hide()}
                }
            }
        })
    }

    override fun onUserError() {
        Toast.makeText(this,"User not found", Toast.LENGTH_LONG).show()
        startActivity(LoginActivity.newIntent(this))
        finish()
    }

    fun resizeTabs(){
        val layout = (tabs.getChildAt(0)as LinearLayout).getChildAt(0)as LinearLayout
        val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 0.4f
        layout.layoutParams = layoutParams
    }

    fun onNewChat(v: View){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)){
                AlertDialog.Builder(this)
                    .setTitle("Contacts Permission")
                    .setMessage("This app requires access to contacts to initiate conversations")
                    .setPositiveButton("Ask me"){ dialog, which -> requestContactsPermision() }
                    .setNegativeButton("Cancel"){dialog, which ->  }
                    .show()
            } else {
                requestContactsPermision()
            }
        } else {
            startNewActivity()
        }
    }

    fun requestContactsPermision(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), PERMISSION_REQUEST_READ_CONTACTS)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_REQUEST_READ_CONTACTS -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startNewActivity()
                }
            }
        }
    }
    fun startNewActivity(){
        startActivityForResult(ContactsActivity.newIntent(this), REQUEST_NEW_CHAT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_NEW_CHAT -> {
                    val name = data?.getStringExtra(PARAM_NAME) ?: ""
                    val phone = data?.getStringExtra(PARAM_PHONE) ?: ""
                    checkNewChatUser(name, phone)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun checkNewChatUser(name: String, phone: String){
        if(!name.isNullOrEmpty() && !phone.isNullOrEmpty())
            firebaseDB.collection(DATA_USERS).whereEqualTo(DATA_USER_PHONE, phone)
                .get()
                .addOnSuccessListener { result ->
                    if (result.documents.size > 0 ){
                        chatsFragment.newChat(result.documents[0].id)
                    } else {
                        AlertDialog.Builder(this)
                            .setTitle("User not found")
                            .setMessage("$name does not have an account. Would you like to send an SMS invite")
                            .setPositiveButton("Confirm"){ dialog, which ->
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data = Uri.parse("sms:$phone")
                                intent.putExtra("sms_body","Hi, I'm using this new cool WhatsAppClone App. You should Install it too so we can chat there")
                                startActivity(intent)
                            }
                            .setNegativeButton("Cancel", null)
                            .show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this,"An error occurred. Please try again later", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
    }

    override fun onResume() {
        super.onResume()
        if(firebaseAuth.currentUser == null ){
            startActivity(LoginActivity.newIntent(this))
            finish()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       when(item.itemId){
           R.id.action_profile -> onProfile()
           R.id.action_logout -> onLogout()
       }

        return super.onOptionsItemSelected(item)
    }

    private fun onProfile(){
        startActivity(ProfileActivity.newIntent(this))
    }

    private fun onLogout(){
        firebaseAuth.signOut()
        startActivity(LoginActivity.newIntent(this))
        finish()
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return when(position){
                0 -> statusUpdateFragment
                1 -> chatsFragment
                else -> statusFragment
            }
        }

        override fun getCount(): Int {
            return 3
        }
    }



    companion object {
        val PARAM_NAME = "Param name"
        val PARAM_PHONE = "Param phone"
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

}
