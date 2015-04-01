#!/usr/bin/env bash
curl -s -XDELETE "http://localhost:9200/test"
echo
curl -s -XPUT "http://localhost:9200/test/" -d '{
    "settings": {
        "index.number_of_shards": 1,
        "index.number_of_replicas": 0
    },
    "mappings": {
        "state": {
            "properties": {
                "name": {
                    "type": "string"
                },
                "capital": {
                    "type": "string"
                },
                "nickname": {
                    "type": "string"
                }
            }
        },
        "city": {
            "properties": {
                "city": {
                    "type": "string"
                },
                "state": {
                    "type": "string",
                    "index": "not_analyzed"
                },
                "population": {
                    "type": "integer"
                }
            }
        }
    }
}'
echo
curl -s -XPUT "localhost:9200/test/state/CT" -d '{"name": "Connecticut", "capital": "Hartford", "nickname": "Constitution State"}'
curl -s -XPUT "localhost:9200/test/state/ME" -d '{"name": "Maine", "capital": "Augusta", "nickname": "Lumber State"}'
curl -s -XPUT "localhost:9200/test/state/MA" -d '{"name": "Massachusetts", "capital": "Boston", "nickname": "Bay State"}'
curl -s -XPUT "localhost:9200/test/state/NH" -d '{"name": "New Hampshire", "capital": "Concord", "nickname": "Granite State"}'
curl -s -XPUT "localhost:9200/test/state/RI" -d '{"name": "Rhode Island", "capital": "Providence", "nickname": "Little Rhody"}'
curl -s -XPUT "localhost:9200/test/state/VT" -d '{"name": "Vermont", "capital": "Montpelier", "nickname": "Green Mountain State"}'

curl -s -XPUT "localhost:9200/test/city/1" -d '{"city": "Cambridge", "state": "MA", "population": 105162}'
curl -s -XPUT "localhost:9200/test/city/2" -d '{"city": "South Burlington", "state": "VT", "population": 17904}'
curl -s -XPUT "localhost:9200/test/city/3" -d '{"city": "South Portland", "state": "ME", "population": 25002}'
curl -s -XPUT "localhost:9200/test/city/4" -d '{"city": "Essex", "state": "VT", "population": 19587}'
curl -s -XPUT "localhost:9200/test/city/5" -d '{"city": "Portland", "state": "ME", "population": 66194}'
curl -s -XPUT "localhost:9200/test/city/6" -d '{"city": "Burlington", "state": "VT", "population": 42417}'
curl -s -XPUT "localhost:9200/test/city/7" -d '{"city": "Stamford", "state": "CT", "population": 122643}'
curl -s -XPUT "localhost:9200/test/city/8" -d '{"city": "Colchester", "state": "VT", "population": 17067}'
curl -s -XPUT "localhost:9200/test/city/9" -d '{"city": "Concord", "state": "NH", "population": 42695}'
curl -s -XPUT "localhost:9200/test/city/10" -d '{"city": "Boston", "state": "MA", "population": 617594}'

curl -s -XPOST "http://localhost:9200/test/_refresh"
echo
curl -s -XGET "localhost:9200/test/city/_search?search_type=count&pretty=true" -d '{
  "query": {
    "match_all": { }
  },
  "aggs": {
    "state": {
      "terms": { "field" : "state"}
    },
    "fields": {
      "scripted_metric": {
        "params" : { "fields": ["state"], "values": [] },
        "map_script" : "first_map",
        "combine_script" : "first_combine",
        "reduce_script" : "first_reduce",
        "lang" : "native"
      }
    }
  }
}'
