@echo off
echo SpringCRM wird gestartet. Der Vorgang kann ca. 30 Sekunden dauern...
echo Rufen Sie danach http://localhost:8080 in Ihrem Browser auf.
javaw -Xms512m -Xmx512m -XX:MaxPermSize=256M -XX:+CMSClassUnloadingEnabled -XX:+CMSPermGenSweepingEnabled -splash:springcrm-splash.png -jar standalone-@VERSION@.jar
