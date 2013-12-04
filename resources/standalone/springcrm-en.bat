@echo off
echo SpringCRM is being started. This will take approx. 30 seconds...
echo After that, call http://localhost:8080 in your browser.
javaw -Xms512m -Xmx512m -XX:MaxPermSize=256M -XX:+CMSClassUnloadingEnabled -XX:+CMSPermGenSweepingEnabled -splash:springcrm-splash.png -jar standalone-@VERSION@.jar
