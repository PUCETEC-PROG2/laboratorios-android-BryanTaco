package ec.edu.puce.githubclient.models

import com.google.gson.annotations.SerializedName

data class RepositoryPayload(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String = "",
    @SerializedName("private") val isPrivate: Boolean = false,
    @SerializedName("auto_init") val autoInit: Boolean = true
)

data class UpdateRepoPayload(
    @SerializedName("name") val name: String? = null,
    @SerializedName("description") val description: String? = null
)