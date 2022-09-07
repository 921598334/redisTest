package gezhenqiMock;

import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


class AnalizeThread  implements  Runnable{

    Jedis jedis = null;
    PointData pointData = null;

    public AnalizeThread(Jedis jedis ,PointData pointData) {
        this.jedis = jedis;
        this.pointData = pointData;
    }

    public void run() {

        //分析。。。。
        //分析完成
        PointData pointDataAfter =  pointData;

        //需要对结果通过时间排序

        //（该方法不行，会导致顺序混乱）左边插入，插入的下位机的分析结果，提供给上位机使用
        //jedis.lpush("pointDataAfterList", JSON.toJSONString(pointDataAfter));

        //一个下位机，一个通道一个zset,命名规范：pointDataAfterList_id_passId
        jedis.zadd("pointDataAfterList_"+pointDataAfter.getId()+"_"+pointData.getPassId()   ,pointDataAfter.getDate(), JSON.toJSONString(pointDataAfter));

        System.out.println("插入成功");
    }
}



public class AnaliseService {

    //核心线程=下位机数量，最大线程=下位机数目*通道数
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(30, 100, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1));
    private Jedis jedis = null;

    private static volatile AnaliseService analiseService = null;

    public static AnaliseService getInstance(PointData pointData){

        if(analiseService==null){
            synchronized (AnaliseService.class){
                if(analiseService==null){
                    analiseService = new AnaliseService();
                }
            }
        }
        return analiseService;
    }

    public AnaliseService() {
        jedis = new Jedis("122.114.178.53", 6379);
        jedis.auth("denghanbo");
    }

    //进行分析
    public void analise(PointData pointData){

        threadPoolExecutor.execute(new AnalizeThread(jedis,pointData));

    }
    
}
