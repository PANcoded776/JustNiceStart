# JustNiceStart
Is a simple minecraft plugin for 1.21.4 (should work with 1.21.5 ig.).

-------------------------------------------------------------

### Features
- /start [seconds] (starts a countdown for X seconds)
- /cancel (stops current countdown)
- /jsnreload (reloads the current config.yml)
- in the config.yml you can turn on custom commands when the countdown has finished. The boolean is called `runCustomCommandOnFinish`

-------------------------------------------------------------

### Standart config.yml
``` 
#        __             __    _  __   _              ____  __               __
#    __ / / __ __  ___ / /_  / |/ /  (_) ____ ___   / __/ / /_ ___ _  ____ / /_
#   / // / / // / (_-</ __/ /    /  / / / __// -_) _\ \  / __// _ `/ / __// __/
#   \___/  \_,_/ /___/\__/ /_/|_/  /_/  \__/ \__/ /___/  \__/ \_,_/ /_/   \__/
#

version: 3.2
description: Ein Plugin, das einen Timer mit Effekten und Befehlen startet.

#Setze auf true wenn die commands ausgeführt werden sollen.
#Jeder command auf extra Zeile:
# - 1
# - 2
# - ...
runCustomCommandOnFinish: false

commands:
  - title @a title {"text":"Los geht’s!","color":"gold","bold":true}
 ```

-------------------------------------------------------------

*Notice: For now the description and commands inside the plugin are on german sry. I´m going to update this to english.*


If you have any issues pls report it to me :)
