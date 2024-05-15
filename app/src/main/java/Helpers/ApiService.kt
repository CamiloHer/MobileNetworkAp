package Helpers

import Entities.ApiSaveResponse
import Entities.ItemSignal
import Entities.SignalData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("/api/Network/SaveSignal")
    suspend fun postSaveSignal(@Body signalData: SignalData): Response<Boolean>

    @GET("/api/Network/GetSignalData")
    suspend fun getSignals():Response<List<ItemSignal>>

    @GET("/api/Network/GetSignalById/{id}")
    suspend fun getSignalData(@Path("id") id :String): Response<SignalData>
}