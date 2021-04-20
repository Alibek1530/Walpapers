package uz.ali.imagefon.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.ali.imagefon.config.Config


class UnsplashClient {
    private var retrofit: Retrofit? = null
  fun getUnsplashClient(): Retrofit? {
        if (retrofit == null) {
            val client = OkHttpClient.Builder()
                .addInterceptor(HeaderInterceptor(Config.unsplash_access_key)).build()


            retrofit = Retrofit.Builder()
                .baseUrl(Config.BASE_URL_UNSPLASH)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }
}