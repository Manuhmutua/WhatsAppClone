package turi.practice.whatsappclone.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import turi.practice.whatsappclone.R
import turi.practice.whatsappclone.listeners.StatusItemClickListener
import turi.practice.whatsappclone.util.StatusListElement
import turi.practice.whatsappclone.util.populateImage

class StatusListAdapter(val statusList: ArrayList<StatusListElement>): RecyclerView.Adapter<StatusListAdapter.StatusListViewHolder>() {
    private var clickListener: StatusItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StatusListViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_status_list, parent, false)
    )
    fun onRefresh(){
        statusList.clear()
        notifyDataSetChanged()
    }
    fun addElement(element: StatusListElement){
        statusList.add(element)
        notifyDataSetChanged()
    }
    override fun getItemCount() = statusList.size

    override fun onBindViewHolder(holder: StatusListViewHolder, position: Int){
        holder.bind(statusList[position], clickListener)
    }

    fun setOnItemClickedListener(listener: StatusItemClickListener){
        clickListener = listener
        notifyDataSetChanged()
    }
    class StatusListViewHolder(view: View): RecyclerView.ViewHolder(view){
        private var layout = view.findViewById<RelativeLayout>(R.id.itemLayout)
        private var elementIV= view.findViewById<ImageView>(R.id.itemIV)
        private var elementNameTV= view.findViewById<TextView>(R.id.itemNameTV)
        private var elementTimeTV= view.findViewById<TextView>(R.id.itemTimeTV)
        fun bind(element: StatusListElement, listener: StatusItemClickListener?){
            populateImage(elementIV.context, element.userUrl, elementIV, R.drawable.default_user)
            elementNameTV.text = element.userName
            elementTimeTV.text = element.statusTime
            layout?.setOnClickListener { listener?.onItemClicked(element) }
        }
    }
}