---
layout: post
title: "Parsing ICD-10-PCS XML"
category: data
tags: [data, pin]
heading-bg: img/snippet.jpg
heading-bg-local: true
# heading-bg-size: "100px 100px"
# heading-bg-position: "center bottom"
# heading-bg-repeat: "repeat"
heading-bg-text: "#1E2127"
---

###### This post is originally written on [this jupyter notebook file](https://nbviewer.jupyter.org/github/Ezral/officedoc/blob/master/Parsing%20ICD-10-PCS%20table%20XML.ipynb)

## Background

The goal of this project is to parse ICD-10-PCS XML file into a CSV file that contains 2 columns, the 7 character code and the title (or medical name) with "@" separating each level title. For this project I'll be using ICD-10-PCS 2020 which XML file [can be downloaded from CMS.gov](https://www.cms.gov/Medicare/Coding/ICD10/Downloads/2020-ICD-10-PCS-Code-Tables.zip).

I'll be using etree package in Python to work this project out. And as usual, I'll start with the table of content:

## Content
- [Importing the packages](#importing-the-packages)
- [Reading the XML file](#reading-the-XML-file)
- [Parsing the nodes](#parsing-the-nodes)
- [Conclusion](#conclusion)

---

## Importing packages


{% highlight python %}
# Importing packages
import xml.etree.ElementTree as ET
import pandas as pd
#Change setting to avoid dataframe from truncating
pd.options.display.max_rows = 500
pd.options.display.width = 500
pd.options.display.max_colwidth = 500
pd.options.display.max_columns = 500
{% endhighlight %}


---

## Reading the XML file

{% highlight python %}
# Reading the XML file into a tree
tree = ET.parse("/icd10pcs_tables_2020.xml")
root = tree.getroot()
{% endhighlight %}

---

## Parsing the nodes

After reading the XML file, there's a consistent pattern for each pcsTable where the first 3 characters of the code will be unique in that table, but the remaining 4 will differ. Depending on the table, there might be more than 1 set of remaining 4 code characters that each will have one or more label under them. The structure is somewhat like this:

{% highlight bash %}
.
├── pcsTable
│   ├ axis
│   │  └── label (position 1)
│   ├ axis
│   │  └── label (position 2)
│   ├ axis
│   │  └── label (position 3)
│   └ pcsRow
│       ├ axis
│       │  └── label (position 4)
│       ├ axis
│       │  └── label (position 5)
│       ├ axis
│       │  └── label (position 6)
│       └ axis
│          └── label (position 7)
│
└── pcsTable

{% endhighlight %}   

To parse this correctly, I created two empty lists to store the parsing result and use a for loop function where:
1. I define the first 3 character codes and titles, concatenate and store them in a separate variable
2. Nest a for loop function that goes through all the rows and define 4 separate variables for each code position (from 4 to 7)
3. Iterate through all the labels for each code positions and append the end result to the lists outside of the loop


{% highlight python %}
# Parsing the nodes
code_result,label_result = [],[]
pcstable = root.findall("./pcsTable")
for table in pcstable:
    label=table.findall("./axis")           
    n1 = label[0].findall("./label")
    n2 = label[1].findall("./label")
    n3 = label[2].findall("./label")
    c123 = n1[0].attrib['code'] + n2[0].attrib['code'] + n3[0].attrib['code']
    l123 = n1[0].text + " @ " + n2[0].text + " @ " + n3[0].text     
    rows=table.findall("./pcsRow")
    for row in rows:                        
        axes = row.findall("./axis")
        pos4 = axes[0].findall("./label")
        pos5 = axes[1].findall("./label")
        pos6 = axes[2].findall("./label")
        pos7 = axes[3].findall("./label")
        for n4 in pos4:
            l124 = l123 + " @ " + n4.text
            c124 = c123 + n4.attrib['code']
            for n5 in pos5:
                l125 = l124 + " @ " + n5.text
                c125 = c124 + n5.attrib['code']
                for n6 in pos6:
                    l126 = l125 + " @ " + n6.text
                    c126 = c125 + n6.attrib['code']
                    for n7 in pos7:
                        l127 = l126  + " @ " + n7.text
                        c127 = c126 + n7.attrib['code']
                        label_result.append(l127)
                        code_result.append(c127)
{% endhighlight %}

[Back to top](#background)

## Conclusion

It took me quite a while to solve this. A colleague from work (to whom I'm very grateful!) gave a thorough explanation which provided a great guidance that allowed me to find the right solution. In the process, I learned about XML file structure, strategies that can be used to parse XML and... tqdm package that allows you to see a progress bar for your long iteration.

I didn't use tqdm progress bar in this notebook because the functions are quite fast to run. I used it running a one hour long for loop function when [I was trying to brute force all the possible combination](https://nbviewer.jupyter.org/github/Ezral/officedoc/blob/master/Wrongly%20parsing%20ICD-10-PCS%20XML.ipynb) out of the unique code characters from the ICD-10-PCS XML file which has 880 tables and returned the output of 368,462 codes! Definitely the wrong way to do it, but ended up learning a lot from that process as well.

Overall, I'm happy with the end result and all the take aways that I got from working this project. On to the next!

[Back to top](#background)

---
