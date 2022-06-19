package com.example.core.extension

import android.net.Uri
import android.text.Html
import android.text.Spanned
import android.util.Base64
import android.util.Patterns
import java.net.URLEncoder
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import java.util.regex.Pattern
import javax.crypto.Cipher

fun String.encode(): String = URLEncoder.encode(this, "UTF-8")

fun String?.isValidEmail() =
    this != null && Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), this)

fun String?.validPhone(): Boolean =
    this != null && Pattern.matches(Patterns.PHONE.pattern(), this) && this.isValidPhone()

fun String?.isValidPhone(): Boolean = this != null && !this.allNumbersAreEquals()

fun String?.allNumbersAreEquals(): Boolean {
    if (this == null || this.isEmpty()) return false

    val numbers = arrayListOf<Int>()

    this.filter { it.isDigit() }.forEach {
        numbers.add(it.toString().toInt())
    }

    if (numbers.size == 10 || numbers.size == 11) {
        numbers.forEach { n -> if (n != numbers[0]) return false }
    }

    return true
}

fun String?.thereIsNumber(): Boolean {
    if (this == null) return false

    val pattern = "[A-Za-zÀ-ÿ '-]*".toRegex()
    return !pattern.matches(this)
}

fun String.removeLetters(): String {

    val chars = this.toCharArray()

    val charsFiltered = chars.filter {
        !it.isLetter()
    }

    return String(charsFiltered.toCharArray())
}

fun String.toCamelCase(): String {
    var result = ""
    if (this.isEmpty()) {
        return result
    }
    val firstChar = this[0]
    val firstCharToUpperCase = Character.toUpperCase(firstChar)
    result += firstCharToUpperCase
    for (i in 1 until this.length) {
        val currentChar = this[i]
        val previousChar = this[i - 1]
        result = if (previousChar == ' ') {
            val currentCharToUpperCase = Character.toUpperCase(currentChar)
            result + currentCharToUpperCase
        } else {
            val currentCharToLowerCase = Character.toLowerCase(currentChar)
            result + currentCharToLowerCase
        }
    }
    return result
}

fun String?.isValidCpf(): Boolean {
    if (this == null || this.isEmpty()) return false
    val numbers = arrayListOf<Int>()

    this.filter { it.isDigit() }.forEach {
        numbers.add(it.toString().toInt())
    }

    if (numbers.size != 11) return false

    (0..9).forEach { n ->
        val digits = arrayListOf<Int>()
        (0..10).forEach { _ -> digits.add(n) }
        if (numbers == digits) return false
    }

    val dv1 = ((0..8).sumBy { (it + 1) * numbers[it] }).rem(11).let {
        if (it >= 10) 0 else it
    }

    val dv2 = ((0..8).sumBy { it * numbers[it] }.let { (it + (dv1 * 9)).rem(11) }).let {
        if (it >= 10) 0 else it
    }

    return numbers[9] == dv1 && numbers[10] == dv2
}

fun String.onlyNumbers(): String = this.replace("[^0-9]+".toRegex(), " ").replace(" ", "")

fun Int.formatDecimal(): String = (this.toDouble() / 100.0).formatDecimal()

fun Int.formatMoney(): Double = this.toDouble() / 100.0

fun Double.formatDecimal(): String {
    val symbols = DecimalFormatSymbols(Locale.forLanguageTag("pt-BR"))
    symbols.decimalSeparator = ','
    symbols.groupingSeparator = '.'
    return DecimalFormat("#,##0.00", symbols).format(this)
}

fun String.formatDecimal(): String {
    return this.toDouble().formatDecimal()
}

fun String.formatToMoney(): String {
    return "R$ ${this.formatDecimal()}"
}

fun Number.formatToMoney(): String {
    if (this is Int)
        return "R$ ${this.formatMoney().formatDecimal()}"

    return "R$ ${this.toString().formatDecimal()}"
}

fun Number.formatToMoneyFreeShipping(): String {
    if(this is Int) {
        if(this == 0) return "Grátis"

        return "R$ ${this.formatMoney().formatDecimal()}"
    }
    return "R$ ${this.toString().formatDecimal()}"
}

fun Int.formatValueToDouble(): String =
    StringBuilder(this.toString()).insert(
        this.toString().length - 2,
        "."
    ).toString().toDouble().formatDecimal()

fun String.wordCount(): Int {
    var count = 0
    val ch = CharArray(this.length)
    for (i in this.indices) {
        ch[i] = this[i]
        if (i > 0 && ch[i] != ' ' && ch[i - 1] == ' ' || ch[0] != ' ' && i == 0)
            count++
    }
    return count
}

fun String.insert(index: Int, string: String): String {
    if (index > 0) {
        return this.substring(0, index) + string + this.substring(index, this.length);
    }
    return string + this
}

fun String.removeWhiteSpaces(): String = this.replace(" ", "")

fun String.replaceName(): String {
    val map =
        mapOf(
            " " to "_", "ç" to "c", "á" to "a", "ã" to "a", "ó" to "o", "õ" to "o", "é" to "e",
            "Ç" to "C", "Á" to "A", "Ã" to "A", "Ó" to "O", "Õ" to "O", "É" to "E"
        )
    var text = this

    for ((k, v) in map) {
        text = text.replace(k, v)
    }

    return text
}

fun String.parsePeriod() =
    when (this) {
        "BUSINESS_HOURS" -> "08:00 - 18:00"
        "MORNING" -> "08:00 - 12:00"
        "AFTERNOON" -> "12:00 - 18:00"
        else -> ""
    }

fun String.getNamePaymentSystem() =
    when (this) {
        "4" -> "Mastercard"
        "1" -> "Amex"
        "2" -> "Visa"
        "3" -> "Diners"
        "9" -> "Elo"
        else -> "Visa"
    }

fun String.getRegexFromPaymentSystem() =
    when (this) {
        "1" -> "^3[47][0-9]{13}\$"
        "2" -> "^4[0-9]{12}(?:[0-9]{3})?\$"
        "3" -> "^3(?:0[0-5]|[68][0-9])[0-9]{11}\$"
        "4" -> "^(5[1-5]|222[1-9]|22[3-9]|2[3-6]|27[0-1]|2720)\\d*"
        "9" -> "(?:401178|401179|431274|438935|451416|457393|457631|457632|504175|627780|636297|636368|655000|655001|651652|651653|651654|650485|650486|650487|650488|506699|5067[0-6][0-9]|50677[0-8]|509\\d{3})\\d{10}\$"
        "21" -> "^(?:2131|1800|35\\d{3})\\d{11}\$"
        else -> ""
    }

fun String.stripHtml(): String {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this.replace("nn", ""), Html.FROM_HTML_MODE_LEGACY).toString()
            .replace(Regex("<[^>]*>"), "")
    } else {
        @Suppress("DEPRECATION")
        this.replace(Regex("<[^>]*>"), "")
    }
}

fun String.fromHtml(): Spanned {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this)
    }
}

fun String.formatCPF(): String {
    return try {
        substring(0, 3) + "." + substring(3, 6) + "." + substring(
            6,
            9
        ) + "-" + substring(9, 11)
    } catch (e: Exception) {
        this
    }
}

fun String.parseUrl(): Uri = Uri.parse(this)