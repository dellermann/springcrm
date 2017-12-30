1a\
{

/^INSERT INTO sel_value/{
s/^.*VALUES \(([[:digit:]]+), 0, '([^']+)', ([[:digit:]]+), '([^']+)', ([\.[:digit:]]+)\);?/    {\
      "_id": { "$numberLong": "\1" },\
      "class": "\4",\
      "name": "\2",\
      "orderId": { "$numberLong": "\3" },\
      "taxValue": { "$numberDecimal": "\5" },\
      "version": { "$numberLong": "0" }\
    },/p
s/^.*VALUES \(([[:digit:]]+), 0, '([^']+)', ([[:digit:]]+), '([^']+)'\);?/    {\
      "_id": { "$numberLong": "\1" },\
      "class": "\4",\
      "name": "\2",\
      "orderId": { "$numberLong": "\3" },\
      "version": { "$numberLong": "0" }\
    },/p
}
$a\
}

