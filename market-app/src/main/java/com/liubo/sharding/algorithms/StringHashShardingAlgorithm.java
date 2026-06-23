package com.liubo.sharding.algorithms;

import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.Collection;
import java.util.Properties;

/**
 * @author 68
 * 2026/6/23 16:21
 */
public class StringHashShardingAlgorithm implements StandardShardingAlgorithm<String> {
    private int padWidth;
    private int startIndex;

    @Override
    public String getType() {
        return "STRING_HASH";
    }

    @Override
    public void init(Properties props) {
        this.padWidth = Integer.parseInt(props.getProperty("pad-width", "2"));
        this.startIndex = Integer.parseInt(props.getProperty("start-index", "1"));
    }

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
        String prefix = shardingValue.getDataNodeInfo().getPrefix();
        long hash = Math.abs(shardingValue.getValue().hashCode());
        long mod = hash % availableTargetNames.size();
        return prefix + String.format("%0" + padWidth + "d", mod + startIndex);
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<String> shardingValue) {
        return availableTargetNames;
    }
}
