package com.example.core.component

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText


class PhoneWatcher(private val editText: EditText?) : TextWatcher {

    companion object {
        private const val MASK_FIX = "(##) ####-####"
        private const val MASK_PORTABLE = "(##) # ####-####"
    }

    private var isUpdating: Boolean = false
    private var old = ""

    override fun afterTextChanged(s: Editable) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        try {
            val str = unmask(s.toString())
            val mask: String
            mask = when {
                str.length >= 11 -> MASK_PORTABLE
                else -> MASK_FIX
            }

            var placeholder = ""
            if (isUpdating) {
                old = str
                isUpdating = false
                return
            }
            var i = 0
            for (m in mask.toCharArray()) {
                if (m != '#' && str.length > old.length || m != '#' && str.length < old.length && str.length != i) {
                    placeholder += m
                    continue
                }

                try {
                    placeholder += str[i]
                } catch (e: Exception) {
                    break
                }

                i++
            }
            isUpdating = true
            editText?.setText(placeholder)
            editText?.setSelection(placeholder.length)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}