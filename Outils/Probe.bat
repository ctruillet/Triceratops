set path=%path%;C:\Program Files\Java\jdk-12.0.2\bin

java -jar ../lib/ivy-java-1.2.18.jar fr.dgac.ivy.tools.Probe -b 127.255.255.255:2010 "^(.*)"
