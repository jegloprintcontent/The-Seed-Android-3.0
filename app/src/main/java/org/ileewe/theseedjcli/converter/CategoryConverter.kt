package org.ileewe.theseedjcli.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class CategoryConverter {

    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<Int?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<Int?>?>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<Int?>?): String? {
        return Gson().toJson(someObjects)
    }
}