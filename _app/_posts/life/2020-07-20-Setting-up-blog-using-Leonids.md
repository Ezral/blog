---
layout: post
title: Setting up blog using Duo
category: life
tags: comp
# Scheme for comp pages
scheme-text: "#ABB2BF"
scheme-link: "#E06C75"
scheme-hover: "#E45649"
scheme-code: "#61AFEF"
scheme-bg: "#1E2127"
---

Setting up blogs using Jekyll is great!
This is how you can set up a jekyll blog using [duo](https://github.com/chibicode/duo) template created by [chibicode](https://github.com/chibicode)

I've forked the repo and made some minor changes. If you want to use the one with that I've modified, clone this repo:

{% highlight zsh %}
$ git clone git@github.com:Ezral/duo.git
{% end highlight %}

And then, to start everything, simply:

{% highlight zsh %}
$ bundle install
$ bundle exec jekyll serve
{% end highlight %}

Once you've made all the necessary adjustment locally, change your git repo's ```origin``` before pushing:

{% highlight zsh %}
$ git remote set-url origin https://github.com/USERNAME/REPOSITORY.git
{% end highlight %}

After that, commit and push the changes that you've made and let github render your blog for you.

Happy blogging!

---
