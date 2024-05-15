package Helpers

import Entities.SignalData
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.telephony.CellIdentityCdma
import android.telephony.CellIdentityLte
import android.telephony.CellIdentityTdscdma
import android.telephony.CellSignalStrengthCdma
import android.telephony.CellSignalStrengthLte
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine

@RequiresApi(Build.VERSION_CODES.R)
class NetworkIndicatorsHelper {
    private lateinit var telephonyManager: TelephonyManager

    fun setupTelephonyManager(context: Context): SignalData {
        val signal: SignalData = SignalData()
        telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

        } else {
            val general = telephonyManager.allCellInfo
            Log.d("Result", general[0].toString())
            val info = telephonyManager.allCellInfo[0].cellSignalStrength
            val identity = telephonyManager.allCellInfo[0].cellIdentity
            if (info != null) {
                if (info is CellSignalStrengthLte) {
                    signal.rssi = info.rssi
                    signal.rsrq = info.rsrq
                    signal.rsrp = info.rsrp
                    signal.rssnr = info.rssnr
                    signal.dbm = info.dbm
                    signal.level = info.level.toString()
                } else if (info is CellSignalStrengthCdma) {
                    signal.rssi = info.cdmaDbm
                    signal.dbm = info.dbm
                    signal.ecio = info.cdmaEcio
                    signal.evdoSnr = info.evdoSnr
                    signal.level = info.level.toString()
                }
            }
            if (identity != null){
                signal.operator = when(identity){
                    is CellIdentityLte ->"${identity.mccString}${identity.mncString}"
                    is CellIdentityTdscdma -> "${identity.mccString}${identity.mncString}"
                    else-> null
                }
            }
            Log.d("signalStats",signal.toString())
        }
        return signal
    }

    @SuppressLint("MissingPermission")
    suspend fun getUserLocation(context: Context): Location? {
        val fusedOrientationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
        val isUserLocationPermissionGranted = true
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled =
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER
            )
        if (!isGPSEnabled || !isUserLocationPermissionGranted)
            return null
        return suspendCancellableCoroutine { cont ->
            fusedOrientationProviderClient.lastLocation.apply {
                if (isComplete) {
                    if (isSuccessful) {
                        cont.resume(result) {}
                    } else {
                        cont.resume(null) {}
                    }
                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener {
                    cont.resume(it) {}
                }
                addOnFailureListener { cont.resume(null) {} }
                addOnCanceledListener { cont.resume(null) {} }
            }
        }
    }

}