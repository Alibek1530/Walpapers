package uz.ali.imagefon.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uz.ali.imagefon.models.Download
import uz.ali.imagefon.models.Photo
import uz.ali.imagefon.models.SearchResults

interface UnsplashInterface {

    @GET("photos/")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int?,
        @Query("order_by") orderBy: String
    ): Response<List<Photo>>

    @GET("photos/curated")
    suspend fun getCuratedPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int?,
        @Query("order_by") orderBy: String
    ): Response<List<Photo>>

    @GET("photos/{id}")
    suspend fun getPhoto(
        @Path("id") id: String?,
        @Query("w") width: Int?,
        @Query("h") height: Int?
    ): Response<Photo>
 //   https://api.unsplash.com/photos/W1bFUDQnzBU?

    @GET("photos/random")
    suspend fun getRandomPhoto(
        @Query("collections") collections: String?,
        @Query("featured") featured: Boolean?,
        @Query("username") username: String?,
        @Query("query") query: String?,
        @Query("w") width: Int?,
        @Query("h") height: Int?,
        @Query("orientation") orientation: String?
    ): Response<Photo>

    @GET("photos/random")
    suspend fun getRandomPhotos(
        @Query("collections") collections: String?,
        @Query("featured") featured: Boolean,
        @Query("username") username: String?,
        @Query("query") query: String?,
        @Query("w") width: Int?,
        @Query("h") height: Int?,
        @Query("orientation") orientation: String?,
        @Query("count") count: Int?
    ): Response<List<Photo>>

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String?,
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("orientation") orientation: String?
    ): Response<SearchResults>
//lukaszischke
    //https://api.unsplash.com/users/lianamikah/photos?client_id=sObwiOJsybwn3myzraV_SOytQxCsT9mXo9TPuliXvxk&w=720&h=1280&page=14
    @GET("users/{name}/photos")
    suspend fun getPhotosAftora(@Path("name") name: String?):Response<List<Photo>>

    @GET("photos/{id}/download")
    suspend  fun getPhotoDownloadLink(@Path("id") id: String?): Response<Download>
}