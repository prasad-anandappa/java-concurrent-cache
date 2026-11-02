
package dev.prasad.cache;
import java.util.*; import java.util.concurrent.locks.*;
public class LfuCache<K,V> implements Cache<K,V> {
  private final int cap; private final Map<K,V> vals=new HashMap<>(); private final Map<K,Long> freq=new HashMap<>(); private final Map<K,Long> access=new HashMap<>(); private long tick; private final ReentrantLock lock=new ReentrantLock(); private final CacheStats stats=new CacheStats();
  public LfuCache(int cap){ if(cap<=0) throw new IllegalArgumentException(); this.cap=cap; }
  public V get(K k){ lock.lock(); try{ V v=vals.get(k); if(v==null){stats.miss(); return null;} stats.hit(); freq.merge(k,1L,Long::sum); access.put(k,++tick); return v; } finally{lock.unlock();} }
  public void put(K k,V v){ Objects.requireNonNull(k); Objects.requireNonNull(v); lock.lock(); try{ stats.putOp(); if(!vals.containsKey(k) && vals.size()>=cap){ K victim=vals.keySet().stream().min(Comparator.comparingLong((K x)->freq.getOrDefault(x,0L)).thenComparingLong(x->access.getOrDefault(x,0L))).orElseThrow(); vals.remove(victim); freq.remove(victim); access.remove(victim); stats.evict(); } vals.put(k,v); freq.putIfAbsent(k,0L); access.put(k,++tick); } finally{lock.unlock();} }
  public V remove(K k){ lock.lock(); try{ freq.remove(k); access.remove(k); return vals.remove(k);} finally{lock.unlock();} }
  public int size(){ lock.lock(); try{return vals.size();} finally{lock.unlock();} }
  public void clear(){ lock.lock(); try{vals.clear(); freq.clear(); access.clear();} finally{lock.unlock();} }
  public CacheStats stats(){return stats;}
}
