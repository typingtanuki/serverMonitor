# serverMonitor

## Goal

Easy monitor the state of linux and windows servers, as well as the tools running on them.

## How to

1. Make the jar by running `gradlew jar`
1. Deploy the tool on each server
1. Copy `conf/sample.json` to `conf/monitor.json`
1. Edit the configuration to match your needs
1. Start the monitor with `java -jar serverMonitor.jar`

## What can be monitored ?

* Local to the server
  * System CPU usage
  * System memory usage
  * Disk usage on all root partitions
  * If specific processes are currently running on the server or not
  * Available system updates (apt-get and apt based only) 
* Network
  * Pingability of other servers (ICMP based)
  * Handshake with other servers running this tool (custom HTTP handshake)
  * If the monitor is not running on another server
  * Clock issues between servers
  
## REST Apis

### Seeing current status

Doing a `GET` on `/status` will return the current status of all monitors

### Seeing current configuration

Doing a `GET` on `/config` will return the current configuration

### Updating current configuration

Doing a `POST` on `/config`, with the new settings as a payload will update settings live.
The new settings will be kept in memory, by adding the parameter `?persist=true`, the new settings will also be persisted to disk.

NOTE: The REST port is the only setting which can not be updated through this method at this time.
  
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

## Planned features

* ~~Yum based system update monitor~~
* ~~A way too fetch current status through REST~~
* ~~A way to see current config through REST~~
* ~~A way to update current config through REST~~
* Windows update monitor (if possible without being admin)
