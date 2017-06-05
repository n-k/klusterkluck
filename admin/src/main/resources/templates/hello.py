#!/usr/bin/python

import fileinput

for line in fileinput.input():
    print "hello from python: ", line
