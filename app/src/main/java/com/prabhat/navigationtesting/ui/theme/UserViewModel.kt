package com.prabhat.navigationtesting.ui.theme

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class AllEvents {
    object Nothing : AllEvents()  // Represents no event/state
    data class UserLoaded(val users: List<String>) : AllEvents() // Represents a list of users being loaded
    object Loading : AllEvents()  // Represents a loading state
    data class Error(val message: String) : AllEvents()  // Represents an error state
}

class UserViewModel() : ViewModel() {

    private val _startRoute = MutableStateFlow("1")
    val startRoute: StateFlow<String> = _startRoute.asStateFlow()

    val _users = MutableStateFlow<AllEvents>(AllEvents.Nothing)
    val users: StateFlow<AllEvents> = _users.asStateFlow()

    fun updateStartRoute(route: String) {
        _startRoute.value = route
    }
}