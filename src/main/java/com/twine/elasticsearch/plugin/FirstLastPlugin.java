package com.twine.elasticsearch.plugin;

import com.twine.elasticsearch.search.aggregations.metrics.first.FirstParser;
import com.twine.elasticsearch.search.aggregations.metrics.first.InternalFirst;
import org.elasticsearch.plugins.AbstractPlugin;
import org.elasticsearch.search.aggregations.AggregationModule;

public class FirstLastPlugin extends AbstractPlugin {

    @Override
    public String name() {
        return "First Last Plugin";
    }

    @Override
    public String description() {
        return "Provide first and last aggregation metric functions";
    }

    public void onModule(AggregationModule aggModule) {
        aggModule.addAggregatorParser(FirstParser.class);
        InternalFirst.registerStreams();
    }
}
