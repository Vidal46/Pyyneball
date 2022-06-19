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

fun View.makeInvisible() {
    visibility = View.INVISIBLE
}

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun ImageView.blendImage() {
    val paint = Paint()
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DARKEN)
    setLayerType(View.LAYER_TYPE_HARDWARE, paint)
}

inline fun <reified T : RecyclerView.ViewHolder> ViewGroup.getViewHolder(
    createHolder: (View) -> T,
    @LayoutRes layout: Int
): T =
    createHolder(LayoutInflater.from(context).inflate(layout, this, false))

fun View.setMargins(
    leftMarginDp: Float? = null,
    topMarginDp: Float? = null,
    rightMarginDp: Float? = null,
    bottomMarginDp: Float? = null
) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val params = layoutParams as ViewGroup.MarginLayoutParams
        leftMarginDp?.run { params.marginStart = this.roundToInt() }
        topMarginDp?.run { params.topMargin = this.roundToInt() }
        rightMarginDp?.run { params.marginEnd = this.roundToInt() }
        bottomMarginDp?.run { params.bottomMargin = this.roundToInt() }
        layoutParams = params
        requestLayout()
    }
}

fun Float.dpToPx(context: Context): Int {
    val metrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, metrics).toInt()
}

fun TextView.setBoldText(text: String, boldText: String) {
    val start = text.indexOf(boldText)
    val end = start + boldText.length
    if (start >= 0) {
        setBoldText(text, start, end)
    } else setText(text)
}

fun TextView.setBoldText(text: String, start: Int, end: Int) {
    if (start >= 0) {
        val spannable = SpannableStringBuilder(text).apply {
            setSpan(
                StyleSpan(Typeface.BOLD),
                start,
                end,
                SpannableStringBuilder.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
        setText(spannable)
    } else setText(text)
}

fun TextView.setValidValue(text: String) {
    if (text.toDouble() > 0.0) {
        setText(text)
    } else setText("--")
}

fun Editable.toDouble() = toString().toInt().formatValueToDouble()
