---
layout: post
title: Almace Scaffolding Quick Post
category: news
tags: comp
---

I wrote a snippet to quickly open draft for my Almace Scaffolding powered blog from command line.
```
function blog () {
    local DATE=$(date +"%Y-%m-%d");
    title=$3
    touch /Users/user/github_repo/my-site/_app/_posts/$1/$DATE-$3.md;
    echo "---\nlayout: post\ntitle: ${title//-/ }\ncategory: $1\ntags: $2\n---\n\nwrite here\n\n---" > /Users/user/github_repo/my-site/_app/_posts/$1/$DATE-$3.md;
    echo "\n$DATE-$3.md has been created in ~/github_repo/my-site/_app/_posts/$1\n"
    cd /Users/user/github_repo/my-site/_app/_posts/$1
    atom /Users/user/github_repo/my-site/_app/_posts/$1/$DATE-$3.md;
}
```
And since I have to generate all the html content for every time I make any changes, I wrote a function to ```grunt build```, re-adding CNAME file that gets deleted by grunt and ```git add``` followed by ```git status``` to compile all those steps into one line.
```
function buildblog() {
    cd /Users/user/github_repo/my-site/
    grunt build;
    echo "site.com" >> /Users/user/github_repo/my-site/docs/CNAME;
    git add docs/* _app/*;
    git status;
}
```
After making sure that everything is ok, I can just  ```git commit``` and ```git push```.

---
