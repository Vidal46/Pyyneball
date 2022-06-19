package com.example.core.component

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.example.core.R
import com.example.core.extension.formatDecimal

class MoneyWatcher(private val editText: EditText?) : TextWatcher {

    private var old = ""

    private fun formatValue(value: String): String =
        editText?.resources?.getString(R.string.money_format, value).orEmpty()

    override fun beforeTextChanged(
        charSequence: CharSequence,
        start: Int,
        count: Int,
        after: Int
    ) {
    }

    override fun afterTextChanged(editable: Editable) {}

    override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        if (text.toString() != old) {
            editText?.removeTextChangedListener(this)
            val cleanString = text.toString().trim().replace(Regex("[R$,.]"), "")

            val parsed = cleanString.toDoubleOrNull() ?: return
            val formatted = formatValue((parsed / 100.0).formatDecimal())

            old = formatted
            editText?.setText(formatted)
            editText?.setSelection(formatted.length)

            editText?.addTextChangedListener(this)
        }
    }

}
