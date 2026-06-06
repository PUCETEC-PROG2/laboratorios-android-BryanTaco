package ec.edu.puce.githubclient.Services

import ec.edu.puce.githubclient.models.GithubUser
import ec.edu.puce.githubclient.models.Repo
import ec.edu.puce.githubclient.models.RepositoryPayload
import ec.edu.puce.githubclient.models.UpdateRepoPayload
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {

    @GET("user")
    suspend fun getAuthenticatedUser(
        @Header("Authorization") token: String,
        @Header("User-Agent") userAgent: String = "Github-Client-PUCE"
    ): Response<GithubUser>

    @GET("user/repos")
    suspend fun getUserRepos(
        @Header("Authorization") token: String,
        @Query("sort") sort: String = "updated",
        @Query("per_page") perPage: Int = 50,
        @Header("User-Agent") userAgent: String = "Github-Client-PUCE"
    ): Response<List<Repo>>

    @POST("user/repos")
    suspend fun createRepo(
        @Header("Authorization") token: String,
        @Body body: RepositoryPayload,
        @Header("User-Agent") userAgent: String = "Github-Client-PUCE"
    ): Response<Repo>

    @PATCH("repos/{owner}/{repo}")
    suspend fun updateRepo(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repoName: String,
        @Body body: UpdateRepoPayload,
        @Header("User-Agent") userAgent: String = "Github-Client-PUCE"
    ): Response<Repo>

    @DELETE("repos/{owner}/{repo}")
    suspend fun deleteRepo(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repoName: String,
        @Header("User-Agent") userAgent: String = "Github-Client-PUCE"
    ): Response<Unit>

    companion object {
        private const val BASE_URL = "https://api.github.com/"

        fun create(): ApiService {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
