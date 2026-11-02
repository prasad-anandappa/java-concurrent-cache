
package dev.prasad.cache;
public interface Cache<K,V> { V get(K k); void put(K k,V v); V remove(K k); int size(); void clear(); CacheStats stats(); }
