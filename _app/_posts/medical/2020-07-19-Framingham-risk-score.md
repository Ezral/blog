---
layout: post
title: Framingham risk score
category: medical
tags: [data, med, pin]
---

I wrote a function in Python that calculates [Framingham risk score for hard coronary heart disease](https://www.framinghamheartstudy.org/fhs-risk-functions/hard-coronary-heart-disease-10-year-risk/) based on the equation provided in [mdcalc's online calculator](https://www.mdcalc.com/framingham-risk-score-hard-coronary-heart-disease#evidence).

[Fonnesbeck](https://github.com/fonnesbeck)'s repository on [framingham_risk](https://github.com/fonnesbeck/framingham_risk)  was huge help!

{% highlight python linenos %}
import numpy as np

def calc_frs(X,B,N,cons):
    """Calculate in percentage, the probability of
    having hard coronary vascular disease in 10 years.

    Args:
        X (np.array): Numpy array consisting number representation
        of age, sex,smoking, tot_cholesterol, hdl_cholesterol,
        sys_bp, bp_treatment variables.
        B (np.array): Numpy array consisting coefficients for
        each variables.
        N (float): Survival rate
        cons (int): Constant

    Returns:
        Framingham score: probability of having hard coronary
        vascular disease in 10 years.
    """
    result = 1 - N**np.exp(np.dot(X,B) - cons)
    return result

def frs(x):
    """Calculate in percentage, the probability of
    having hard coronary vascular disease in 10 years.

    Args:
        x (list): List of integers with len of 7 as an input where
        x[0] = age, x[1] = sex, x[2] = smoker, x[3] = tot_cholesterol,
        x[4] = hdl_cholesterol, x[5] = sys_bp, x[6] = bp_treatment.

    Returns:
        Framingham score: probability of having hard coronary
        vascular disease in 10 years.
    """

    if x[0] >= 30:
        # [age,tot_cholesterol,hdl_cholesterol,sys_bp,bp_treatment,smoker,age * tot_cholesterol,age*smoker,age**2]
        age,tot_cholesterol,hdl_cholesterol,sys_bp = np.log(x[0]),np.log(x[3]),np.log(x[4]),np.log(x[5])
        PARAMS_M = np.array([age,
                            tot_cholesterol,
                            hdl_cholesterol,
                            sys_bp,
                            x[6],
                            x[2],
                            age*tot_cholesterol,
                            age*x[2],
                            age*age
                            ])

        PARAMS_F = np.array([age,
                            tot_cholesterol,
                            hdl_cholesterol,
                            sys_bp,
                            x[6],
                            x[2],
                            age*tot_cholesterol,
                            age*x[2]
                            ])
        # Coefficients
        MALE_B = np.array([52.00961,
                            20.014077,
                            -0.905964,
                            1.305784,
                            0.241549,
                            12.096316,
                            -4.605038,
                            -2.84367,
                            -2.93323
                            ])

        FEMALE_B = np.array([31.764001,
                            22.465206,
                            -1.187731,
                            2.552905,
                            0.420251,
                            13.07543,
                            -5.060998,
                            -2.996945
                            ])
        # n
        MALE_N = 0.9402
        FEMALE_N = 0.98767
        # constant
        MALE_CONS = 172.300168
        FEMALE_CONS = 146.5933061

        # If sex == Male
        if x[1] == 1:
            if x[0] > 70:
                x[0] = 70
            result = round(calc_frs(PARAMS_M, MALE_B, MALE_N, MALE_CONS)*100,1)
            return result

        # If sex == Female
        if x[1] == 0:
            if x[0] > 78:
                x[0] = 78
            result = round(calc_frs(PARAMS_F, FEMALE_B, FEMALE_N, FEMALE_CONS)*100,1)
            return result
    else:
        return np.NaN
{% endhighlight %}


---
