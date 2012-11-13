Run this from /p4. It will build the project, restart Tomcat, and deploy it. It's available at: localhost:8080/eBay/

ant clean; ant build; cp build/eBay.war $CATALINA_HOME/webapps; rm -rf $CATALINA_HOME/webapps/eBay/; $CATALINA_HOME/bin/shutdown.sh; $CATALINA_HOME/bin/startup.sh
