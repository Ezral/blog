package com.ezral.personalinventory.ui.placeholder

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ezral.personalinventory.ui.components.InventoryScaffold

@Composable
fun ScanPlaceholderScreen() {
    InventoryScaffold(title = "Scan") { padding ->
        Text(
            text = "Barcode scanning arrives in v1.1.\nUse Search or Browse for now.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
        )
    }
}

@Composable
fun ListsPlaceholderScreen() {
    InventoryScaffold(title = "Lists") { padding ->
        Text(
            text = "Shopping list and expiry alerts arrive in v1.2–v1.3.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
        )
    }
}
