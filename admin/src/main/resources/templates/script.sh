#!/bin/sh
if [ "$(($RANDOM%2))" -eq "0" ]; then
    ${WORK_DIR}/hello.py
else
    ${WORK_DIR}/hello.js
fi
