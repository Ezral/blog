package com.ezral.personalinventory.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ezral.personalinventory.ui.browse.ContainerDetailScreen
import com.ezral.personalinventory.ui.browse.HouseDetailScreen
import com.ezral.personalinventory.ui.browse.HousesScreen
import com.ezral.personalinventory.ui.browse.RoomDetailScreen
import com.ezral.personalinventory.ui.item.AddEditItemScreen
import com.ezral.personalinventory.ui.item.ItemDetailScreen
import com.ezral.personalinventory.ui.navigation.Routes
import com.ezral.personalinventory.ui.onboarding.OnboardingScreen
import com.ezral.personalinventory.ui.placeholder.ListsPlaceholderScreen
import com.ezral.personalinventory.ui.scan.ScanScreen
import com.ezral.personalinventory.ui.search.SearchScreen

private data class BottomTab(
    val route: String,
    val label: String,
    val icon: @Composable () -> Unit,
)

private val bottomTabs = listOf(
    BottomTab(Routes.HOUSES, "Browse") { Icon(Icons.Default.Home, contentDescription = null) },
    BottomTab(Routes.SEARCH, "Search") { Icon(Icons.Default.Search, contentDescription = null) },
    BottomTab(Routes.SCAN, "Scan") { Icon(Icons.Default.QrCodeScanner, contentDescription = null) },
    BottomTab(Routes.LISTS, "Lists") { Icon(Icons.Default.List, contentDescription = null) },
)

@Composable
fun PersonalInventoryAppRoot(showOnboarding: Boolean) {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Routes.HOUSES,
        Routes.SEARCH,
        Routes.SCAN,
        Routes.LISTS,
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomTabs.forEach { tab ->
                        NavigationBarItem(
                            selected = currentRoute == tab.route ||
                                (tab.route == Routes.SEARCH && currentRoute?.startsWith("search") == true),
                            onClick = {
                                val destination = if (tab.route == Routes.SEARCH) {
                                    Routes.search()
                                } else {
                                    tab.route
                                }
                                navController.navigate(destination) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = tab.icon,
                            label = { Text(tab.label) },
                        )
                    }
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = if (showOnboarding) Routes.ONBOARDING else Routes.HOUSES,
            modifier = Modifier.padding(padding),
        ) {
            composable(Routes.ONBOARDING) {
                OnboardingScreen(
                    onFinished = {
                        navController.navigate(Routes.HOUSES) {
                            popUpTo(Routes.ONBOARDING) { inclusive = true }
                        }
                    },
                )
            }

            composable(Routes.HOUSES) {
                HousesScreen(
                    onOpenHouse = { navController.navigate(Routes.houseDetail(it)) },
                )
            }

            composable(
                route = Routes.HOUSE_DETAIL,
                arguments = listOf(navArgument("houseId") { type = NavType.LongType }),
            ) { entry ->
                val houseId = entry.arguments?.getLong("houseId") ?: return@composable
                HouseDetailScreen(
                    houseId = houseId,
                    onBack = { navController.popBackStack() },
                    onOpenRoom = { navController.navigate(Routes.roomDetail(it)) },
                )
            }

            composable(
                route = Routes.ROOM_DETAIL,
                arguments = listOf(navArgument("roomId") { type = NavType.LongType }),
            ) { entry ->
                val roomId = entry.arguments?.getLong("roomId") ?: return@composable
                RoomDetailScreen(
                    roomId = roomId,
                    onBack = { navController.popBackStack() },
                    onOpenContainer = { navController.navigate(Routes.containerDetail(it)) },
                    onOpenItem = { navController.navigate(Routes.itemDetail(it)) },
                    onAddItem = { navController.navigate(Routes.addItem(roomId = roomId)) },
                )
            }

            composable(
                route = Routes.CONTAINER_DETAIL,
                arguments = listOf(navArgument("containerId") { type = NavType.LongType }),
            ) { entry ->
                val containerId = entry.arguments?.getLong("containerId") ?: return@composable
                ContainerDetailScreen(
                    containerId = containerId,
                    onBack = { navController.popBackStack() },
                    onOpenContainer = { navController.navigate(Routes.containerDetail(it)) },
                    onOpenItem = { navController.navigate(Routes.itemDetail(it)) },
                    onAddItem = { navController.navigate(Routes.addItem(containerId = containerId)) },
                )
            }

            composable(
                route = Routes.ITEM_DETAIL,
                arguments = listOf(navArgument("itemId") { type = NavType.LongType }),
            ) { entry ->
                val itemId = entry.arguments?.getLong("itemId") ?: return@composable
                ItemDetailScreen(
                    itemId = itemId,
                    onBack = { navController.popBackStack() },
                    onEdit = { navController.navigate(Routes.addItem(itemId = itemId)) },
                )
            }

            composable(
                route = Routes.ADD_ITEM,
                arguments = listOf(
                    navArgument("roomId") { type = NavType.LongType; defaultValue = -1L },
                    navArgument("containerId") { type = NavType.LongType; defaultValue = -1L },
                    navArgument("itemId") { type = NavType.LongType; defaultValue = -1L },
                    navArgument("barcode") { type = NavType.StringType; defaultValue = "" },
                ),
            ) { entry ->
                val roomId = entry.arguments?.getLong("roomId")?.takeIf { it > 0 }
                val containerId = entry.arguments?.getLong("containerId")?.takeIf { it > 0 }
                val itemId = entry.arguments?.getLong("itemId")?.takeIf { it > 0 }
                val barcode = entry.arguments?.getString("barcode").orEmpty().ifBlank { null }
                AddEditItemScreen(
                    roomId = roomId,
                    containerId = containerId,
                    itemId = itemId,
                    initialBarcode = barcode,
                    onBack = { navController.popBackStack() },
                    onSaved = { id ->
                        navController.navigate(Routes.itemDetail(id)) {
                            popUpTo(Routes.HOUSES)
                        }
                    },
                )
            }

            composable(
                route = Routes.SEARCH,
                arguments = listOf(
                    navArgument("query") {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                ),
            ) { entry ->
                val query = entry.arguments?.getString("query").orEmpty()
                SearchScreen(
                    initialQuery = query,
                    onOpenItem = { navController.navigate(Routes.itemDetail(it)) },
                )
            }

            composable(Routes.SCAN) {
                ScanScreen(
                    onOpenItem = { navController.navigate(Routes.itemDetail(it)) },
                    onAddItemWithBarcode = { barcode ->
                        navController.navigate(Routes.addItem(barcode = barcode))
                    },
                )
            }
            composable(Routes.LISTS) { ListsPlaceholderScreen() }
        }
    }
}
