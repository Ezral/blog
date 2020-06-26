---
layout: post
title: Almace Scaffolding Quick Post
category: news
tags: comp
---

This is a snippet I use to quickly create a post draft for Almace Scaffolding from command line.

{% highlight zsh %}
function blog () {
    local DATE=$(date +"%Y-%m-%d");
    title=$3
    touch /Users/user/github_repo/my-site/_app/_posts/$1/$DATE-$3.md;
    echo "---\nlayout: post\ntitle: ${title//-/ }\ncategory: $1\ntags: $2\n---\n\nwrite here\n\n---" > /Users/user/github_repo/my-site/_app/_posts/$1/$DATE-$3.md;
    echo "\n$DATE-$3.md has been created in ~/github_repo/my-site/_app/_posts/$1\n"
    cd /Users/user/github_repo/my-site/_app/_posts/$1
    atom /Users/user/github_repo/my-site/_app/_posts/$1/$DATE-$3.md;
}
{% endhighlight %}

And since I have to generate all the html content for every time I make any changes, I wrote a function to ```grunt build```, re-adding CNAME file that gets deleted by grunt and ```git add``` followed by ```git status``` to compile all those steps into one line.

{% highlight zsh %}
function buildblog() {
    cd /Users/user/github_repo/my-site/;
    grunt --force build;
    echo "site.com" > temp | mv -f temp /Users/user/github_repo/my-site/docs/CNAME;
    rm temp;
    git add docs/* _app/*;
    git status;
    git commit -m $1;
    git push;
}
{% endhighlight %}

And that's how I get them posts published.

---
