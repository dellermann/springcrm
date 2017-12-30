1a\
{

/^INSERT INTO seq_number/s/^.*VALUES \(([[:digit:]]+), 0, '([^']+)', ([[:digit:]]+), '([^']*)', ([[:digit:]]+), '([^']*)', ([[:digit:]]+)\);?/    {\
      "_id": { "$numberLong": "\1" },\
      "controllerName": "\2",\
      "endValue": \3,\
      "orderId": { "$numberLong": "\7" },\
      "prefix": "\4",\
      "startValue": \5,\
      "suffix": "\6",\
      "version": { "$numberLong": "0" }\
    },/p
$a\
}

