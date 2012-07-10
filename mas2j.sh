#!/bin/sh
#
# this script creates the Ant script (build.xml) 
# to run a Jason project  
#

if [ ! -f $1 ]
then
    echo File $1 not found
    exit
fi

JASONDIR='/home/simon/projects/jason'

echo "java -classpath $JASONDIR/bin/classes:$JASONDIR/lib/jason.jar:$JASONDIR/lib/saci.jar:$JASONDIR/lib/jade.jar:$JASONDIR/lib/c4jason.jar:$JASONDIR/lib/cartago.jar:$JASONDIR/lib/jacamo.jar:$CLASSPATH:. \
  jason.mas2j.parser.mas2j $1 $2"

java -classpath $JASONDIR/bin/classes:$JASONDIR/lib/jason.jar:$JASONDIR/lib/saci.jar:$JASONDIR/lib/jade.jar:$JASONDIR/lib/c4jason.jar:$JASONDIR/lib/cartago.jar:$JASONDIR/lib/jacamo.jar:$CLASSPATH:. \
  jason.mas2j.parser.mas2j $1 $2
