# This is how you install the current JDK into the linux subsystem on windows

## Step 1
Run the provided `jdkinstall.sh` file (may have to use `sudo`)
This was provided by [this](https://stackoverflow.com/questions/36478741/installing-oracle-jdk-on-windows-subsystem-for-linux) stackoverflow post

## Step 2
Wait

## Step 3
```java

System.out.println("ENJOY!");

```

## Step 4

Make sure to install `maven` into the linux subsystem as well by doing

```
sudo apt-get update
sudo apt-get install maven
```

## Step 5

Navigate to the desired folder with the java code you'd like to compile and run 
```
mvn clean verify
```
