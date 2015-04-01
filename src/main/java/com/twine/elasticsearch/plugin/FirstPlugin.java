package com.twine.elasticsearch.plugin;

import com.twine.elasticsearch.search.aggregations.metrics.first.FirstParser;
import com.twine.elasticsearch.search.aggregations.metrics.first.FirstScripts;
import com.twine.elasticsearch.search.aggregations.metrics.first.InternalFirst;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.plugins.AbstractPlugin;
import org.elasticsearch.script.ScriptModule;
import org.elasticsearch.search.aggregations.AggregationModule;

public class FirstPlugin extends AbstractPlugin {
  private static ESLogger LOG = ESLoggerFactory.getLogger(FirstPlugin.class.getName());

  @Override
  public String name() {
    return "first";
  }

  @Override
  public String description() {
    return "Provide first aggregation metric functions";
  }

  public void onModule(AggregationModule aggModule) {
    aggModule.addAggregatorParser(FirstParser.class);
    InternalFirst.registerStreams();
  }

  public void onModule(ScriptModule module) {
    module.registerScript("firstMap",     FirstScripts.MapFactory.class);
    module.registerScript("firstCombine", FirstScripts.CombineFactory.class);
    module.registerScript("firstReduce", FirstScripts.ReduceFactory.class);
  }
}
