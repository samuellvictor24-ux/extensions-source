package eu.kanade.tachiyomi.extension.pt.toonlivre

import eu.kanade.tachiyomi.multisrc.madara.Madara
import keiyoushi.annotation.Source
import okhttp3.Interceptor
import okhttp3.OkHttpClient

@Source
abstract class Toonlivre : Madara() {

    override val client: OkHttpClient = super.client.newBuilder()
        .addInterceptor(
            Interceptor { chain ->
                val request = chain.request()
                val urlString = request.url.toString()
                if (urlString.contains("mangalivre.net") || urlString.contains("mangalivre.tv")) {
                    val newUrl = request.url.newBuilder()
                        .host("toonlivre.net")
                        .build()
                    chain.proceed(request.newBuilder().url(newUrl).build())
                } else {
                    chain.proceed(request)
                }
            }
        )
        .build()
}
