#!/bin/bash

OUTPUT=import.xml

echo '<?xml version="1.0" encoding="utf-8"?>' >${OUTPUT}
echo '<pma_xml_export version="1.0">' >>${OUTPUT}
echo '<database name="springcrm">' >>${OUTPUT}
if (($# > 0)); then
    for f in $*; do
        cat springcrm-${f}.xml | sed -f fix-names.sed >>${OUTPUT}
    done
else
    cat springcrm-*.xml | sed -f fix-names.sed >>${OUTPUT}
fi
echo '</database>' >>${OUTPUT}
echo '</pma_xml_export>' >>${OUTPUT}
