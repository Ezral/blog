# Homeventory (Android)

Offline-first home inventory app: organize items by **house → room → container → item**, with photos, search, and (upcoming) barcode scanning, consumable tracking, and expiry alerts.

## Requirements

- Android Studio Ladybug (2024.2.1) or newer
- JDK 17
- Android SDK 35
- Min SDK 26

## Get started

```bash
git clone https://github.com/Ezral/Homeventory.git
cd homeventory
```

Open the folder in **Android Studio**, let Gradle sync, then run on an emulator or device.

## Current status (v0.1.0-mvp)

| Feature | Status |
|---------|--------|
| Onboarding (house + template rooms) | Done |
| House / room / container hierarchy | Done |
| Items with photos (gallery picker) | Done |
| Location breadcrumbs | Done |
| Search | Done |
| Favorites on item detail | Done |
| Barcode scanning | Planned v1.1 |
| Consumable cycles & expiry | Planned v1.2 |
| Price trends & shopping list | Planned v1.3 |

## Planning documentation

Design specs: [`planning/README.md`](./planning/README.md)

## Stack

- Kotlin, Jetpack Compose, Material 3
- Room, Hilt, Navigation Compose
- Coil (images)

## Build

```bash
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest
```

## License

MIT
