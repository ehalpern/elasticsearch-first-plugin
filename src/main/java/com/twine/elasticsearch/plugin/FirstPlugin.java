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
    LOG.info("Registering aggregation parser");
    aggModule.addAggregatorParser(FirstParser.class);
    InternalFirst.registerStreams();
  }

  public void onModule(ScriptModule module) {
    LOG.info("Registering scripts");
    module.registerScript("first_map",     FirstScripts.MapFactory.class);
    module.registerScript("first_combine", FirstScripts.CombineFactory.class);
    module.registerScript("first_reduce", FirstScripts.ReduceFactory.class);
  }
}
