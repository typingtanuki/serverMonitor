# serverMonitor

## Goal

Easy monitor the state of linux and windows servers, as well as the tools running on them.

## How to

1. Make the jar by running `gradlew jar`
1. Deploy the tool on each server
1. Copy conf/sample.conf to conf/monitor.conf
1. Edit the configuration to match your needs
1. Start the monitor with `java -jar serverMonitor.jar`

## What can be monitored ?

* Local to the server
  * System CPU usage
  * System memory usage
  * Disk usage on all root partitions
  * If specific processes are currently running on the server or not 
* Network
  * Pingability of other servers (ICMP based)
  * Handshake with other servers running this tool (custom HTTP handshake)
  * If the monitor is not running on another server
  * Clock issues between servers
  
## How can it report ?

* Printing messages to the console
* Printing messages to a log file
* Sending notifications to a MessageCard compatible API (like Microsoft Teams)

## Dependencies

* Oshi is used for system monitoring
* Jackson for JSON parsing and writing
* Jersey+jetty for the handshake server
* RestEasy for the handshake client
* Logback for logging
