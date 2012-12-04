#! /bin/bash

ant clean && ant build && ant deploy && $CATALINA_HOME/bin/shutdown.sh && $CATALINA_HOME/bin/startup.sh
