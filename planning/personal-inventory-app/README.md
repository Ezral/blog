# Personal Inventory Android App — Planning Docs

Design documentation for an offline-first Android application to manage household inventory across a hierarchical location model, with barcode scanning, consumable lifecycle tracking, expiry management, and price trend analytics.

## Document Index

| Doc | Description |
|-----|-------------|
| [01-product-overview.md](./01-product-overview.md) | Goals, users, scope, and design principles |
| [02-architecture.md](./02-architecture.md) | Tech stack, layers, modules, and platform services |
| [03-database-schema.md](./03-database-schema.md) | Room entities, relationships, indexes, and migrations |
| [04-scanning.md](./04-scanning.md) | Barcode/QR scanning pipeline and integration points |
| [05-consumables-expiry-pricing.md](./05-consumables-expiry-pricing.md) | Cycles, expiry, usage duration, price trends, replenishment |
| [06-ui-flows.md](./06-ui-flows.md) | Navigation, screens, and key user journeys |
| [07-build-phases.md](./07-build-phases.md) | Phased delivery plan and acceptance criteria |

## Hierarchy Model

```
House
 └── Room
      └── Container (cabinet, drawer, shelf, box — nestable)
           └── Item (with photos, barcode, optional consumable data)
```

## Quick Reference

- **Stack:** Kotlin, Jetpack Compose, Room, Hilt, CameraX, ML Kit
- **Architecture:** MVVM + use cases, repository pattern
- **Consumable expiry:** Per purchase cycle (optional stock lots in v1.3+)
- **Primary actions:** Browse, Search, Scan, Lists (shopping + alerts)

## Status

Planning only — no implementation in this repository.
