package com.twine.elasticsearch.plugin;

import com.twine.elasticsearch.search.aggregations.metrics.first.*;
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
    //LOG.info("Registering aggregation parser");
    //aggModule.addAggregatorParser(FirstParser.class);
    //InternalFirst.registerStreams();
  }

  public void onModule(ScriptModule module) {
    LOG.info("Registering scripts");
    module.registerScript("first_map",     FirstMapScriptFactory.class);
    module.registerScript("first_combine", FirstCombineScriptFactory.class);
    module.registerScript("first_reduce", FirstReduceScriptFactory.class);
  }
}
