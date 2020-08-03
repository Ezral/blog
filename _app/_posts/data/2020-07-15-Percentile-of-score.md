---
layout: post
title: Percentile of score
category: data
tags: [data]
---

Earlier at work I was trying to figure out how to do in Python what ```ntile()``` in R does.
After a googling session, I discovered [scipy.stat.percentileofscore](https://docs.scipy.org/doc/scipy/reference/generated/scipy.stats.percentileofscore.html) from scipy library that does just exactly what I was looking for.

``` in [1]: ```

{% highlight python %}
from scipy import stats

stats.percentileofscore([1, 2, 3, 4], 3)
{% endhighlight %}

``` out [1]: ```

{% highlight python %}
75.0
{% endhighlight %}

The syntax is ```scipy.stats.percentileofscore(a, score, kind='rank')[source]``` where ```a``` is the array of scores to which the ```score``` is compared to. While ```score``` is the int/float value that will be compared to ```a```.

When trying to create a new column in a dataframe that contains the percentile rank of a column, I used a comprehension list that I will assign to the new column. For example:

``` in [2]: ```

{% highlight python %}
import pandas as pd

df = pd.DataFrame({'Name':["Able", "Baker", "Charlie", "Dog", "Easy", "Fox"],'Weight':[21,16,16,23,34,26]})
{% endhighlight %}

To generate ```df['percentile_rank']``` for the Weight, we can just use this:

``` in [3]: ```

{% highlight python %}
df['percentile_rank'] = [scipy.stats.percentileofscore(df['Weight'], x) for x in df['Weight']]
df['percentile_rank']
{% endhighlight %}

``` out [3]: ```

{% highlight python %}
0     50.000000
1     25.000000
2     25.000000
3     66.666667
4    100.000000
5     83.333333
Name: percentile_rank, dtype: float64
{% endhighlight %}

Done!
