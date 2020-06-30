---
layout: post
title: "Dataquest Guided Project #2"
category: data
tags: data
---
This is another guided project from [Dataquest](dataquest.io) that I recently completed.
It's not really data heavy like the usual ones as it focuses more on probability topic.
I particularly enjoy this one where I learned how to write math formula using TeX in Jupyter Notebook.

For example, this line here:

{% highlight TeX %}
\frac{n!}{k!(n-k)!} = \binom{n}{k}
{% endhighlight %}

will give this output:

{% katex display %}
\frac{n!}{k!(n-k)!} = \binom{n}{k}
{% endkatex %}

Another different thing that I tried is using anchors on the notebook page using the header which turned out to be real simple.
For example,

{% highlight md %}
[Click here](#Anchor)
{% endhighlight %}

Will bring you to your ``` # Anchor``` header. Note that you need to remove the whitespace between # and your header. If you have whitespace on your header you need to replace them with ```"-"```. For example ``` # Your header``` should be written like this

{% highlight md %}
[Click here](#Your-header)
{% endhighlight %}

Click [here](https://nbviewer.jupyter.org/github/Ezral/guided_project/blob/master/Mobile%20App%20for%20Lottery%20Addiction.ipynb) to view the notebook.

---
