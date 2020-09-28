#!/bin/bash
#
# Helper script for running Matcher benchmarker.
#

mvn -f ../pom.xml compile exec:java -Dexec.classpathScope="test" -Dexec.cleanupDaemonThreads="false" -Dexec.mainClass="com.suse.matcher.Benchmarker" -Dexec.args="$*"

