package util;

import algorithms.ClusterDivision;

import pojo.Cluster;
import pojo.Device;
import pojo.Graph;

import java.util.*;

/**
 * @author: ming
 * @date: 2022/4/11 14:48
 */
public class ClusterGenerator {


    private Map<Integer, Cluster> map;


    public Map<Integer, Cluster> genCluster(Graph graph) {
        map = new HashMap<>();
        genClusterHelper(graph);
        genClusterBoundaryHelper();
        return map;
    }


    /**
     * 构建簇的辅助函数.此时没有边界信息。
     *
     * @param graph 网络拓扑
     */
    private void genClusterHelper(Graph graph) {
        // 获取簇头节点列表
        List<Device> minimumDominatingSet = ClusterDivision.findClusterHead(graph);

        Map<Device, List<Device>> deviceListMap = ClusterDivision.findClusterSlave(minimumDominatingSet, graph);
        // 遍历头结点列表
        for (int i = 0; i < minimumDominatingSet.size(); i++) {
            // 簇头
            Device device = minimumDominatingSet.get(i);

            // 簇内从属节点列表
            List<Device> slaveList = deviceListMap.get(device);

            // 生成簇
            Cluster cluster = new Cluster(i);
            cluster.setHead(device);
            cluster.setSize(slaveList.size() + 1);
            List<Device> allDeviceList = cluster.getAllDeviceList();
            allDeviceList.add(device);
            allDeviceList.addAll(slaveList);

            map.put(i, cluster);

            // 为节点添加簇有关信息
            device.setBelongingCluster(cluster);
            for (Device slave : slaveList) {
                slave.setHead(false);
                slave.setBelongingCluster(cluster);
            }

        }
    }

    /**
     * 生成簇边界的辅助函数
     */
    private void genClusterBoundaryHelper() {
        Collection<Cluster> clusters = map.values();
        for (Cluster c : clusters) {
            List<Device> allDeviceList = c.getAllDeviceList();
            // 检查集群中的所有设备
            for (Device d : allDeviceList) {
                // 获取设备的邻居节点
                List<Device> adjDevice = d.getAdjDevice();
                for (Device adj : adjDevice) {
                    // 如果邻居节点不属于该集群，则该节点就是集群边界节点。
                    if (adj.getBelongingCluster().getId() != c.getId() &&
                            !c.getBoundaryMap().containsKey(d.getId())) {

                        c.getBoundaryMap().put(d.getId(),d);
                    }
                }
            }
        }
    }

}
