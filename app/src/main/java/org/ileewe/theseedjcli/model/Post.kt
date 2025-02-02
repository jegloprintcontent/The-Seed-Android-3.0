package org.ileewe.theseedjcli.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith

@Parcelize
@Entity(tableName = "posts")
data class Post (

    @PrimaryKey
    @SerializedName("id") @ColumnInfo(name = "id") val id: Int,
    @SerializedName("title") @ColumnInfo(name = "title") val title: @WriteWith<TitleParceler>() Title,
    @SerializedName("article_author") @ColumnInfo(name = "author") val article_author: String?,
    @SerializedName("content") @ColumnInfo(name = "content") val content: @WriteWith<ContentParceler>() Content,
    @SerializedName("categories") @ColumnInfo(name = "categories") val categories: List<Int>,
    @SerializedName("image") @ColumnInfo(name = "image") val image: String?,
    @SerializedName("audioURL") @ColumnInfo(name = "audioUrl") val audioURL: String?,  // New audioUrl field
    val date: String,
    val date_gmt: String,
    val link: String,
    val modified: String,
    val modified_gmt: String,
    val slug: String,
    val status: String,
    val template: String,
    val type: String,
    val tags: List<Int>
) : Parcelable

// Parcelers to handle the Title and Content fields
object TitleParceler : Parceler<Title> {
    override fun create(parcel: Parcel): Title = parcel.readString()?.let { Title(it) }!!
    override fun Title.write(parcel: Parcel, flags: Int) = parcel.writeString(rendered)
}

object ContentParceler : Parceler<Content> {
    override fun create(parcel: Parcel): Content = parcel.readString()?.let { Content(it) }!!
    override fun Content.write(parcel: Parcel, flags: Int) = parcel.writeString(rendered)
}
