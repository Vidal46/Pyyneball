package com.example.core.component

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.text.InputFilter
import android.text.InputType
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import android.text.Spanned
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.children
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.core.R
import com.example.core.extension.setVisibility
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

typealias Validation = (text: CharSequence?, hasFocus: Boolean, hasChange: Boolean) -> Boolean?

class Input @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayoutCompat(context, attrs, defStyle) {

    private var inputText: TextInputEditText? = null
    private var inputLayout: TextInputLayout? = null
    private var inputError: TextView? = null

    private var inputValidation: InputTextWatcher? = null
    private val validationData = MutableLiveData<Boolean>()
    private val validationObserver = Observer<Boolean> {
        when (it) {
            true -> inputDefault()
            false -> inputError()
            else -> inputSuccess()
        }
    }

    var validate: (Validation)? = null
        set(value) {
            field = value
            if (field != null) {
                setupValidation()
            } else
                inputText?.removeTextChangedListener(inputValidation)
        }

    private var maskWatcher: MaskWatcher

    var mask: String = ""
        set(value) {
            field = value
            inputText?.removeTextChangedListener(maskWatcher)
            if (field.isNotEmpty()) {


                maskWatcher = MaskWatcher(inputText, field)
                inputText?.addTextChangedListener(maskWatcher)


            }
        }

    private val digitsFilter = object : InputFilter {
        override fun filter(
            src: CharSequence?,
            start: Int,
            end: Int,
            dest: Spanned?,
            dstart: Int,
            dend: Int
        ): CharSequence {
            if (src?.equals("") == true) {
                return src
            }
            if (src?.toString()?.matches(Regex("[a-zA-Z0-9 ]+")) == true) {
                return src
            }
            return ""
        }

    }

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.input, this, true)
        orientation = VERTICAL

        try {
            inputLayout = children.firstOrNull() as? TextInputLayout
            inputText =
                (inputLayout?.children?.firstOrNull() as? FrameLayout)?.children?.find { it is TextInputEditText } as? TextInputEditText
            inputError = children.elementAtOrNull(1) as? TextView
        } catch (e: Exception) {
            e.printStackTrace()
        }

        maskWatcher = MaskWatcher(inputText, mask)

        isSaveEnabled = true

        inputDefault()
        setupValidation()

        attrs?.let {
            val a = context.obtainStyledAttributes(it, R.styleable.Input)
            with(a) {
                mask = getString(R.styleable.Input_mask) ?: ""
                val inputType = a.getInt(
                    R.styleable.Input_android_inputType,
                    TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
                )
                inputText?.inputType = inputType
                inputText?.imeOptions = a.getInt(R.styleable.Input_android_imeOptions, 0)
                inputLayout?.hint = getString(R.styleable.Input_android_hint)
                val maxLength = getInt(R.styleable.Input_android_maxLength, -1)
                var filters = inputText?.filters?.toList().orEmpty()
                if (maxLength >= 0)
                    filters = filters.plus(InputFilter.LengthFilter(maxLength))
                if (getBoolean(R.styleable.Input_alphanumeric, false))
                    filters = filters.plus(digitsFilter)
                inputText?.filters = arrayOf(*filters.toTypedArray())
                val drawableEnd = getResourceId(R.styleable.Input_android_drawableEnd, -1)
                if (drawableEnd != -1) {
                    inputLayout?.setEndIconDrawable(drawableEnd)
                    inputLayout?.editText?.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        AppCompatResources.getDrawable(context, drawableEnd),
                        null
                    )
                }
                inputLayout?.isEndIconVisible =
                    (inputType == TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD) || drawableEnd != -1
                if (inputType == TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD) {
                    inputLayout?.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                } else inputLayout?.endIconMode = TextInputLayout.END_ICON_CUSTOM

                recycle()
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        validationData.observeForever(validationObserver)
    }

    override fun onDetachedFromWindow() {
        validationData.removeObserver(validationObserver)
        super.onDetachedFromWindow()
    }

    override fun onSaveInstanceState(): Parcelable? {
        return try {
            val savedState = SavedState(super.onSaveInstanceState())
            savedState.text = getText()
            savedState.error = inputError?.text.toString()
            savedState.mask = mask
            savedState.inputType =
                inputText?.inputType ?: TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            savedState
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onRestoreInstanceState(state: Parcelable?) {
        try {
            if (state is SavedState) {
                super.onRestoreInstanceState(state.superState)
                cleanInput()
                setError(state.error.orEmpty())
                mask = state.mask.orEmpty()
                setText(state.text, state.inputType)
            } else super.onRestoreInstanceState(state)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setError(error: String) {
        if (error.isNotEmpty()) {
            inputError?.setVisibility(true)
            validationData.postValue(false)
            inputError?.text = error
        } else inputDefault()
        announceForAccessibility(error)
    }

    fun getText() = inputText?.text.toString()

    fun getRawText() = inputText?.text?.unmask().orEmpty()

    fun setText(text: String?, inputType: Int? = null) {
        inputType?.let { inputText?.inputType = it }
        inputText?.setText(text, TextView.BufferType.EDITABLE)
        validationData.postValue(validate?.invoke(text, false, false))
    }

    fun isValid(defaultValue: Boolean = !hasFocus()) =
        validate?.invoke(inputText?.text, false, false) ?: defaultValue

    fun cleanInput() {
        inputText?.text?.clear()
    }

    private fun setupValidation() {
        inputText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus()) inputSuccess()
            else inputDefault()
            validationData.postValue(validate?.invoke(inputText?.text, hasFocus, false))
        }

        inputText?.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
            ) {
                val valid = validate?.invoke(v.text, false, false)
                validationData.postValue(valid)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        inputValidation = InputTextWatcher(validationData, validate ?: return)
        inputText?.addTextChangedListener(inputValidation)
    }

    private fun inputSuccess() {
        if (inputText?.hasFocus() == true) {
            inputError?.setVisibility(false)
            inputLayout?.setBackgroundResource(R.drawable.success_background)
        } else inputDefault()
    }

    private fun inputDefault() {
        inputError?.setVisibility(false)
        inputLayout?.setBackgroundResource(R.drawable.selector_background)
    }

    fun addTextChangedListener(watcher: TextWatcher) {
        inputText?.addTextChangedListener(watcher)
    }

    private fun inputError() {
        inputError?.setVisibility(true)
        inputLayout?.setBackgroundResource(R.drawable.error_background)
    }

    internal class SavedState : BaseSavedState {

        var text: String? = null
        var error: String? = null
        var mask: String? = null

        var inputType: Int = InputType.TYPE_TEXT_VARIATION_NORMAL

        constructor(source: Parcel) : super(source) {
            try {
                text = source.readString()
                error = source.readString()
                mask = source.readString()
                inputType = source.readInt()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        constructor(superState: Parcelable?) : super(superState)

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            try {
                out.writeString(text)
                out.writeString(error)
                out.writeString(mask)
                out.writeInt(inputType)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        companion object {
            @JvmField
            @Suppress("unused")
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {

                override fun createFromParcel(source: Parcel): SavedState {
                    return SavedState(source)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}