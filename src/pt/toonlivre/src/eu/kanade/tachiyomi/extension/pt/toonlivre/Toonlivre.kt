package eu.kanade.tachiyomi.extension.pt.toonlivre

import eu.kanade.tachiyomi.multisrc.madara.Madara
import keiyoushi.annotation.Source
import okhttp3.Interceptor
import okhttp3.Response

@Source
abstract class Toonlivre : Madara() {

    // Intercepta e corrige URLs legadas retornadas pela estrutura do site
    override val client = super.client.newBuilder()
        .addInterceptor(::domainRedirectInterceptor)
        .build()

    private fun domainRedirectInterceptor(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val urlString = request.url.toString()
        
        // Verifica se a URL extraída contém o domínio morto
        if (urlString.contains("mangalivre.net") || urlString.contains("mangalivre.tv")) {
            val newUrl = request.url.newBuilder()
                .host("toonlivre.net")
                .build()
            
            // Continua a requisição com o domínio host corrigido
            return chain.proceed(request.newBuilder().url(newUrl).build())
        }
        
        return chain.proceed(request)
    }
}
