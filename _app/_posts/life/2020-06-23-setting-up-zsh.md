---
layout: post
title: Setting up ZSH
category: life
tags: comp
# Scheme for about and comp pages
scheme-text: "#ABB2BF"
scheme-link: "#E06C75"
scheme-hover: "#E45649"
scheme-code: "#61AFEF"
scheme-bg: "#1E2127"
---

MacOSX Catalina 15.5 update changed the Terminal emulator default shell from bash to zsh and I figure it's the right time to get to know zsh. Here's how I setup my zsh after doing a quick search on the web.



 __1. Installing oh-my-zsh__

I installed _[oh-my-zsh](https://github.com/ohmyzsh/ohmyzsh)_ via curl:

{% highlight zsh %}
$ sh -c "$(curl -fsSL https://raw.githubusercontent.com/robbyrussell/oh-my-zsh/master/tools/install.sh)"
{% endhighlight %}

Oh-my-zsh will write its default ```.zshrc``` onto ours and that will makes things easier to customize.

---

 __2. Setting up oh-y-zsh theme__

There are plenty of great looking themes but I like this clean looking [spaceship](https://github.com/denysdovhan/spaceship-prompt) theme. Clone the repo to install:
{% highlight zsh %}
$ git clone https://github.com/denysdovhan/spaceship-prompt.git "$ZSH_CUSTOM/themes/spaceship-prompt"
{% endhighlight %}

Then symlink ```spaceship.zsh-theme``` to the oh-my-zsh custom themes directory:
{% highlight zsh %}
$ ln -s "$ZSH_CUSTOM/themes/spaceship-prompt/spaceship.zsh-theme" "$ZSH_CUSTOM/themes/spaceship.zsh-theme"
{% endhighlight %}

And set ```ZSH_THEME="spaceship"``` in your ```.zshrc```. To set up the prompt, you can see the prompt options [here](https://denysdovhan.com/spaceship-prompt/docs/Options.htmlprompt). The setup I'm using in my ```.zshrc``` is like this:
{% highlight zsh %}
SPACESHIP_USER_SHOW=always
SPACESHIP_USER_COLOR=green
SPACESHIP_USER_SUFFIX=' '
SPACESHIP_HOST_PREFIX="@"
SPACESHIP_HOST_SUFFIX=" "
{% endhighlight %}

---

 __3. Installing font__

I used [FiraCode](https://github.com/tonsky/FiraCode). Installation instructions can be found in FiraCode [wiki](https://github.com/tonsky/FiraCode/wiki/Installing).
Homebrew offers a quick way to do it:

{% highlight zsh %}
$ brew tap homebrew/cask-fonts
$ brew cask install font-fira-code
{% endhighlight %}

Once installed, select to use the font in your Terminal emulator preference.

---

 __4. Setting up oh-my-zsh plugins__

To install zsh-syntax-highlighting in your ```~/.zshrcs```, first clone the repo:
{% highlight zsh %}
$ git clone https://github.com/zsh-users/zsh-syntax-highlighting.git $ZSH_CUSTOM/plugins/zsh-syntax-highlighting
{% endhighlight %}

Then, enable syntax highlighting in the current interactive shell by adding the line below:
{% highlight zsh %}
$ source /usr/local/share/zsh-syntax-highlighting/zsh-syntax-highlighting.zsh
{% endhighlight %}

Note the `source` command must be **at the end** of `~/.zshrc`. Next, clone zsh-syntax-suggestion repo as well to install it:
{% highlight zsh %}
$ git clone https://github.com/zsh-users/zsh-autosuggestions.git $ZSH_CUSTOM/plugins/zsh-autosuggestions
{% endhighlight %}

To enable it we need to add the plugin in your ```.zshrc```:
{% highlight zsh %}
plugins=(git zsh-autosuggestions)
{% endhighlight %}

---

 __5. Organising aliases__

If you have your aliases already setup in bash or other shell, you can just copy them directly to your ```.zshrc```, or place them in a file with ```.zsh``` extension in the ```~/.oh-my-zsh/custom/``` directory. The latter looks more organised in my opinion.

---

__6. Add Conda setup__

If you're like me and you're using Conda for your jupyter notebook, you need to make sure that you put the following script into your ```.zshrc```:
{% highlight zsh %}
 >>> conda initialize >>>
 !! Contents within this block are managed by 'conda init' !!
__conda_setup="$('/Users/user/opt/anaconda3/bin/conda' 'shell.zsh' 'hook' 2> /dev/null)"
if [ $? -eq 0 ]; then
    eval "$__conda_setup"
else
    if [ -f "~/opt/anaconda3/etc/profile.d/conda.sh" ]; then
        . "~/opt/anaconda3/etc/profile.d/conda.sh"
    else
        export PATH="~/opt/anaconda3/bin:$PATH"
    fi
fi
unset __conda_setup
 <<< conda initialize <<<
{% endhighlight %}

To activate or deactivate your conda environment use the command ```conda activate``` or ```conda deactivate``` on your command prompt.

---

 __7. Colors for terminal__

For color, I follow [atom one dark color](https://github.com/nathanbuchar/atom-one-dark-terminal/blob/master/COLORS) theme which I applied manually on my terminal preference. Here are the hex colors for both _One Dark_ and _One Light_ theme:

```
One Dark                       One Light

Black:          1E2127        Black:          000000
Bright Black:   5C6370        Bright Black:   383A42
Red:            E06C75        Red:            E45649
Bright Red:     E06C75        Bright Red:     E45649
Green:          98C379        Green:          50A14F
Bright Green:   98C379        Bright Green:   50A14F
Yellow:         D19A66        Yellow:         986801
Bright Yellow:  D19A66        Bright Yellow:  986801
Blue:           61AFEF        Blue:           4078F2
Light Blue:     61AFEF        Light Blue:     4078F2
Magenta:        C678DD        Magenta:        A626A4
Light Magenta:  C678DD        Light Magenta:  A626A4
Cyan:           56B6C2        Cyan:           0184BC
Light Cyan:     56B6C2        Light Cyan:     0184BC
White:          ABB2BF        White:          A0A1A7
Bright White:   FFFFFF        Bright White:   FFFFFF
Text:           ABB2BF        Text:           383A42
Bold Text:      ABB2BF        Bold Text:      A0A1A7
Selection:      3A3F4B        Selection:      3A3F4B
Cursor:         5C6370        Cursor:         383A42
Background:     1E2127        Background:     F9F9F9
```

The background color I used also follows the One Dark black color which hex is ```1E2127```. You can change it to ```FEFDF9``` or ```FEF4EB``` if you want a peach colored background.

---

 References
   - [Oh-my-zsh](https://github.com/ohmyzsh/ohmyzsh)
   - [Spaceship ZSH prompt](https://denysdovhan.com/spaceship-prompt/)
   - [Zsh-syntax-highlighting](https://github.com/zsh-users/zsh-syntax-highlighting)
   - [Zsh-syntax-autosuggestion](https://github.com/zsh-users/zsh-autosuggestions)
   - [FiraCode font](https://github.com/tonsky/FiraCode)
   - [Atom one dark for terminal](https://github.com/nathanbuchar/atom-one-dark-terminal)

---
