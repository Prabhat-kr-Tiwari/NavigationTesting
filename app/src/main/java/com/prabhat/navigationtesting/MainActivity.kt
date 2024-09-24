package com.prabhat.navigationtesting

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.navigation.compose.composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.prabhat.navigationtesting.ui.theme.NavigationTestingTheme
import com.prabhat.navigationtesting.ui.theme.UserViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect // Import statement for collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn



class MainActivity : ComponentActivity() {

    private lateinit var connectivityObserver: ConnectivityObserver

    private lateinit var conectivityViewModel: ConnectivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        connectivityObserver=NetworkConnectivityObserver(applicationContext)

        conectivityViewModel= ConnectivityViewModel(connectivityObserver)

        setContent {
            NavigationTestingTheme {
                val userViewModel: UserViewModel = viewModel()
                val startRoute by userViewModel.startRoute.collectAsState()
                val navController = rememberNavController()
                NavigationComponent(userViewModel = userViewModel, navController = navController, startRoute =startRoute )


                //1st way
                val isInternetAvailable by conectivityViewModel.isInternetAvailable.collectAsState()

               /* LaunchedEffect(isInternetAvailable) {
                    val newRoute = if (isInternetAvailable) "2" else "1"
                    if (newRoute != userViewModel.startRoute.value) {
                        userViewModel.updateStartRoute(newRoute)
                        navController.navigate(newRoute) {
//                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }

                }*/
//                2nd way
              /*  LaunchedEffect(Unit) {
                    conectivityViewModel.isInternetAvailable.collect{isOnline->
                        Log.d("PRABHAT", "onCreate: "+isOnline)
                        userViewModel.updateStartRoute(if (isOnline) "2" else "1")

                    }

                }*/
                //your way
                //suggestion = use viewmodel for the networkstatus
                val networkStatus = conectivityViewModel.isInternetAvailable.map(Boolean::not).stateIn(
                    scope = rememberCoroutineScope(),
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = false
                )
                LaunchedEffect(key1= Unit) {

                    networkStatus.collect{isOffline->
                        userViewModel.updateStartRoute(if (isOffline) "1" else "2")

                    }

                }


            }
        }
    }
    @Composable
    private fun NavigationComponent(
        userViewModel: UserViewModel,
        navController: NavHostController,
        startRoute: String
    ) {
        NavHost(navController = navController, startDestination = startRoute) {

           composable("1") {
                ShowInternetStatus()
            }
            composable("2") {
                LoadUsers(userViewModel)
            }
        }
    }
}


@Composable
fun LoadUsers(userViewModel: UserViewModel) {

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Yellow)) {
        Text(text = "Load user")
    }
}

@Composable
fun ShowInternetStatus() {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Red)) {
        Text(text = "Show internet status")
    }
}

