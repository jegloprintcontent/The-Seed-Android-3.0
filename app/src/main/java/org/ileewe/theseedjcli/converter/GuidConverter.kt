package org.ileewe.theseedjcli.converter

import androidx.room.TypeConverter
import org.ileewe.theseedjcli.model.Guid

class GuidConverter {

    @TypeConverter
    fun fromGuid(guid: Guid): String? {
        return guid.rendered
    }

    @TypeConverter
    fun toGuid(rendered: String?): Guid? {
        return rendered?.let { Guid(it) }
    }
}