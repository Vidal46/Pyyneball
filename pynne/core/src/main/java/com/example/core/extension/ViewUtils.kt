package com.example.core.extension

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

@BindingAdapter("android:visibility")
fun setViewVisibility(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.setVisibility(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

inline fun <reified T : RecyclerView.ViewHolder> ViewGroup.getViewHolder(
    createHolder: (View) -> T,
    @LayoutRes layout: Int
): T =
    createHolder(LayoutInflater.from(context).inflate(layout, this, false))