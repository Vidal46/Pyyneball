package com.example.core.component

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.MutableLiveData

class InputTextWatcher(
    private val validationData: MutableLiveData<Boolean>,
    private val validate: (text: CharSequence?, hasFocus: Boolean, hasChange: Boolean) -> Boolean?
) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        validationData.postValue(validate(s, true, true))
    }
}

fun unmask(s: String): String =
    s.replace("[.]".toRegex(), "").replace("[-]".toRegex(), "")
        .replace("[/]".toRegex(), "").replace(
            "[(]".toRegex(), ""
        ).replace("[ ]".toRegex(), "").replace("[:]".toRegex(), "").replace("[)]".toRegex(), "")