
package dev.prasad.cache;
import java.util.concurrent.atomic.*;
public class CacheStats { final AtomicLong hits=new AtomicLong(), misses=new AtomicLong(), evictions=new AtomicLong(), puts=new AtomicLong();
  void hit(){hits.incrementAndGet();} void miss(){misses.incrementAndGet();} void evict(){evictions.incrementAndGet();} void putOp(){puts.incrementAndGet();}
  public long hits(){return hits.get();} public long misses(){return misses.get();} public long evictions(){return evictions.get();} public long puts(){return puts.get();}
  public double hitRate(){ long t=hits.get()+misses.get(); return t==0?0:(double)hits.get()/t; } }
