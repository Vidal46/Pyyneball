package com.example.core.component

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText

class MaskWatcher(private val editText: EditText?, private val mask: String) : TextWatcher {

    private val emptyFilters = arrayOf<InputFilter>()
    private var isUpdating: Boolean = false
    private var old = ""

    override fun beforeTextChanged(
        charSequence: CharSequence,
        start: Int,
        count: Int,
        after: Int
    ) {
    }

    override fun afterTextChanged(editable: Editable) {
        val unmaskedString = editable.toString().unmask()
        val maskedString = StringBuilder("")
        val mask = mask

        if (isUpdating) {
            old = unmaskedString
            isUpdating = false

            return
        }

        var i = 0

        for (m in mask.toCharArray()) {
            if (m != '#' && i < unmaskedString.length) {
                maskedString.append(m)
                continue
            }

            try {
                maskedString.append(unmaskedString[i])
            } catch (e: Exception) {
                break
            }

            i++
        }

        isUpdating = true

        val filters = editable.filters
        editable.filters = emptyFilters
        editable.replace(0, editable.length, maskedString)
        editable.filters = filters
    }

    override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {}
}

fun CharSequence.unmask(): String =
    replace("[.]".toRegex(), "").replace("[-]".toRegex(), "")
        .replace("[/]".toRegex(), "").replace(
            "[(]".toRegex(), ""
        ).replace("[ ]".toRegex(), "").replace("[:]".toRegex(), "").replace("[)]".toRegex(), "")

fun String.maskString(mask: String): String {
    val unmaskedString = this.unmask()
    val maskedString = StringBuilder("")
    val maskCharArray = mask.toCharArray()

    var i = 0
    for (c in maskCharArray) {
        if (c != '#' && i < unmaskedString.length) {
            maskedString.append(c)
            continue
        }

        try {
            maskedString.append(unmaskedString[i])
        } catch (e: Exception) {
            e.printStackTrace()
            break
        }

        i++
    }

    return maskedString.toString()
}
