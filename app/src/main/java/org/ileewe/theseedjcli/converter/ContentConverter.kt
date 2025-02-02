package org.ileewe.theseedjcli.converter

import androidx.room.TypeConverter
import org.ileewe.theseedjcli.model.Content

class ContentConverter {

    @TypeConverter
    fun fromContent(content: Content): String? {
        return content.rendered
    }

    @TypeConverter
    fun toContent(rendered: String?): Content? {
        return rendered?.let { Content(it) }
    }
}