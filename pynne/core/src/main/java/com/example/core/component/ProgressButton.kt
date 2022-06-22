package com.example.core.component

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.DynamicDrawableSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.core.R

open class ProgressButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatButton(context, attrs, defStyle) {

    private var oldText = ""
        set(value) {
            field = value
            text = oldText
        }
    private var color: Int = 0
        set(value) {
            field = value
            invalidateDrawable(progressDrawable)
        }
    private var wasEnabled: Boolean = isEnabled

    private val progressDrawable by lazy {
        CircularProgressDrawable(context).apply {
            // let's use large style just to better see one issue
            setStyle(CircularProgressDrawable.LARGE)
            setColorSchemeColors(color)

            //bounds definition is required to show drawable correctly
            val size = (centerRadius + strokeWidth).toInt() * 2
            setBounds(0, 0, size, size)
        }
    }

    private val drawableSpan by lazy {
        object : DynamicDrawableSpan() {
            override fun getDrawable() = progressDrawable
        }
    }

    private val callback by lazy {
        object : Drawable.Callback {
            override fun unscheduleDrawable(who: Drawable, what: Runnable) {
            }

            override fun invalidateDrawable(who: Drawable) {
                invalidate()
            }

            override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
            }
        }
    }

    private val spannableString by lazy {
        SpannableString(" ").apply {
            setSpan(drawableSpan, length - 1, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.ProgressButton, defStyle, 0).apply {
            oldText = getString(R.styleable.ProgressButton_android_text).orEmpty()
            color = getColor(R.styleable.ProgressButton_progressColor, Color.WHITE)
            recycle()
        }
    }

    fun setLoading() {
        if (text != spannableString) oldText = text.toString()
        wasEnabled = isEnabled

        progressDrawable.start()
        progressDrawable.callback = callback
        text = spannableString
        isEnabled = false
    }

    fun endLoading(enabled: Boolean = wasEnabled) {
        text = oldText
        isEnabled = enabled
    }
}