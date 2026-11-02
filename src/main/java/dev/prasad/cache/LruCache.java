
package dev.prasad.cache;
import java.util.*; import java.util.concurrent.locks.*;
public class LruCache<K,V> implements Cache<K,V> {
  private final int cap; private final LinkedHashMap<K,V> map; private final ReentrantLock lock=new ReentrantLock(); private final CacheStats stats=new CacheStats();
  public LruCache(int cap){ if(cap<=0) throw new IllegalArgumentException(); this.cap=cap; this.map=new LinkedHashMap<>(16,0.75f,true){ protected boolean removeEldestEntry(Map.Entry<K,V> e){ if(size()>cap){ stats.evict(); return true;} return false; } }; }
  public V get(K k){ lock.lock(); try{ V v=map.get(k); if(v==null) stats.miss(); else stats.hit(); return v; } finally{lock.unlock();} }
  public void put(K k,V v){ Objects.requireNonNull(k); Objects.requireNonNull(v); lock.lock(); try{ stats.putOp(); map.put(k,v);} finally{lock.unlock();} }
  public V remove(K k){ lock.lock(); try{return map.remove(k);} finally{lock.unlock();} }
  public int size(){ lock.lock(); try{return map.size();} finally{lock.unlock();} }
  public void clear(){ lock.lock(); try{map.clear();} finally{lock.unlock();} }
  public CacheStats stats(){return stats;}
}
