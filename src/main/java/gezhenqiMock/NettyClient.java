package gezhenqiMock;

import redis.clients.jedis.Jedis;

public class NettyClient {



    //3个下位机，每个下位机10个通道
    public static void main(String[] args) throws InterruptedException {

        AnaliseService analiseService = new AnaliseService();

        //===========================Netty端============================

        //从下位机获取的某一条原始振动数据

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
                    analiseService.analise(pointData);
                }
            }

            Thread.sleep(1000);
        }



    }

}
