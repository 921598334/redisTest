import com.alibaba.fastjson.JSON;
import redis.clients.jedis.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class RedisUtils {


    public static void main(String[] args) {
        //1、 new Jedis() 对象即可
        final Jedis jedis = new Jedis("122.114.178.53", 6379);
        jedis.auth("denghanbo");
        //jedis 的所以命令就是之前学习的所以redis指令！
        System.out.println(jedis.ping());


        Set<String> keys = jedis.keys("z*");


        System.out.println(JSON.toJSON(keys));
    }
}
