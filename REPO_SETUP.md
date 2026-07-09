# Publish as `Ezral/personal-inventory`

This branch contains the **standalone** Android project at the repository root (not inside a blog monorepo).

The cloud agent cannot create new GitHub repositories on your account. Run these steps **once** on your laptop:

## 1. Create the empty repository on GitHub

```bash
gh repo create Ezral/personal-inventory --public \
  --description "Android personal home inventory — hierarchical storage, barcodes, consumables & expiry"
```

Or create **Ezral/personal-inventory** manually at https://github.com/new (empty, no README).

## 2. Push this branch to the new repo

```bash
git clone -b personal-inventory https://github.com/Ezral/blog.git personal-inventory
cd personal-inventory
git remote rename origin blog
git remote add origin https://github.com/Ezral/personal-inventory.git
git push -u origin personal-inventory:main
```

## 3. Open in Android Studio

```bash
cd personal-inventory   # your clone of Ezral/personal-inventory
```

Android Studio → Open → run on emulator/device.

## 4. Optional — remove inventory branches from blog

After the new repo is live:

```bash
# Close open PRs #32 and #33 on Ezral/blog if still open (GitHub UI)
git push https://github.com/Ezral/blog.git --delete personal-inventory
git push https://github.com/Ezral/blog.git --delete cursor/personal-inventory-android-52f5
git push https://github.com/Ezral/blog.git --delete cursor/personal-inventory-planning-docs-52f5
```

The blog `master` branch was never modified with the Android app.
