package gezhenqiMock;

import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.Set;

public class MasterClient {

    //上位机获取数据进行展示
    public static void main(String[] args) {

        Jedis jedis = new Jedis("122.114.178.53", 6379);
        jedis.auth("denghanbo");


        while (true){

            //获取key来判断当前的下位机和通道
            Set<String> keys = jedis.keys("pointDataAfterList_");

            Set<String> salveIdSet = new HashSet<String>();
            Set<String> passIdSet = new HashSet<String>();

            for(String key:keys){
                String[] keyBody = key.split("_");
                salveIdSet.add(keyBody[1]);
                passIdSet.add(keyBody[2]);
            }


            for(String salve:salveIdSet){
                for(String passId:passIdSet){

                    //获取某个下位机，某个通道最新的元素
                    String key = "pointDataAfterList_" + salve + "_" + passId;
                    Set<String> lastDataSet = jedis.zrange(key, 0L, 0L);
                    if(lastDataSet==null || lastDataSet.size()==0){
                        continue;
                    }


                    String lastData = lastDataSet.toArray()[0].toString();

                    //删除
                    jedis.zrem(key,lastData);



                    //进行展示
                    PointData showPointData = JSON.parseObject(lastData,PointData.class);


                }
            }

        }

    }
}