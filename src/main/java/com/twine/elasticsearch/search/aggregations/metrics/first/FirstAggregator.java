package com.twine.elasticsearch.search.aggregations.metrics.first;


import org.apache.lucene.index.AtomicReaderContext;
import org.elasticsearch.common.lease.Releasables;
import org.elasticsearch.common.util.DoubleArray;
import org.elasticsearch.common.util.LongArray;
import org.elasticsearch.common.util.ObjectArray;
import org.elasticsearch.index.fielddata.SortedBinaryDocValues;
import org.elasticsearch.index.fielddata.SortedNumericDoubleValues;
import org.elasticsearch.search.aggregations.Aggregator;
import org.elasticsearch.search.aggregations.InternalAggregation;
import org.elasticsearch.search.aggregations.metrics.MetricsAggregator;
import org.elasticsearch.search.aggregations.metrics.NumericMetricsAggregator;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.valuecount.InternalValueCount;
import org.elasticsearch.search.aggregations.support.AggregationContext;
import org.elasticsearch.search.aggregations.support.ValuesSource;
import org.elasticsearch.search.aggregations.support.ValuesSourceAggregatorFactory;
import org.elasticsearch.search.aggregations.support.ValuesSourceConfig;

import java.io.IOException;

/**
 * A field data based aggregator that counts the number of values a specific field has within
 * the aggregation context.
 *
 * This aggregator works in a multi-bucket mode, that is, when serves as a sub-aggregator, a singlei
 * aggregator instance aggregates the  counts for all buckets owned by the parent aggregator)
 */
public class FirstAggregator extends MetricsAggregator {
  private final ValuesSource.Bytes valuesSource;
  private SortedBinaryDocValues values;

  private ObjectArray bucketValues;

  public FirstAggregator(String name, long estimatedBucketsCount, ValuesSource.Bytes valuesSource, AggregationContext context, Aggregator parent) {
    super(name, estimatedBucketsCount, context, parent);
    this.valuesSource = valuesSource;
    if (valuesSource != null) {
      final long initialSize = estimatedBucketsCount < 2 ? 1 : estimatedBucketsCount;
      bucketValues = bigArrays.newObjectArray(initialSize);
    }
  }

  @Override
  public boolean shouldCollect() {
    return valuesSource != null;
  }

  @Override
  public void setNextReader(AtomicReaderContext reader) {
    values = valuesSource.bytesValues();
  }

  @Override
  // map
  public void collect(int doc, long owningBucketOrdinal) throws IOException {
    bucketValues = bigArrays.grow(bucketValues, owningBucketOrdinal + 1);
    values.setDocument(doc);
    Object value = (values.count() > 0) ? values.valueAt(0) : null;
    bucketValues.set(owningBucketOrdinal, value);
  }

  @Override
  // combine
  public InternalAggregation buildAggregation(long owningBucketOrdinal) {
    if (valuesSource == null) {
      return new InternalFirst(name, null);
    } else {
      return new InternalFirst(name, bucketValues.get(owningBucketOrdinal));
    }
  }

  @Override
  public InternalAggregation buildEmptyAggregation() {
    return new InternalFirst(name, null);
  }

  public static class Factory extends ValuesSourceAggregatorFactory.LeafOnly<ValuesSource.Bytes> {

    public Factory(String name, ValuesSourceConfig<ValuesSource.Bytes> valuesSourceConfig) {
      super(name, InternalSum.TYPE.name(), valuesSourceConfig);
    }

    @Override
    protected Aggregator createUnmapped(AggregationContext aggregationContext, Aggregator parent) {
      return new FirstAggregator(name, 0, null, aggregationContext, parent);
    }

    @Override
    protected Aggregator create(ValuesSource.Bytes valuesSource, long expectedBucketsCount, AggregationContext aggregationContext, Aggregator parent) {
      return new FirstAggregator(name, expectedBucketsCount, valuesSource, aggregationContext, parent);
    }
  }

  @Override
  public void doClose() {
    Releasables.close(bucketValues);
  }
}
