#!/bin/bash

mysql CS144 < drop.sql

mysql CS144 < create.sql

ant
ant run-all

# Duplicate removal
sort Items.dat > sorted_Items.dat
uniq sorted_Items.dat > Items.dat
sort Users.dat > sorted_Users.dat
uniq sorted_Users.dat > Users.dat
sort Bids.dat > sorted_Bids.dat
uniq sorted_Bids.dat > Bids.dat
sort Categories.dat > sorted_Categories.dat
uniq sorted_Categories.dat > Categories.dat

mysql CS144 < load.sql

rm *.dat
rm -r bin
