package org.ileewe.theseedjcli.utils

object Constants {

//    const val BASE_URL = "https://theseedjcli.ileewe.org/"
//    const val MINISTRIES_CATEGORY = 57
//    const val DEVOTIONS_CATEGORY = 41
//    const val SERMONS_CATEGORY = 56
//    const val DEFAULT_NUMBER_OF_POST = 40

    //CHANGING API SOURCE TO AMAZON S3 BUCKET
    // https://jeglo.s3.eu-west-2.amazonaws.com/apps/theseed/v1/latest-seeds.json - Latest seeds with S3
    // https://jeglo.s3.eu-west-2.amazonaws.com/apps/theseed/v1/latest-sermons.json - Sermons By S3 bucket
    // https://jeglo.s3.eu-west-2.amazonaws.com/apps/theseed/v1/list-ministries.json - Ministries By S3 Bucket

    //https://theseedjcli.ileewe.org/wp-json/wp/v2/posts?categories=42,44,45,46,47,48,49&per_page=20
    // https://jeglo.s3.eu-west-2.amazonaws.com/apps/theseed/v1/latest-ministries-posts.json - Ministries posts By S3 Bucket

    const val BASE_URL = "https://jeglo.s3.eu-west-2.amazonaws.com/apps/theseed/"
    const val MINISTRIES_CATEGORY = 57
    const val DEVOTIONS_CATEGORY = 41
    const val SERMONS_CATEGORY = 56
    const val DEFAULT_NUMBER_OF_POST = 40
}