package com.twine.elasticsearch.plugin;

import com.twine.elasticsearch.search.aggregations.metrics.first.FirstParser;
import com.twine.elasticsearch.search.aggregations.metrics.first.InternalFirst;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.plugins.AbstractPlugin;
import org.elasticsearch.search.aggregations.AggregationModule;

public class FirstLastPlugin extends AbstractPlugin
{
    private static ESLogger LOG = ESLoggerFactory.getLogger(FirstLastPlugin.class.getName());
    @Override
    public String name() {
        return "first";
    }

    @Override
    public String description() {
        return "Provide first and last aggregation metric functions";
    }

    public void onModule(AggregationModule aggModule) {
        LOG.info("Installing plugin");
        aggModule.addAggregatorParser(FirstParser.class);
        InternalFirst.registerStreams();
    }
}
