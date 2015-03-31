# Elasticsearch first/last plugin - WIP

This plugin adds `first` and `last` aggregation functions.  

These functions provide a convenient way to select a value from the first or last document in an aggregation group.

Installation
------------

```
bin/plugin --install first-last-plugin --url TBD
```

### First aggregation

The `first` function selects a value from the first document encountered in the aggregation group.

Example:
```
"aggs": {
  "<agg_name>": {
    "field": { 
      "first": { 
        "field" : "<field_name>"
      }
    }
  }
}
```

Result :

```
{
  "aggregations": {
    "<agg_name>": {
      "value": <first value of field_name>
    }
  }
}
```

