package Entities

import com.google.gson.annotations.SerializedName
import java.util.*

data class SignalData(
    @SerializedName("id") var id: String? = null,
    @SerializedName("longitude") var longitude: Double? = null,
    @SerializedName("latitude") var latitude: Double? = null,
    @SerializedName("level") var level: String? = null,
    @SerializedName("dbm") var dbm: Int? = null,
    @SerializedName("rsrp") var rsrp: Int? = null,
    @SerializedName("rsrq") var rsrq: Int? = null,
    @SerializedName("rssi") var rssi: Int? = null,
    @SerializedName("rssnr") var rssnr: Int? = null,
    @SerializedName("ecio") var ecio: Int? = null,
    @SerializedName("evdoSnr") var evdoSnr: Int? = null,
    @SerializedName("operator") var operator: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("speedTest") var speedTest: String? = null,
    @SerializedName("latency") var latency: Int? = null
)

data class ItemSignal(
    @SerializedName("id") val id: String,
    @SerializedName("date") val fecha: String)

data class SignalDataRequest(
    @SerializedName("id") var id: String? = null,
    @SerializedName("longitude") var longitude: Double? = null,
    @SerializedName("latitude") var latitude: Double? = null,
    @SerializedName("level") var level: String? = null,
    @SerializedName("dbm") var dbm: Int? = null,
    @SerializedName("rsrp") var rsrp: Int? = null,
    @SerializedName("rsrq") var rsrq: Int? = null,
    @SerializedName("rssi") var rssi: Int? = null,
    @SerializedName("rssnr") var rssnr: Int? = null,
    @SerializedName("ecio") var ecio: Int? = null,
    @SerializedName("evdoSnr") var evdoSnr: Int? = null,
    @SerializedName("operator") var operator: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("speedTest") var speedTest: String? = null,
    @SerializedName("latency") var latency: Int? = null
)