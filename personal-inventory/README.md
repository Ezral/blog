# Personal Inventory (Android)

Offline-first home inventory app: organize items by **house → room → container → item**, with photos, search, and (upcoming) barcode scanning, consumable tracking, and expiry alerts.

## Requirements

- Android Studio Ladybug (2024.2.1) or newer
- JDK 17
- Android SDK 35
- Min SDK 26

## Open in Android Studio

1. **Create the GitHub repo** (if you have not already):
   ```bash
   gh repo create personal-inventory --public --clone
   ```
2. Copy or clone this `personal-inventory/` folder into that repo root.
3. Open the folder in **Android Studio**.
4. Let Gradle sync, then run on an emulator or device.

## Current status (v0.1.0-mvp)

| Feature | Status |
|---------|--------|
| Onboarding (house + template rooms) | Done |
| House / room / container hierarchy | Done |
| Items with photos (gallery picker) | Done |
| Location breadcrumbs | Done |
| Search | Done |
| Favorites & recent views | Partial (favorites on detail; recent tracked) |
| Barcode scanning | Planned v1.1 |
| Consumable cycles & expiry | Planned v1.2 |
| Price trends & shopping list | Planned v1.3 |

## Planning documentation

Full design specs live in [`planning/`](./planning/README.md).

## Stack

- Kotlin, Jetpack Compose, Material 3
- Room, Hilt, Navigation Compose
- Coil (images)
- CameraX + ML Kit (v1.1)

## Project structure

```
app/src/main/java/com/ezral/personalinventory/
├── data/          # Room entities, DAOs, repositories
├── domain/        # Models and drafts
├── di/            # Hilt modules
└── ui/            # Compose screens & navigation
```

## Build from command line

```bash
./gradlew :app:assembleDebug
```

## Splitting from the blog monorepo

This project was scaffolded under `Ezral/blog/personal-inventory/`. To use it as a standalone repo:

```bash
cd personal-inventory
git init
git add .
git commit -m "Initial Android MVP scaffold"
git remote add origin git@github.com:Ezral/personal-inventory.git
git push -u origin main
```

## License

MIT (align with your preference when publishing).
