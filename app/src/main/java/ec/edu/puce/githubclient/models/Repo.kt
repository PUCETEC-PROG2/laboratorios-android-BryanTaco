package ec.edu.puce.githubclient.models

import com.google.gson.annotations.SerializedName

data class Repo(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("description") val description: String? = null,
    @SerializedName("full_name") val fullName: String = "",
    @SerializedName("private") val isPrivate: Boolean = false,
    @SerializedName("stargazers_count") val stars: Int = 0,
    @SerializedName("forks_count") val forks: Int = 0,
    @SerializedName("language") val language: String? = null,
    @SerializedName("html_url") val htmlUrl: String = "",
    @SerializedName("updated_at") val updatedAt: String? = null,
    @SerializedName("owner") val owner: GithubUser? = null
)