#!/bin/bash

RESULT=`mysql information_schema -e "SELECT TABLE_NAME FROM TABLES WHERE TABLE_SCHEMA = 'CS144'" | grep -o Bids`
if [ "$RESULT" == "Bids" ]; then
  mysql CS144 < drop.sql
fi

mysql CS144 < create.sql

ant
ant run-all

# Duplicate removal
sort load.sql > sorted_load.sql
uniq sorted_load.sql > load.sql

mysql CS144 < load.sql

rm sorted_load.sql
rm -r bin
rm load.sql
