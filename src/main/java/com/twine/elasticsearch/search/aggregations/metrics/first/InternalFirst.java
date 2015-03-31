package com.twine.elasticsearch.search.aggregations.metrics.first;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.aggregations.AggregationStreams;
import org.elasticsearch.search.aggregations.InternalAggregation;
import org.elasticsearch.search.aggregations.metrics.InternalMetricsAggregation;
import org.elasticsearch.search.aggregations.metrics.MetricsAggregator;
import org.elasticsearch.search.aggregations.support.format.ValueFormatter;
import org.elasticsearch.search.aggregations.support.format.ValueFormatterStreams;

import java.io.IOException;

public class InternalFirst extends InternalMetricsAggregation {
    private static ESLogger LOG = ESLoggerFactory.getLogger(InternalFirst.class.getName());


    public final static Type TYPE = new Type("first");

    public final static AggregationStreams.Stream STREAM = new AggregationStreams.Stream() {
        @Override
        public InternalFirst readResult(StreamInput in) throws IOException {
            InternalFirst result = new InternalFirst();
            result.readFrom(in);
            return result;
        }
    };

    public static void registerStreams() {
        AggregationStreams.registerStream(STREAM, TYPE.stream());
    }

    protected ValueFormatter valueFormatter;

    public InternalFirst() {}

    private Object value;

    public InternalFirst(String name, Object value) {
        super(name);
        this.value = value;
    }

    @Override
    public Type type() {
        return TYPE;
    }

    @Override
    public InternalFirst reduce(ReduceContext reduceContext) {
        LOG.info("Reducing " + reduceContext.aggregations());

        for (InternalAggregation aggregation : reduceContext.aggregations()) {
            Object value = ((InternalFirst)aggregation).value;
            return new InternalFirst(name, value);
        }
        return new InternalFirst(name, null);
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        name = in.readString();
        valueFormatter = ValueFormatterStreams.readOptional(in);
        value = in.readString();
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(name);
        ValueFormatterStreams.writeOptional(valueFormatter, out);
        out.writeString((String)value);
    }

    @Override
    public XContentBuilder doXContentBody(XContentBuilder builder, Params params) throws IOException {
        builder.field(CommonFields.VALUE, value);
        if (valueFormatter != null) {
            builder.field(CommonFields.VALUE_AS_STRING, String.valueOf(value));
        }
        return builder;
    }
}