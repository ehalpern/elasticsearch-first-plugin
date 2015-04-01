package com.twine.elasticsearch.search.aggregations.metrics.first;

import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.elasticsearch.script.AbstractSearchScript;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by eric on 3/31/15.
 */
public class FirstCombineScriptFactory implements NativeScriptFactory {
  public ExecutableScript newScript(final Map<String, Object> params) {
    return new AbstractSearchScript() {
      List<Object> values = XContentMapValues.extractRawValues("values", params);

      public Object run() {
        return values;
      }
    };
  }
}
