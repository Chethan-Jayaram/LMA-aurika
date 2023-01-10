package com.example.lastmailattendent.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.lastmailattendent.R
import com.google.android.material.snackbar.Snackbar
import de.hdodenhof.circleimageview.CircleImageView


fun Context.toast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG ).show()
}

fun ProgressBar.show(){
    visibility = View.VISIBLE
}

fun ProgressBar.hide(){
    visibility = View.GONE
}


fun LinearLayout.hideLinerlyt(){
    visibility = View.GONE
}

fun View.snackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()
}

@BindingAdapter("changecolor")
fun TextView.changeColor(color: String) {
    try{
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadii = floatArrayOf(25f, 25f, 25f, 25f, 25f, 25f, 25f, 25f)
        shape.setColor(Color.parseColor(color))
        setBackground(shape)
    }catch (e:Exception){
        e.printStackTrace()
    }
}


@BindingAdapter("changecolor")
fun Button.changeColor(color: String) {
    try{
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadii = floatArrayOf(30f, 30f, 30f, 30f, 30f, 30f, 30f, 30f)
        shape.setColor(Color.parseColor(color))
        setBackground(shape)
    }catch (e:Exception){
        e.printStackTrace()
    }
}


@BindingAdapter("imageUrl")
fun loadImage(view: CircleImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(view.context).load(url).error(R.drawable.profile_image).placeholder(R.drawable.glide_place_holder).into(view)
    }
}




@BindingAdapter("changecolor")
fun ProgressBar.setProgress(color: String) {
    try{

    }catch (e:Exception){
        e.printStackTrace()
    }
}