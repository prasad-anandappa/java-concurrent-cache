
package dev.prasad.cache;
import org.junit.jupiter.api.*; import static org.junit.jupiter.api.Assertions.*; import java.util.concurrent.*;
class CacheTest {
  @Test void lruEvictsLeastRecentlyUsed(){ var c=new LruCache<String,Integer>(2); c.put("a",1); c.put("b",2); c.get("a"); c.put("c",3); assertNull(c.get("b")); assertEquals(1,c.get("a")); assertEquals(1,c.stats().evictions()); }
  @Test void lfuEvictsLeastFrequent(){ var c=new LfuCache<String,Integer>(2); c.put("a",1); c.put("b",2); c.get("a"); c.get("a"); c.get("b"); c.put("c",3); assertNull(c.get("b")); assertEquals(1,c.get("a")); assertEquals(3,c.get("c")); assertEquals(1,c.stats().evictions()); }
  @Test void statsHitMiss(){ var c=new LruCache<String,Integer>(10); c.put("x",9); c.get("x"); c.get("y"); assertEquals(1,c.stats().hits()); assertEquals(1,c.stats().misses()); assertEquals(0.5,c.stats().hitRate(),1e-9); }
  @Test void overwriteDoesNotGrow(){ var c=new LruCache<String,Integer>(2); c.put("a",1); c.put("a",2); assertEquals(1,c.size()); assertEquals(2,c.get("a")); }
  @Test void removeAndClear(){ var c=new LfuCache<String,Integer>(5); c.put("a",1); assertEquals(1,c.remove("a")); c.put("b",2); c.clear(); assertEquals(0,c.size()); }
  @Test void concurrentLru() throws Exception { var c=new LruCache<Integer,Integer>(100); int threads=8, per=1000; var pool=Executors.newFixedThreadPool(threads); var latch=new CountDownLatch(threads); for(int t=0;t<threads;t++){ int base=t; pool.submit(()->{ for(int i=0;i<per;i++){ c.put(i%200,i); c.get(i%200);} latch.countDown(); return null;}); } assertTrue(latch.await(10,TimeUnit.SECONDS)); pool.shutdown(); assertTrue(c.size()<=100); assertTrue(c.stats().hits()+c.stats().misses()>0); }
  @Test void concurrentLfu() throws Exception { var c=new LfuCache<Integer,Integer>(50); var pool=Executors.newFixedThreadPool(4); var f=new java.util.ArrayList<Future<?>>(); for(int t=0;t<4;t++) f.add(pool.submit(()->{ for(int i=0;i<500;i++){c.put(i%80,i); c.get(i%80);} })); for(var x:f) x.get(10,TimeUnit.SECONDS); pool.shutdown(); assertTrue(c.size()<=50); }
  @Test void rejectsZeroCapacity(){ assertThrows(IllegalArgumentException.class,()->new LruCache<>(0)); assertThrows(IllegalArgumentException.class,()->new LfuCache<>(0)); }
}
