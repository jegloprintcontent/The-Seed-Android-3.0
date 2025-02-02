package org.ileewe.theseedjcli.utils

import android.content.Context
import android.util.Log
import org.ileewe.theseedjcli.R
import org.jsoup.nodes.Document
import java.util.*

class WebHelper {

    companion object {
        fun docToBetterHTML(context: Context, doc: Document): String? {
            val value = context.resources.getInteger(R.integer.reading_padding)
            try {
                doc.select("img[src]").removeAttr("width")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                if (doc.select("iframe").attr("src").contains("https://www.youtube.com/")) {
                    doc.select("iframe").attr("width", "100%")
                    doc.select("iframe").attr("height", "240px")
                } else {
                    doc.select("iframe").attr("width", "100%")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            /*try {

                if(doc.getElementsByTag("p").eq(0).text() != null
                        && !doc.getElementsByTag("p").eq(0).text().equals("&nbsp")) {
                    doc.getElementsByTag("p").eq(0).addClass("first-image");
                }

            }catch(Exception e){
                e.printStackTrace();
            }*/try {
                doc.getElementsByClass("alignnone").first()!!.remove()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                val el = doc.getElementsByTag("p")
                if (el.size > 0) {
                    for (i in el.indices) {
                        if (el[i]
                                .getElementsByTag("iframe")
                                .attr("src").contains("https://www.youtube.com/") ||
                            el[i].allElements
                                .attr("src").contains("//www.channelstv.com/")
                        ) {
                            el[i].addClass("paragraph-video")
                        }
                        Log.d("TAG", "element " + el[i].getElementsByTag("iframe"))
                        Log.d("TAG", "element " + el[i].getElementsByTag("img"))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                doc.select(".size-full").first()!!.removeAttr("width")
                doc.select(".size-full").addClass("myImage")
                //doc.select(".size-full").attr("style", "display:block;");
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                doc.select(".size-full").first()!!.removeAttr("width")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val style = ("<style>" +
                    "img{" +
                    "max-width: 100% !important; " +
                    "width: 100% !important; height: auto;" +
                    "}" +
                    "figure{" +
                    "padding: 0; " +
                    "margin: 0; " +
                    "max-width: 100% !important; " +
                    "width: 100% !important; height: auto;" +
                    "}"
                    +
                    "figcaption {" +
                    "font-size: 12px; " +
                    "padding:20px;" +
                    "background-color: #f2f2f2;" +
                    "}" +
                    "img.size-full {" +
                    "max-width: 100% !important; " +
                    "width: 100% !important; height: auto;" +
                    "}" +
                    ".myImage {" +
                    "max-width: 100% !important; " +
                    "width: 100% !important; height: auto;" +
                    "}" +
                    "body {" +
                    "font-family:Georgia,Times New Roman,Times,Cambria,serif !important;" +  /*"font-family: Roboto,Helvetica Neue,Helvetica,Arial,sans-serif!important" +*/
                    "padding: 0; margin: 0;" +
                    "}" +
                    "iframe {" +
                    "padding: 0; margin: 0; text-align: center;" +
                    "}" +
                    "video {" +
                    "width: 100%; " +
                    "height: 100%; " +
                    "}" +
                    "h1,h2,h3 {" +
                    "padding-left: " + value + "px; padding-right: " + value + "px;" +
                    "}" +
                    "p{" +
                    "max-width: 100%; " +
                    "width: auto; height: auto;" +
                    "margin-bottom: " + value + "px;" +
                    "padding-left: " + value + "px; padding-right: " + value + "px;" +
                    "line-height: 1.8;" +  //"font-size: 20px !important;" +
                    "}" +
                    "a {" +
                    "font-weight: 700; color: #5172a0 !important;" +
                    "}" +
                    "hr {" +
                    "border: 0;" +
                    "border-top: 1px solid #eee;" +
                    "}" +
                    ".paragraph-video {" +
                    "padding-left: 0px !important; padding-right: 0px !important;" +
                    "}" +
                    "blockquote:before {" + "content: '\"';font-size:30px; line-height:1.4;position:absolute;left: 40px; " + "}" +
                    "blockquote p{" + "font-weight:bold;margin: 0 auto;font-style:italic;" + "}"  +

                    "</style>")
            val body = doc.body()
            body.append(style)
            val html = doc.toString()
            //Log.v("TAG", "HTML " + html);
            Log.d("TAG", "document $doc")
            val strRegEx = "\r\n*"
            return html.replace(strRegEx.toRegex(), "<p>")
        }


        fun isSameDomain(url: String, url1: String): Boolean {
            return getRootDomainUrl(url.lowercase(Locale.getDefault())) == getRootDomainUrl(url1)
        }

        private fun getRootDomainUrl(url: String): String {
            val domainKeys = url.split("/").toTypedArray()[2].split("\\.").toTypedArray()
            val length = domainKeys.size
            val dummy = if (domainKeys[0] == "www") 1 else 0
            return if (length - dummy == 2) domainKeys[length - 2] + "." + domainKeys[length - 1] else {
                if (domainKeys[length - 1].length == 2) {
                    domainKeys[length - 3] + "." + domainKeys[length - 2] + "." + domainKeys[length - 1]
                } else {
                    domainKeys[length - 2] + "." + domainKeys[length - 1]
                }
            }
        }
    }


}