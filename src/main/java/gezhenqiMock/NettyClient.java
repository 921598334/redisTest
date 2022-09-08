package gezhenqiMock;

import com.alibaba.fastjson.JSON;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class NettyClient {



    //3个下位机，每个下位机10个通道
    public static void main(String[] args) throws InterruptedException {

        AnaliseService analiseService = new AnaliseService();

//        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
//        JedisPool jedisPool = new JedisPool(poolConfig, "122.114.178.53", 6379);


        //===========================Netty端============================

        //从下位机获取的某一条原始振动数据

        int count = 0;
        //100条数据
        for(int i=0;i<100;i++){


            for (int salveId = 0; salveId < 3; salveId++) {

                for (int passId = 0; passId < 10; passId++) {

                    PointData pointData = new PointData();
                    pointData.setId(salveId);
                    pointData.setPassId(passId);
                    pointData.setDate(System.currentTimeMillis());

                    Double[] xArray = new Double[100000];
                    Double[] yArray = new Double[100000];
                    for(int j=0;j<100000;j++){
                        xArray[j] = Double.parseDouble(j+"");
                        yArray[j] = Double.parseDouble(j+"");
                    }
                    pointData.setX(xArray);
                    pointData.setY(yArray);

                    //调用远程服务进行分析与存储
                    count++;
                    analiseService.analise(pointData,count);


//                    Jedis jedis = jedisPool.getResource();
//                    jedis.auth("denghanbo");
//                    jedis.zadd("pointDataAfterList_"+pointData.getId()+"_"+pointData.getPassId()   ,pointData.getDate(), JSON.toJSONString(pointData));
//                    jedis.close();


                    System.out.println(salveId+":"+passId+":完成");
                }
            }

            Thread.sleep(1000);
            System.out.println("完成一轮");
        }



    }

}
