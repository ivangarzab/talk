package com.ivangarzab.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

/**
 * The purpose of this utility class is to leverage the [ConnectivityManager.NetworkCallback]
 * in order to identify whether we have an active internet connection or not.
 */
class NetworkRepository(context: Context) {

    private val _networkAvailability = MutableStateFlow(false)

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Timber.v("Found an available network")
            _networkAvailability.value = true
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            // IMPORTANT: Emulators will NEVER get a Network with validated internet access;
            //  Make sure to always send 'true' when testing with an emulator.
            val hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            Timber.v("Network capabilities changed, has internet: $hasInternet")
            _networkAvailability.value = hasInternet
        }

        override fun onUnavailable() {
            super.onUnavailable()
            Timber.v("Network is unavailable")
            _networkAvailability.value = false
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            Timber.v("Network is lost")
            _networkAvailability.value = false
        }
    }

    private var connectivityManager: ConnectivityManager = context.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager

    init {
        startListeningForNetworkChanges()
    }

    private fun startListeningForNetworkChanges() {
        connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder().apply {
                addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }.build(),
            networkCallback
        )
    }

    /**
     * Provides a [StateFlow] to observe network availability changes.
     *
     * @return StateFlow that emits true when network is available, false otherwise
     */
    fun listenForNetworkAvailability(): StateFlow<Boolean> {
        return _networkAvailability.asStateFlow()
    }
}