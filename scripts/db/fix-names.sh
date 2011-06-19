#!/bin/bash

table=$1
if [[ -n "${table}" ]]; then
    cat springcrm-${table}.xml | sed -f fix-names.sed >import.xml
else
    cat springcrm-*.xml | sed -f fix-names.sed >import.xml
fi
