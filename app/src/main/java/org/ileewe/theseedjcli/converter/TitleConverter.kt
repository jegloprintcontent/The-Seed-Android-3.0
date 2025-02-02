package org.ileewe.theseedjcli.converter

import androidx.room.TypeConverter
import org.ileewe.theseedjcli.model.Title

class TitleConverter {

    @TypeConverter
    fun fromTitle(title: Title): String? {
        return title.rendered
    }

    @TypeConverter
    fun toTitle(rendered: String?): Title? {
        return rendered?.let { Title(it) }
    }
}