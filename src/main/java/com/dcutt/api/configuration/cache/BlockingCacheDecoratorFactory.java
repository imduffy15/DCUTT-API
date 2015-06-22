package com.dcutt.api.configuration.cache;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.constructs.CacheDecoratorFactory;
import net.sf.ehcache.constructs.blocking.BlockingCache;

import java.util.Properties;

public class BlockingCacheDecoratorFactory extends CacheDecoratorFactory {

    private static final int TIMEOUT_MILLIS = 1000;

    @Override
    public Ehcache createDecoratedEhcache(final Ehcache cache, final Properties properties) {
        final BlockingCache blockingCache = new BlockingCache(cache);
        blockingCache.setTimeoutMillis(TIMEOUT_MILLIS);
        return blockingCache;
    }

    @Override
    public Ehcache createDefaultDecoratedEhcache(final Ehcache cache, final Properties properties) {
        return this.createDecoratedEhcache(cache, properties);
    }
}