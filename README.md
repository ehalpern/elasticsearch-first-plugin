# Elasticsearch first plugin - WIP

This plugin adds a `first` aggregation function.
 
This function selects a field value from the first document encountered in an aggregation group.

Installation
------------

```
bin/plugin --install first-plugin --url TBD
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

