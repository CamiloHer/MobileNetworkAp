package Entities

import com.google.gson.annotations.SerializedName

class ApiDataResponse {
}

data class ApiSaveResponse(
    @SerializedName("Result") val result: Boolean
)