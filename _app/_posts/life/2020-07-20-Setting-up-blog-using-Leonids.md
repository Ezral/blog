---
layout: post
title: Setting up blog using Leonids
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
These are the steps that I used to setup a Jekyll blog using a theme called [Leonids](https://github.com/renyuanz/leonids/) that's pretty sleek:

#### Clone the repo
{% highlight zsh %}
$ git clone https://github.com/renyuanz/leonids
{% endhighlight %}

#### Go to the folder and bundle install
{% highlight zsh %}
$ cd leonids
$ bundle install
{% endhighlight %}

#### Run it locally
{% highlight zsh %}
$ bundle exec jekyll serve
{% endhighlight %}

#### View it on your browser at this address
{% highlight zsh %}
http://127.0.0.1:4000
{% endhighlight %}

Once it's running, you can begin to edit the content.

Happy blogging!

---
