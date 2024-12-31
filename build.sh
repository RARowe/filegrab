rm -rf dist
mkdir dist
javac -d dist Main.java
jar cfm filegrab.jar manifest.mf dist/*.class
