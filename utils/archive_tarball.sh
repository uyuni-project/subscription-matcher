#!/bin/sh

if [ "$1" = "-h" -o "$1" = "--help" ]; then
    echo "Usage: $0 [<git tag>]"
    echo "       When no git tag is given, HEAD is used."
    exit 0
fi

if [ -n "$1" ]; then
    TAG=$1
else
    TAG="HEAD"
fi

pushd `git rev-parse --show-toplevel`
git archive --format=tar.gz --prefix=subscription-matcher/ $TAG -o subscription-matcher.tar.gz
if [ $? -eq 0 ]; then
    echo "subscription-matcher.tar.gz created"
fi
popd

