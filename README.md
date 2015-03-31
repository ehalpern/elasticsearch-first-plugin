# Elasticsearch first/last plugin 

## (WORK IN PROGRESS)

This plugin adds `first` and `last` aggregation functions.  

These functions provide a convenient way to select a value from the first or last document in an aggregation group.

Installation
------------

```
bin/plugin --install geoshape-plugin --url "https://github.com/ehalpern/elasticsearch-plugin-first-last/releases/download/v1.0/elasticsearch-first-last-plugin-1.0.zip"
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

