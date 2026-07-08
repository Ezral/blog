# 01 — Product Overview

## Problem

Personal belongings are spread across rooms and storage furniture. Finding items, knowing what to restock, tracking how long consumables last, and avoiding expired food or products is difficult without a structured system.

## Solution

An Android app that mirrors how homes are organized: **house → room → container → item**. Users can photograph items, scan barcodes or QR codes, and—for consumables—track purchases, expiry, finish dates, usage duration, and price changes over time.

## Target User

- Homeowners or renters managing one or more properties
- Users who want offline access and privacy (local-first data)
- People who restock groceries, cleaning supplies, or toiletries and want usage and cost insights

## Goals

| Goal | How the app addresses it |
|------|--------------------------|
| Fast retrieval | Global search, barcode scan-to-find, favorites, recent items, location breadcrumbs |
| Easy replenishment | Low-stock thresholds, expiring-soon alerts, shopping list, “buy again” from last purchase |
| Accurate organization | Hierarchical locations with nestable containers (e.g. drawer inside cabinet) |
| Low friction capture | Scan-first add flow, camera photos, category-aware defaults |
| Consumable insight | Usage duration per cycle, price-over-time charts, store comparison |

## Non-Goals (Initial Releases)

- Multi-user real-time sync
- Commercial warehouse / SKU management at scale
- Integration with retailer online accounts
- Automatic receipt OCR (future consideration)

## Design Principles

1. **Location is always visible** — Every item shows where it lives (breadcrumb).
2. **Scan is one tap away** — Primary FAB on browse and search screens.
3. **Progressive complexity** — Durable items need minimal fields; consumables reveal date/price/expiry fields when toggled.
4. **Offline-first** — Full functionality without network; optional product lookup when online.
5. **Sensible defaults** — Today’s date for purchase/finish; currency from settings; expiry prompts for food/medicine categories.

## Entity Summary

| Entity | Description | Example |
|--------|-------------|---------|
| House | A property | "Main Home", "Beach Condo" |
| Room | Area within a house | Kitchen, Garage, Bedroom |
| Container | Furniture or storage unit | Pantry cabinet, Drawer 2, Plastic bin |
| Item | A tracked object | Olive oil, AA batteries, Drill |
| Consumable cycle | One purchase-to-finish period | Bought rice Jan 2, finished Jan 23 |
| Stock lot | (Optional) Unit with its own expiry | 2 milk cartons, different dates |

## Success Metrics (Post-Launch)

- Time to find an item via scan &lt; 3 seconds
- Add new item with scan &lt; 30 seconds
- Users act on expiring-soon notifications within 48 hours
- Replenishment list reduces “forgot to buy” incidents (qualitative)
