package eu.kanade.tachiyomi.extension.pt.toonlivre

import eu.kanade.tachiyomi.multisrc.madara.Madara
import keiyoushi.annotation.Source
import okhttp3.Interceptor
import okhttp3.OkHttpClient

@Source
abstract class Toonlivre : Madara() {

    // Desliga as chamadas AJAX que estão puxando o domínio morto do banco de dados do site
    override val useLoadMoreRequest = false
    override val useNewChapterEndpoint = false

    // Mantém o interceptor de segurança para corrigir links perdidos
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
            },
        )
        .build()
}
