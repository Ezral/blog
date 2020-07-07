---
layout: post
title: "Building a Spam Filter with Naive Bayes"
category: data
tags: [data, pin]
scheme-text: "#ABB2BF"
scheme-link: "#E06C75"
scheme-hover: "#E45649"
scheme-code: "#61AFEF"
scheme-bg: "#1E2127"
---

###### This is a [Dataquest guided project](https://app.dataquest.io/m/433/guided-project%3A-building-a-spam-filter-with-naive-bayes) originally written on [this jupyter notebook file](https://nbviewer.jupyter.org/github/Ezral/guided_project/blob/master/Building%20a%20Spam%20Filter%20with%20Naive%20Bayes.ipynb)

## Background

Have you ever felt disturbed by spam messages? I'm sure everybody has. Even those who sent the spam messages have. Fortunately there's this thing called spam filter that helps us avoid the hassle of repeatedly cleaning our inbox. The goal of this exercise is to create a basic messaging spam filter using Naive Bayes theorem. Isn't this exciting? It sure is.

We'll achieve this goal using a dataset put together by Tiago A. Almeida and José María Gómez Hidalgo that has 5,572 SMS messages made available in [UCI Machine Learning Repository](https://archive.ics.uci.edu/ml/datasets/sms+spam+collection).  The messages have been previously classified into spam/ham (not spam) category and can be downloaded from [here](https://dq-content.s3.amazonaws.com/433/SMSSpamCollection). This allows us to "train" the algorithm to classify the messages and later on label whether a certain message is a spam/ham using the following principal:

{% katexmm %}
If the $ P(Spam) \cdot \prod_{i=1}^{n}P(w_i|Spam) $ is bigger than $ P(Spam^C) \cdot \prod_{i=1}^{n}P(w_i|Spam^C) $ then it's a $\text{Spam message} $.

Otherwise, if the $ P(Spam) \cdot \prod_{i=1}^{n}P(w_i|Spam) $ is smaller than the $ P(Spam^C) \cdot \prod_{i=1}^{n}P(w_i|Spam^C) $ then it's not a $ \text{Spam message} $
{% endkatexmm %}
Now that we've covered the basics, let's start working~

## Content
> - [Background](#background)
> - [Loading the dataset](#separating-the-dataset)
> - [Separating the dataset](#separating-the-dataset)
> - [Cleaning the dataset](#cleaning-the-dataset)
> - [Creating word count dataset](#creating-word-count-dataset)
> - [Calculating constants](#calculating-constants)
> - [Calculating parameters](#calculating-parameters)
> - [Creating spam filter function](#creating-spam-filter-function)
> - [Measuring filter accuracy](#measuring-filter-accuracy)
> - [Conclusion](#conclusion)

---

## Loading the dataset

First we will import the packages that we'll be using and load the dataset. We'll also take a quick look at the dataset to familiarize ourselves with the data.

``` in [1]: ```
```python
# Importing packages for data management
import pandas as pd    # Importing pandas
import numpy as np     # Importing numpy
import datetime as dt  # Importing datetime
import re              # Importing regular expression
import warnings        # To suppress warning alert
warnings.filterwarnings('ignore')
#Change setting to avoid dataframe from truncating
pd.options.display.max_rows = 500
pd.options.display.width = 500
pd.options.display.max_colwidth = 500
pd.options.display.max_columns = 500
# Displaying all output from code cell. Default value = 'last_expr'.
from IPython.core.interactiveshell import InteractiveShell
InteractiveShell.ast_node_interactivity = "all"
```

``` in [2]: ```
```python
# loading the dataset
sms = pd.read_csv("SMSSpamCollection", sep='\t', header=None, names=['Label','spam_sms'])
```

``` in [3]: ```
```python
# Shape and proportion of the dataset
print("\nSMSSpamCollection dataset has {} rows and {} columns".format(sms.shape[0],sms.shape[1]))
sms['Label'].value_counts(normalize=True)*100
```
``` out [3]: ```

```
    SMSSpamCollection dataset has 5572 rows and 2 columns

    ham     86.593683
    spam    13.406317
    Name: Label, dtype: float64
```


[Back to top](#background)



---

## Separating the dataset

To create the spam filter we will need to split the dataset into 2, one for training purpose and the other one for the spam filter testing. The dataset will be split into 80:20 proportion, the larger one being the training dataset that will "teach" our algorithm to recognize spam messages.

Here's where the pre-labelled message will come into play. Basically we'll have the algorithm calculate and store probability for every single words in the dataset, letting it know which words are more probable to be used in spam and which words are not.

Since we have this 5,572 rows of messages, we'll split them into two sets, 80% for training and 20% for testing.

``` in [4]: ```
```python
# Randomizing the dataset
random_set = sms.sample(frac=1,random_state=1)

# Calculating the index for split
random_index = round(len(random_set)*0.8)

# Splitting the dataset
training = random_set[:random_index].reset_index(drop=True)
test = random_set[random_index:].reset_index(drop=True)

# Checking shape and label proportion for training dataset
print("\nTraining dataset has {} rows.".format(training.shape[0]))
training['Label'].value_counts(normalize=True)*100

# Checking shape and label proportion for test dataset
print("Test dataset has {} rows.".format(test.shape[0]))
test['Label'].value_counts(normalize=True)*100
```
``` out [4]: ```
```
    Training dataset has 4458 rows.
    ham     86.54105
    spam    13.45895
    Name: Label, dtype: float64

    Test dataset has 1114 rows.
    ham     86.804309
    spam    13.195691
    Name: Label, dtype: float64
```


We've split them nicely and it looks like the proportion between the two is similar, which is good.

---

## Cleaning the dataset

Up next, tidying the dataset. We'll clean the words by removing any punctuation and have all the words in lower case.

``` in [5]: ```
```python
# Changing to lower case and replacing all punctuation
def clean_punctuation(x):
    result = re.sub('\W', ' ', x)
    result = result.lower()
    return result

training['spam_sms'] = training['spam_sms'].apply(clean_punctuation)
```

---

## Creating word count dataset

To count the words, we need to split the words in the messages. By performing a split we'd be creating series containing lists that we can iterate to obtain the vocabulary frequency.

``` in [5]: ```
```python
# Transforming messages in SMS column into list
training['spam_sms'] = training['spam_sms'].str.split()
```

``` in [6]: ```
```python
# Creating vocabulary from the SMS column
vocabulary = []

for row in training['spam_sms']:
    for n in row:
        if n not in vocabulary:
            vocabulary.append(n)
        else:
            continue
```

The for loop function in the above should prevent any duplicate in the vocabulary list.
Quick check to confirm that there's no duplicate:

``` in [7]: ```
```python
len(vocabulary) == len(set(vocabulary))
len(vocabulary)
```
``` out [7]: ```
```
    True
    7783
```

The check returns ```True```, meaning there's no duplicated words and the vocabulary has 7,873 unique words in it.

Now we can move on to the next step, which is counting the number of times a certain word is used and storing them into a dataframe. This dataframe will later be concatenated with training dataframe to complete the process.

``` in [8]: ```
```python
# Creating word count per sms dictionary
word_counts_per_sms = {unique_word: [0] * len(training['spam_sms']) for unique_word in vocabulary}

# For loop function to fill the dictionary
for index, sms in enumerate(training['spam_sms']):
    for word in sms:
        word_counts_per_sms[word][index] += 1
```

``` in [9]: ```
```python
# Creating word count dataframe and concatenating it with training dataframe
word_count_data = pd.DataFrame(word_counts_per_sms)
training_set = pd.concat([training,word_count_data],axis=1)
```

By having this we can obtain the frequency of a word on a certain label and subsequently calculate the probability of that particular word given spam or ham.

[Back to top](#background)

---

## Calculating constants
{% katexmm %}
Calculating $P(Spam)$, $P(Ham)$, $N_{Spam}$, $N_{Ham}$ and $N_{Vocabulary}$
{% endkatexmm %}

``` in [10]: ```
```python
# Calculating the constants
p_ham=training_set['Label'].value_counts(normalize=True)[0]
p_spam=training_set['Label'].value_counts(normalize=True)[1]

# Function to count n of words
def word_counter(x):
    '''Function takes in a series
    of strings that will append
    it to a list and return the
    len to complete the word count
    '''
    result=[]
    for row in x:
        for n in row:
            word = str(n)
            result.append(word)
    return len(result)

# Number of words in vocabulary, ham and spam
n_vocab = len(vocabulary)
n_ham = word_counter(training_set.query("Label=='ham'")['spam_sms'])
n_spam = word_counter(training_set.query("Label=='spam'")['spam_sms'])

# Laplace smoothing
alpha = 1
```

[Back to top](#background)

---

## Calculating parameters
{% katexmm %}
$ P(w_{i} | Spam)$ and $ P(w_{i} | Ham)$ that will be used to classify the incoming message are called _parameters_.
They will be calculated using the following equations:
{% endkatexmm %}

{% katex display %}
P(w_{i} | Spam) = \frac{N_{w_{i}|Spam}+ \alpha}{N_{Spam} + N_{Vocabulary}}
{% endkatex %}

{% katex display %}
P(w_{i} | Ham) = \frac{N_{w_{i}|Ham}+ \alpha}{N_{Ham} + N_{Vocabulary}}
{% endkatex %}

``` in [11]: ```
```python
# Separating spam/ham messages
spam_msg = training_set[training_set['Label']=='spam']
ham_msg = training_set[training_set['Label']=='ham']

# Creating 2 dictionaries for parameters
parameters_spam = {word: 0 for word in vocabulary}
parameters_ham = {word: 0 for word in vocabulary}
```

``` in [12]: ```
```python
# Calculating parameters:

for word in vocabulary:
    n_word_given_spam = spam_msg[word].sum()   
    p_word_given_spam = (n_word_given_spam + alpha) / (n_spam + alpha*n_vocab)
    parameters_spam[word] = p_word_given_spam

    n_word_given_ham = ham_msg[word].sum()
    p_word_given_ham = (n_word_given_ham + alpha) / (n_ham + alpha*n_vocab)
    parameters_ham[word] = p_word_given_ham
```

[Back to top](#background)

---

## Creating spam filter function
{% katexmm %}
The spam filter works by comparing the $ P(Spam | Message)$ to $ P(Ham | Message)$ and assigning spam/ham label depending on which one has the larger proportion. To do this we'll use the following function:
{% endkatexmm %}

``` in [13]: ```
```python
# Spam filter function
import re

def prob(message, dictionary, init_value):
    p = init_value
    for n in message:
        if n in dictionary:
            p *= dictionary[n]
        else:
            continue
    return p

def classify(message):

    message = re.sub('\W', ' ', message)
    message = message.lower()
    message = message.split()

    p_spam_given_message = prob(message, parameters_spam, p_spam)
    p_ham_given_message = prob(message, parameters_ham, p_ham)

    print('P(Spam|message):', p_spam_given_message)
    print('P(Ham|message):', p_ham_given_message)

    if p_ham_given_message > p_spam_given_message:
        print('Label: Ham')
    elif p_ham_given_message < p_spam_given_message:
        print('Label: Spam')
    else:
        print('Equal proabilities, have a human classify this!')
```

Testing the ```classify``` function using a message that's clearly a spam:

``` in [14]: ```
```python
classify('WINNER!! This is the secret code to unlock the money: C3421.')
```
``` out [14]: ```
```
    P(Spam|message): 1.3481290211300841e-25
    P(Ham|message): 1.9368049028589875e-27
    Label: Spam
```

and using a message that's a ham:

``` in [15]: ```
```python
classify("Sounds good, Tom, then see u there")
```
``` out [15]: ```
```
    P(Spam|message): 2.4372375665888117e-25
    P(Ham|message): 3.687530435009238e-21
    Label: Ham
```

This shows that the function is working properly and we can apply it to the test set that we haven't touched yet. We'll do that while measuring the filter accuracy.

[Back to top](#background)

---

## Measuring filter accuracy

To do this, we will compare the set that was not included in the training set, which is the test set, and compare the label given by the filter with the label that comes with the dataset, which is done by human.
We'll adjust the ```classify``` function in the above to return a value instead of printing them so we can apply it to create a new column.

``` in [16]: ```
```python
# Function to test the filter function
def classify_test_set(message):
    message = re.sub('[\W]',' ', message)
    message = message.lower()
    message = re.split("[\s]",message)
    p_spam_given_message = prob(message, parameters_spam, p_spam)
    p_ham_given_message = prob(message, parameters_ham, p_ham)

    if p_ham_given_message > p_spam_given_message:
        return 'ham'
    elif p_ham_given_message < p_spam_given_message:
        return 'spam'
    else:
        return 'Equal proabilities, have a human classify this!'
```

``` in [17]: ```
```python
# Applying the function
test['predicted'] = test['spam_sms'].apply(classify_test_set)
```

{% katex display %}
\small \text{Accuracy} = \frac{\text{number of correctly classified messages}}{\text{total number of classifed messages}}
{% endkatex %}
The applied function works as expected. Next we'll do the calculation in the above on the dataset by using this for loop function below that will print the numbers:

``` in [18]: ```
```python
# Measuring accuracy value
correct = 0
total = len(test['Label'])
for val_1, val_2 in zip(test['Label'],test['predicted']):
    if val_1 == val_2:
        correct += 1
    else:
        continue
print("Accuracy value of the spam filter is {}%.".format(correct/total * 100))
print("Number of correct label: {}".format(correct))
print("Number of incorrect label: {}".format(total-correct))
```
``` out [18]: ```
```
    Accuracy value of the spam filter is 98.74326750448833%.
    Number of correct label: 1100
    Number of incorrect label: 14
```

---

## Conclusion

In conclusion, it's a really worthwhile project that helped me understand the application of Bayes theorem and see how a simple spam filter that uses statistics works. It would be interesting to see how we might be able to increase the accuracy further by modifying the algorithm.

[Back to top](#background)

---
