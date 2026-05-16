package ec.edu.puce.githubclient.ui.theme.model

import com.google.gson.annotations.SerializedName

data class Repo(
    val id: Long,
    val name: String,
    val description: String?,
    val language: String?,
    @SerializedName("owner")
    val owner: Owner,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class Owner(
    val login: String,
    @SerializedName("avatar_url")
    val avatarUrl: String
)