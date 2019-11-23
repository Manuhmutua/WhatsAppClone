package turi.practice.whatsappclone.util

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import turi.practice.whatsappclone.R
import java.text.DateFormat
import java.util.*

fun populateImage(context: Context?, uri: String?, imageView: ImageView, errorDrawable: Int = R.drawable.empty){
    if (context != null) {
        val options = RequestOptions()
            .placeholder(progresDrawable(context))
            .error(errorDrawable)
        Glide.with(context)
            .load(uri)
            .apply(options)
            .into(imageView)
    }
}

fun progresDrawable(context: Context): CircularProgressDrawable{
    return CircularProgressDrawable(context).apply {
        strokeWidth = 5f
        centerRadius = 30f
        start()
    }
}

fun getTime(): String{
    val df = DateFormat.getDateInstance()
    return df.format(Date())
}