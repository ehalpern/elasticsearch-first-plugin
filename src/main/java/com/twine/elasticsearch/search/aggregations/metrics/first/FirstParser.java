package com.twine.elasticsearch.search.aggregations.metrics.first;

import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.search.aggregations.Aggregator;
import org.elasticsearch.search.aggregations.AggregatorFactory;
import org.elasticsearch.search.aggregations.support.ValueType;
import org.elasticsearch.search.aggregations.support.ValuesSourceParser;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;


public class FirstParser implements Aggregator.Parser {

  @Override
  public String type() {
    return InternalFirst.TYPE.name();
  }

  @Override
  public AggregatorFactory parse(String aggregationName, XContentParser parser, SearchContext context)
    throws IOException
  {
    ValuesSourceParser vsParser =
      ValuesSourceParser.any(aggregationName, InternalFirst.TYPE, context)
        .targetValueType(ValueType.STRING)
        .formattable(true)
        .build();

    XContentParser.Token token;

    String currentFieldName = null;

    while((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
      if (token == XContentParser.Token.FIELD_NAME) {
        currentFieldName = parser.currentName();
      } else if (vsParser.token(currentFieldName, token, parser)) {
        continue;
      }
    }
    return new FirstAggregator.Factory(aggregationName, vsParser.config());
  }
}