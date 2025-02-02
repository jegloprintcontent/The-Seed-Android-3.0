package org.ileewe.theseedjcli.utils

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtils {

    companion object {
        @SuppressLint("SimpleDateFormat")
        fun getDate(dateString: String): String? {

            var date: String? = null
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            formatter.timeZone = TimeZone.getDefault() //TimeZone.getTimeZone("UTC")
            try {
                val dateFromDateString = formatter.parse(dateString)?.time
                //date = dateFromDateString?.toString()
                date = dateFromDateString?.let {
                    DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH).format(it)
                    //Date(it).toLocaleString().toString()
                }
            }catch(e: ParseException) {
                e.printStackTrace()
            }

            return date
        }
    }
}