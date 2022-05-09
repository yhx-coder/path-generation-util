import constant.ConfigConsts;
import pojo.Cluster;
import pojo.Device;
import pojo.Edge;
import pojo.Graph;
import util.ClusterGenerator;
import util.GraphGenerator;
import util.PathGenerator;

import java.util.*;

/**
 * @author: ming
 * @date: 2022/4/12 18:19
 */
public class NetworkTelemetryApp {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        Graph graph = new GraphGenerator().genGraph(ConfigConsts.NODE_NUM);
        Map<Integer, Cluster> clusterMap = new ClusterGenerator().genCluster(graph);

        List<List<Integer>> path = new ArrayList<>();
        int count = 0;

        // 防止簇间路径重复添加
        Set<Edge> state = new HashSet<>();
        for (Map.Entry<Integer,Cluster> entry: clusterMap.entrySet()) {
            Cluster cluster = entry.getValue();

            // 必须先看簇间，有优化。
            // 获取簇间路径
            List<List<Integer>> intraClusterPath = PathGenerator.generateClusterPath(cluster,state);
            System.out.println("cluster " + cluster.getId() + " 的簇间路径为: " + intraClusterPath);
            cluster.setIntraClusterPath(intraClusterPath);

            // 为了好复制
            path.addAll(intraClusterPath);

            // 获取簇内路径
            List<List<Integer>> interClusterPath = PathGenerator.generatePath(cluster.getHead(),state);
            System.out.println("cluster " + cluster.getId() + " 的簇内路径为: " + interClusterPath);
            cluster.setInterClusterPath(interClusterPath);

            // 为了好复制
            path.addAll(interClusterPath);


            int interClusterPathNum = interClusterPath.size();
            int intraClusterPathNum = intraClusterPath.size();
            int totalPathNum = interClusterPathNum + intraClusterPathNum;
            System.out.println("簇内路径总数为: " + interClusterPathNum);
            System.out.println("簇间路径总数为: " + intraClusterPathNum);
            System.out.println("簇内路径总数为: " + totalPathNum);
            System.out.println("簇内节点数: " + cluster.getSize());
            System.out.println("-----------------------------------------------------");
            count += totalPathNum;
        }
        long endTime = System.currentTimeMillis();
        long span = endTime - startTime;
        System.out.println("网络总路径数为: " + count);
        System.out.println("程序运行时间: " + span + " 毫秒");
        System.out.println("所有的路径: " + path);

        System.out.println(state.size());

//        List<Device> devices = graph.getDevices();
//        for (int i = 0; i < devices.size(); i++) {
//            if (devices.get(i).getId() == 33){
//                System.out.println(devices.get(i).getBelongingCluster().getHead().getId());
//                List<Device> adjDevice = devices.get(i).getAdjDevice();
//                for (int j = 0; j < adjDevice.size(); j++) {
//                    System.out.print(adjDevice.get(i).getId() + " ");
//                }
//            }
//        }


    }
}
