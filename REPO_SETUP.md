# Sync code into your `homeventory` repo

If you created **Ezral/homeventory** as an empty repo, pull this scaffold from the blog staging branch:

```bash
git clone https://github.com/Ezral/homeventory.git
cd homeventory

# If the repo is empty (no commits yet):
git remote add source https://github.com/Ezral/blog.git
git fetch source homeventory
git checkout -b main
git reset --hard source/homeventory
git push -u origin main
```

If you already have a README or initial commit on `main`:

```bash
git remote add source https://github.com/Ezral/blog.git
git fetch source homeventory
git merge source/homeventory --allow-unrelated-histories
# resolve conflicts if any, then:
git push origin main
```

Then open the project in **Android Studio**.
