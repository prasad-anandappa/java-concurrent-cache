# Java Concurrent Cache — Thread-Safe LRU + LFU Cache

I built a thread-safe in-memory cache library in Java 21 with two eviction policies behind one Cache interface: an access-order LRU cache and an LFU cache that evicts the least-frequently-used entry (with recency as the tie-breaker). Every cache tracks hit, miss, eviction and put counts with a hit-rate calculation, and all operations are guarded so concurrent readers and writers stay consistent — which I verify with multi-threaded stress tests.

## Highlights

- LruCache, LfuCache, CacheStats, configurable capacity.
- Java 21, Maven, JUnit 5.
- I wrote this as a Software Engineer to demonstrate the underlying mechanics, not just the APIs — everything here is implemented from first principles.

## Build and test

```bash
export JAVA_HOME=/path/to/jdk-21
mvn verify
```

## Verification

`mvn verify` — **Tests run: 8, Failures: 0, Errors: 0** (verified locally before publishing).

## License

MIT — see [LICENSE](LICENSE). Copyright (c) 2026 Prasad Dimba Anandappa.
