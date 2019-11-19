package turi.practice.whatsappclone

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainActivity : AppCompatActivity() {

    private var mSectionPagerAdapter: PlaceholderFragment.SectionPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        mSectionPagerAdapter = PlaceholderFragment.SectionPagerAdapter(supportFragmentManager)
        container.adapter = mSectionPagerAdapter
        fab.setOnClickListener{ view ->
            Snackbar.make(view, "Replace with action", Snackbar.LENGTH_SHORT).setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item?.itemId
        if(id == R.id.action_settings){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    class PlaceholderFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.fragment_main, container, false)
            rootView.section_label.text =
                "Hello World from section${arguments?.getInt(ARG_SECTION_NUMBER)}"
            return rootView
        }

        inner class SectionPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
            override fun getItem(position: Int): Fragment {
                return PlaceholderFragment.newIntent(position + 1)
            }

            override fun getCount(): Int {
                return 3
            }
        }

        companion object {
            private val ARG_SECTION_NUMBER = "Section number"
            fun newIntent(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }

    companion object{
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

}
