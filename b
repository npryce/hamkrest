#!/bin/sh
set -e
version=`git describe --always --tags --match '*.*.*.*' --dirty="_patched"`
`dirname $0`/gradlew -P-version=${version} "$@"
