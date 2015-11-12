#!/bin/sh

if [ $# -eq 0 ]
  then
    echo "Usage: $0 <VERSION_NUMBER>"
    exit 1
fi

mvn versions:set -DnewVersion=$1 scm:add -Dincludes=pom.xml scm:checkin -Dmessage="Version bumped to v$1" -DgenerateBackupPoms=false
mvn scm:tag -Dtag=v$1 -DpushChanges=true

