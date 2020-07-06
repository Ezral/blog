---
layout: post
title: "Dataquest guided project #3"
category: data
tags: [data, pin]
---

By far this is the most interesting guided project that I've worked on in [Dataquest](https://dataquest.io) course. The goal is to write a basic spam filter algorithm using naive bayes as the foundation which uses the following principle:

{% katexmm %}
If the $ P(Spam) \cdot \prod_{i=1}^{n}P(w_i|Spam) $ is bigger than $ P(Spam^C) \cdot \prod_{i=1}^{n}P(w_i|Spam^C) $ then it's a $\text{Spam message} $.

Otherwise, if the $ P(Spam) \cdot \prod_{i=1}^{n}P(w_i|Spam) $ is smaller than the $ P(Spam^C) \cdot \prod_{i=1}^{n}P(w_i|Spam^C) $ then it's not a $ \text{Spam message} $
{% endkatexmm %}

Apart from learning how to write the basic algorithm, I gained a new knowledge on how this seemingly simple feature, that is integrated to our everyday lives, works. Which is neat.

Click [here](https://nbviewer.jupyter.org/github/Ezral/guided_project/blob/master/Building%20a%20Spam%20Filter%20with%20Naive%20Bayes.ipynb) to view notebook.

---
