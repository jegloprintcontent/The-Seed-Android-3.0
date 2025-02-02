package org.ileewe.theseedjcli.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey
    @SerializedName("id") @ColumnInfo(name = "id") val id: Int,
    @SerializedName("count") @ColumnInfo(name = "count") val count: Int,
    @SerializedName("description") @ColumnInfo(name = "description", defaultValue = "") val description: String?,
    @SerializedName("link") @ColumnInfo(name = "link") val link: String,
    @SerializedName("name") @ColumnInfo(name = "name") val name: String,
    @SerializedName("parent") @ColumnInfo(name = "parent") val parent: Int,
    @SerializedName("slug") @ColumnInfo(name = "slug") val slug: String,
    @SerializedName("taxonomy") @ColumnInfo(name = "taxonomy") val taxonomy: String
)