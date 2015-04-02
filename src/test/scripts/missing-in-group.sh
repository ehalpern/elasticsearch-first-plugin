#!/usr/bin/env bash
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
. "$DIR/init.sh"

curl -s -XPUT "localhost:9200/test/city/10" -d '{"city": "Ann Arbor", "state": "MI" }'

curl -s -XPOST "http://localhost:9200/test/_refresh"

curl -s -XGET "localhost:9200/test/city/_search?search_type=count&pretty=true" -d '{
  "query": {
    "match_all": { }
  },
  "aggs": {
    "state": {
      "terms": { "field" : "state"},
      "aggs": {
        "fields": {
          "scripted_metric": {
            "params" : { "fields": ["state", "population"] },
            "map_script" : "first_map",
            "combine_script" : "first_combine",
            "reduce_script" : "first_reduce",
            "lang" : "native"
          }
        }
      }
    }
  }
}'
