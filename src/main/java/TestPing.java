//import com.alibaba.fastjson.JSON;
//import gezhenqiMock.PointData;
//import redis.clients.jedis.Jedis;
//
//import java.util.HashSet;
//import java.util.Set;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
//
////基本思路，下位机解析出数据后发送给服务分析端，分析出来后从写入redis的zset中，最后上位机获取
//
//public class TestPing {
//
//    //3个下位机，每个下位机10个通道
//    public static void main(String[] args){
//
//        //1、 new Jedis() 对象即可
//        final Jedis jedis = new Jedis("122.114.178.53", 6379);
//        jedis.auth("denghanbo");
//        //jedis 的所以命令就是之前学习的所以redis指令！
//        System.out.println(jedis.ping());
//
//
//
//        //===========================Netty端============================
//
//        //从下位机获取的某一条原始振动数据
//        final PointData pointData = new PointData();
//        pointData.setId(i);
//        pointData.setPassId(1);
//        pointData.setDate(System.currentTimeMillis());
//        pointData.setX(new Double[]{1.0,2.0,3.0});
//        pointData.setY(new Double[]{1.1,2.2,3.3});
//
//
//
//
//
//        //=============利用Feign发送给其他服务进行分析，下面假设是其他服务的处理逻辑================
//
//
//        //核心线程=下位机数量，最大线程=下位机数目*通道数
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 30, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1));
//
//        //分析.....
//        executor.execute(new Runnable() {
//            public void run() {
//                //分析。。。。
//                //分析完成
//                PointData pointDataAfter =  pointDataAfter11;
//
//                //需要对结果通过时间排序
//
//                //（该方法不行，会导致顺序混乱）左边插入，插入的下位机的分析结果，提供给上位机使用
//                //jedis.lpush("pointDataAfterList", JSON.toJSONString(pointDataAfter));
//
//                //一个下位机，一个通道一个zset,命名规范：pointDataAfterList_id_passId
//                jedis.zadd("pointDataAfterList_"+pointDataAfter.getId()+"_"+pointData.getPassId()   ,pointDataAfter.getDate(),JSON.toJSONString(pointDataAfter));
//            }
//        });
//
//
//
//
//
//        //=============下面假设是上位机获取分析结果================
//        //创建1个线程实时获取
//
//        while (true){
//
//            //获取key来判断当前的下位机和通道
//            Set<String> keys = jedis.keys("pointDataAfterList_");
//
//            Set<String> salveIdSet = new HashSet<String>();
//            Set<String> passIdSet = new HashSet<String>();
//
//            for(String key:keys){
//                String[] keyBody = key.split("_");
//                salveIdSet.add(keyBody[1]);
//                passIdSet.add(keyBody[2]);
//            }
//
//
//            for(String salve:salveIdSet){
//                for(String passId:passIdSet){
//
//                    //获取某个下位机，某个通道最新的元素
//                    String key = "pointDataAfterList_" + salve + "_" + passId;
//                    Set<String> lastDataSet = jedis.zrange(key, 0L, 0L);
//                    if(lastDataSet==null || lastDataSet.size()==0){
//                        continue;
//                    }
//
//
//                    String lastData = lastDataSet.toArray()[0].toString();
//
//                    //删除
//                    jedis.zrem(key,lastData);
//
//
//
//                    //进行展示
//                    PointData showPointData = JSON.parseObject(lastData,PointData.class);
//
//
//                }
//            }
//
//
//
//
//        }
//
//
//
//
//
//
//
//          //10s过期
////        jedis.setex("key",10,"value");
//
//
////        System.out.println("清空数据:"+jedis.flushDB());
////        System.out.println("判断某个键是否存在: "+jedis.exists( "username"));
////        System.out.println("新增< 'username ' , 'kuangshen'>的键值对:"+jedis.set("username","kuangshen"));
////        System.out.println("新增<'password' , 'password'>的键值对:"+jedis.set("password","password"));
////        System. out.print("系统中所有的键如下:");
////        Set<String> keys = jedis.keys( "*");
////        System.out.println(keys) ;
////        System.out.println("删除键password: "+jedis.del( "password"));
////        System.out.println("判断键password是否存在: "+jedis.exists( "password"));
////        System.out.println("查看键username所存储的值的类型:"+jedis.type("username"));
////        System.out.println("随机返回key空间的一个: "+jedis.randomKey());
////        System.out.println("重命名key: "+jedis.rename( "username" ,"name"));
////        System.out.println("取出改后的name: "+jedis.get( "name" )) ;
////        System.out.println("按索引查询:"+jedis.select( 0));
////        System.out.println("删除当前选择数据库中的所有key: "+jedis.flushDB());
////        System.out.println("返回当前数据库中key的数目:"+jedis.dbSize());
////        System.out.println("删除所有数据库中的所有key: "+jedis.flushAll());
//
//
//
//    }
//}
