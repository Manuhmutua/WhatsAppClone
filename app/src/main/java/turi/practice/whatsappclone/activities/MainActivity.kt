package turi.practice.whatsappclone.activities

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import turi.practice.whatsappclone.R
import turi.practice.whatsappclone.fragments.ChatsFragment
import turi.practice.whatsappclone.fragments.StatusFragment
import turi.practice.whatsappclone.fragments.StatusUpdateFragment
import turi.practice.whatsappclone.util.PERMISSION_REQUEST_READ_CONTACTS

class MainActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private val chatsFragment = ChatsFragment()
    private val statusUpdateFragment = StatusUpdateFragment()
    private val statusFragment = StatusFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
            } else {
                startNewActivity()
            }
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
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

}
