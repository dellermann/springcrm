1a\
{

/^INSERT INTO config/s/^.*VALUES \(0, '([^']+)', '([^']+)'\);?/    {\
      "name": "\1",\
      "version": { "$numberLong": "0" },\
      "value": "\2"\
    },/p
$a\
}

