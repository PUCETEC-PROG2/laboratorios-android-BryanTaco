package ec.edu.puce.githubclient.models

import com.google.gson.annotations.SerializedName

data class GithubUser(
    @SerializedName("login") val login: String = "",
    @SerializedName("id") val id: Long = 0,
    @SerializedName("avatar_url") val avatarUrl: String = "",
    @SerializedName("name") val name: String? = null,
    @SerializedName("bio") val bio: String? = null,
    @SerializedName("public_repos") val publicRepos: Int = 0
)