package com.prabhat.navigationtesting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ConnectivityViewModel(private val connectivityObserver: ConnectivityObserver) : ViewModel() {

    // MutableStateFlow that will emit `true` when internet is available and `false` when lost
    private val _isInternetAvailable = MutableStateFlow(false)
    val isInternetAvailable: StateFlow<Boolean> = _isInternetAvailable

    init {
        // Launch a coroutine to observe connectivity changes
        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                // Update the MutableStateFlow based on the network status
                _isInternetAvailable.value = status == ConnectivityObserver.Status.Available
            }
        }
    }
}
