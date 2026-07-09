# Sync into Ezral/Homeventory

Your repo: **https://github.com/Ezral/Homeventory**

The cloud agent cannot push directly to your repo (permissions). Pull this scaffold from the staging branch on `Ezral/blog`:

## Empty repo (recommended)

```bash
git clone https://github.com/Ezral/Homeventory.git
cd Homeventory
git remote add source https://github.com/Ezral/blog.git
git fetch source homeventory
git checkout -b main
git reset --hard source/homeventory
git push -u origin main
```

## Repo already has a README / initial commit

```bash
cd Homeventory
git remote add source https://github.com/Ezral/blog.git
git fetch source homeventory
git merge source/homeventory --allow-unrelated-histories
git push origin main
```

Then open in **Android Studio** and run.
