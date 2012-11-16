Run this from /p4. It will build the project, restart Tomcat, and deploy it. It's available at: localhost:8080/eBay/

ant clean && ant build && ant deploy && $CATALINA_HOME/bin/shutdown.sh && $CATALINA_HOME/bin/startup.sh
