#!/bin/bash
. ./classpath.rc

JMX="-Xms256m -Xmx512m"
DEBUG=
java -Dfile.encoding=UTF-8 $JMX $DEBUG wwlib.xlssmartreader.CLI $1 $2
