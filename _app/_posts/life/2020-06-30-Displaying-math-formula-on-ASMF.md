---
layout: post
title: Displaying math formula on ASMF
category: life
tags: comp
---

This is a short post on how to set up KaTex plugin to render TeX on Almace Scaffolding, mostly taken from [Jekyll-Katex](https://github.com/linjer/jekyll-katex) github page.
1. Add plugin to ```_config.yml```:

{% highlight ruby %}
plugins:
  - jekyll-katex
{% endhighlight %}

2. Add ```jekyll-katex``` to Gemfile plugin block:

{% highlight ruby %}
group :jekyll_plugins do
  gem 'jekyll-katex'
end
{% endhighlight %}

Once this is done, execute ```bundle install```
3. Place the following script into your
```/my-site/_includes/themes/curtana/includes/top.html``` :

{% highlight ruby %}
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/katex@0.11.1/dist/katex.min.css" integrity="sha384-zB1R0rpPzHqg7Kpt0Aljp8JPLqbXI3bhnPWROx27a9N0Ll6ZP/+DiW/UqRcLbRjq" crossorigin="anonymous">
{% endhighlight %}

Once these 3 steps are done you are ready to go. For more details on usage check out [Jekyll-Katex](https://github.com/linjer/jekyll-katex) page.
