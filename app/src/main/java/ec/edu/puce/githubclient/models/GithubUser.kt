package ec.edu.puce.githubclient.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializer

data class GithubUser(
    val id: String,
    val login: String,
    @SerializedName(value="avatar_url")
    val avatarUrl: String
)