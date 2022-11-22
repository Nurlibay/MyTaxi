package uz.nurlibaydev.mytaxi.utils.extensions

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan

fun String.coloredString(query: String?): Spannable {
    val spannable = SpannableStringBuilder(this)
    if (query == null) return spannable
    if (!this.contains(query)) return spannable
    if (query == this) {
        spannable.setSpan(
            ForegroundColorSpan(Color.parseColor("#017EF1")),
            0, this.length,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        return spannable
    }
    val start = this.indexOf(query)
    val end = query.length + start
    spannable.setSpan(
        ForegroundColorSpan(Color.parseColor("#017EF1")),
        start, end,
        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
    )
    return spannable
}

fun String.toDayEnglish(): String {
    var res = ""
    if (this == "Понедельник") {
        res = "Monday"
    } else if (this == "Вторник") {
        res = "Tuesday"
    } else if (this == "Среда") {
        res = "Wednesday"
    } else if (this == "Четверг") {
        res = "Thursday"
    } else if (this == "Пятница") {
        res = "Friday"
    } else if (this == "Суббота") {
        res = "Saturday"
    } else if (this == "Воскресенье") {
        res = "Sunday"
    } else {
        res = this
    }
    return res
}

fun String.toDayRus(): String {
    var res = ""
    if (this == "monday") {
        res = "Понедельник"
    } else if (this == "tuesday") {
        res = "Вторник"
    } else if (this == "wednesday") {
        res = "Среда"
    } else if (this == "thursday") {
        res = "Четверг"
    } else if (this == "friday") {
        res = "Пятница"
    } else if (this == "saturday") {
        res = "Суббота"
    } else if (this == "sunday") {
        res = "Воскресенье"
    } else {
        res = this
    }
    return res
}