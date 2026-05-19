package ec.edu.puce.githubclient.ui.theme.network

import ec.edu.puce.githubclient.ui.theme.model.Repo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {

    @GET("users/{username}/repos")
    suspend fun getRepos(
        @Path("username") username: String,
        @Query("sort") sort: String = "updated",
        @Query("per_page") perPage: Int = 10
    ): List<Repo>
}

object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: GithubApiService = retrofit.create(GithubApiService::class.java)
}