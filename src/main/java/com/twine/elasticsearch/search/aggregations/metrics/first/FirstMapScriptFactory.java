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
public class FirstMapScriptFactory implements NativeScriptFactory {
  public ExecutableScript newScript(final Map<String, Object> params) {
    return new AbstractSearchScript() {
      List<Object> fields = XContentMapValues.extractRawValues("fields", params);
      List<Object> values = XContentMapValues.extractRawValues("values", params);

      public Object run() {
        if (values.isEmpty()) {
          for (int i = 0; i < fields.size(); i++) {
            values.add(source().extractValue(fields.get(i).toString()));
          }
        }
        return null;
      }
    };
  }
}
