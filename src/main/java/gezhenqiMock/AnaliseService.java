package gezhenqiMock;

import com.alibaba.fastjson.JSON;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


class AnalizeThread  implements  Runnable{

    JedisPool jedisPool = null;
    PointData pointData = null;
    int count = 0;
    public AnalizeThread(JedisPool jedisPool ,PointData pointData,int count) {
        this.jedisPool = jedisPool;
        this.pointData = pointData;
        this.count = count;
    }

    public void run() {

        Jedis jedis = jedisPool.getResource();
        jedis.auth("denghanbo");


        //分析。。。。
        //分析完成
        PointData pointDataAfter =  pointData;

        //需要对结果通过时间排序

        //（该方法不行，会导致顺序混乱）左边插入，插入的下位机的分析结果，提供给上位机使用
        //jedis.lpush("pointDataAfterList", JSON.toJSONString(pointDataAfter));

        //一个下位机，一个通道一个zset,命名规范：pointDataAfterList_id_passId
        jedis.zadd("pointDataAfterList_"+pointDataAfter.getId()+"_"+pointData.getPassId()   ,pointDataAfter.getDate(), JSON.toJSONString(pointDataAfter));
        jedis.close();
        System.out.println(count+"插入成功");
    }
}



public class AnaliseService {

    //核心线程=下位机数量，最大线程=下位机数目*通道数
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(300, 3000, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1));
    private JedisPool jedisPool = null;



    public AnaliseService() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        jedisPool = new JedisPool(poolConfig, "122.114.178.53", 6379,10000);
    }

    //进行分析
    public void analise(PointData pointData,int count){

        threadPoolExecutor.execute(new AnalizeThread(jedisPool,pointData,count));

    }
    
}
