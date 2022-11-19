package com.mathroda.favorite_coins

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mathroda.favorite_coins.components.authed_users.WatchListAuthedUsers
import com.mathroda.favorite_coins.components.ghost_users.WatchListGhostUsers
import com.mathroda.core.state.AuthenticationState

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun WatchListScreen(
    viewModel: FavoriteCoinsViewModel = hiltViewModel(),
    navController: NavController
) {
    viewModel.uiState()
    when(viewModel.authState.value) {

        is AuthenticationState.AuthedUser -> {
            viewModel.refresh()
            WatchListAuthedUsers(navController = navController)
        }

        is AuthenticationState.UnauthedUser -> {
            WatchListGhostUsers(navController)
        }

        else -> {}
    }
}