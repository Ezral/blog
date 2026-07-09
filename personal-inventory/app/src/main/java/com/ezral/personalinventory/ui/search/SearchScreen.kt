package com.ezral.personalinventory.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ezral.personalinventory.data.local.dao.ItemLocationRow
import com.ezral.personalinventory.data.repository.ItemRepository
import com.ezral.personalinventory.ui.components.EmptyState
import com.ezral.personalinventory.ui.components.InventoryScaffold
import com.ezral.personalinventory.ui.components.LocationBreadcrumb
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
) : ViewModel() {
    private val query = MutableStateFlow("")

    val results: StateFlow<List<ItemLocationRow>> = query
        .flatMapLatest { q ->
            if (q.isBlank()) flowOf(emptyList()) else itemRepository.searchWithLocation(q)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun setQuery(value: String) {
        query.value = value
    }
}

@Composable
fun SearchScreen(
    initialQuery: String = "",
    onOpenItem: (Long) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    var text by rememberSaveable { mutableStateOf(initialQuery) }
    val results by viewModel.results.collectAsStateWithLifecycle()

    androidx.compose.runtime.LaunchedEffect(initialQuery) {
        if (initialQuery.isNotBlank()) {
            text = initialQuery
            viewModel.setQuery(initialQuery)
        }
    }

    InventoryScaffold(title = "Search") { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                    viewModel.setQuery(it)
                },
                label = { Text("Search items") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true,
            )

            if (text.isBlank()) {
                EmptyState(
                    message = "Search by name, brand, or category.",
                    modifier = Modifier.fillMaxSize(),
                )
            } else if (results.isEmpty()) {
                EmptyState(
                    message = "No items match \"$text\".",
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    items(results, key = { it.itemId }) { row ->
                        SearchResultRow(row = row, onClick = { onOpenItem(row.itemId) })
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResultRow(row: ItemLocationRow, onClick: () -> Unit) {
    val path = listOfNotNull(row.houseName, row.roomName, row.containerName).joinToString(" › ")
    ListItem(
        headlineContent = { Text(row.itemName) },
        supportingContent = { LocationBreadcrumb(path) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    )
}
