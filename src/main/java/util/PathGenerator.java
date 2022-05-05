package util;

import pojo.Cluster;
import pojo.Device;
import pojo.Edge;

import java.util.*;

/**
 * @author: ming
 * @date: 2022/3/16 15:00
 */
public class PathGenerator {

    /**
     * 簇内路径生成
     *
     * @param head 簇头节点
     * @return 簇内路径集合
     */
    public static List<List<Integer>> generatePath(Device head) {
        List<List<Integer>> path = new ArrayList<>();
        HashSet<Edge> state = new HashSet<>();

        int clusterId = head.getBelongingCluster().getId();

        List<Device> adjDevice = head.getAdjDevice();
        if (adjDevice == null || adjDevice.size() == 0) {
            List<Integer> temp = new ArrayList<>();
            temp.add(head.getId());
            path.add(temp);
        }
        for (Device d2 : adjDevice) {
            // d2 要在簇内
            if (d2.getBelongingCluster().getId() == clusterId) {
                List<Device> adj = d2.getAdjDevice();
                // 二级节点无法扩展
                if (adj.size() == 1) {
                    List<Integer> temp = new ArrayList<>();
                    temp.add(head.getId());
                    temp.add(d2.getId());
                    Edge test = new Edge(head, d2);
                    if (!state.contains(test)) {
                        path.add(temp);
                        state.add(test);
                    }
                } else {
                    // 存在可扩展的三级节点
                    for (Device d3 : adj) {
                        List<Integer> temp = new ArrayList<>();
                        // d3 在簇内
                        if (d3.getBelongingCluster().getId() == clusterId) {
                            if (d3.getId() != head.getId()) {

                                temp.add(head.getId());
                                temp.add(d2.getId());
                                Edge test1 = new Edge(head, d2);
                                Edge test2 = new Edge(d2, d3);
                                Edge test3 = new Edge(d3, head);
                                if (!state.contains(test1) && !state.contains(test2) && !state.contains(test3)) {
                                    temp.add(d3.getId());
                                    temp.add(head.getId());
                                    // 权宜之计，起点和终点相同时仿真收不到包。正常应该用上面的。可没找到仿真哪里有问题。簇间也是这样处理。
                                    temp.add(d3.getId());
                                    path.add(temp);
                                    state.add(test1);
                                    state.add(test2);
                                    state.add(test3);
                                }
                            }
                            // d3 不在簇内
                        } else {
                            temp.add(head.getId());
                            temp.add(d2.getId());
                            Edge test1 = new Edge(d2, head);
                            if (!state.contains(test1)) {
                                path.add(temp);
                                state.add(test1);
                            }
                        }
                    }
                }
            }
        }
        return path;
    }


    public static List<List<Integer>> generateClusterPath(Cluster cluster, Set<Edge> state) {
        // 结果集
        List<List<Integer>> clusterPath = new ArrayList<>();

        List<Device> boundaryDeviceList = new ArrayList<>(cluster.getBoundaryMap().values());
        int clusterSize = cluster.getSize();

        for (Device boundaryDevice : boundaryDeviceList) {
            // 边界是不是本簇的头设备
            boolean isBoundaryDeviceHead = boundaryDevice.isHead();
            // 获取边界节点的邻居列表
            List<Device> adjDevice = boundaryDevice.getAdjDevice();
            for (Device boundaryAdjDevice : adjDevice) {

                // 邻居是不是头
                boolean isBoundaryAdjDeviceHead = boundaryAdjDevice.isHead();

                // 邻居所属的簇
                Cluster adjCluster = boundaryAdjDevice.getBelongingCluster();
                int adjClusterSize = adjCluster.getSize();

                // 创建边界设备与相邻节点的边。
                Edge edge = new Edge(boundaryDevice, boundaryAdjDevice);

                // 头头相连
                if (isBoundaryDeviceHead && isBoundaryAdjDeviceHead) {
                    if (!state.contains(edge)) {
                        // 本簇规模小
                        if (adjClusterSize > clusterSize) {
                            clusterPath.add(List.of(boundaryDevice.getId(), boundaryAdjDevice.getId()));
                            state.add(edge);
                        }
                        // 规模相等时, Id 大的发包
                        if (adjClusterSize == clusterSize) {
                            if (cluster.getId() > adjCluster.getId()) {
                                clusterPath.add(List.of(boundaryDevice.getId(), boundaryAdjDevice.getId()));
                                state.add(edge);
                            }
                        }
                    }

                }

                if (cluster.getId() != adjCluster.getId()) {
                    // 必须先看头和非头相连，里面有个三角形的优化

                    // 头和非头相连
                    if (isBoundaryDeviceHead && !isBoundaryAdjDeviceHead) {
                        List<Device> memberInAdjList = findMemberInAdjList(cluster.getId(), boundaryAdjDevice);
                        // 无簇间三角形
                        if (memberInAdjList.isEmpty()) {
                            if (!state.contains(edge)) {
                                clusterPath.add(List.of(cluster.getHead().getId(), boundaryAdjDevice.getId()));
                                state.add(edge);
                            }
                        } else {
                            // 有簇间三角形
                            for (Device secondMember : memberInAdjList) {
                                Edge secondEdge = new Edge(boundaryAdjDevice, secondMember);
                                // 一级边和二级扩展边都没加过
                                if (!state.contains(edge) && !state.contains(secondEdge)) {
//                                    clusterPath.add(List.of(cluster.getHead().getId(), boundaryAdjDevice.getId(), secondMember.getId(), cluster.getHead().getId()));
                                    //  权宜之计，起点和终点相同时仿真收不到包。正常应该用上面的。可没找到仿真哪里有问题。
                                    clusterPath.add(List.of(
                                            cluster.getHead().getId(),
                                            boundaryAdjDevice.getId(),
                                            secondMember.getId(),
                                            cluster.getHead().getId(),
                                            secondMember.getId()
                                    ));
                                    state.add(edge);
                                    state.add(secondEdge);
                                }
                                if (!state.contains(edge)) {
                                    clusterPath.add(List.of(cluster.getHead().getId(), boundaryAdjDevice.getId()));
                                    state.add(edge);
                                }
                            }
                        }
                        // 优化之前
//                        if (!state.contains(edge)) {
//                            clusterPath.add(List.of(cluster.getHead().getId(), boundaryAdjDevice.getId()));
//                            state.add(edge);
//                        }
                    }


                    // 非头和非头相连
                    if (!isBoundaryDeviceHead && !isBoundaryAdjDeviceHead) {
                        // 本簇规模小
                        if (adjClusterSize > clusterSize) {
                            if (!state.contains(edge)) {
                                clusterPath.add(List.of(cluster.getHead().getId(), boundaryDevice.getId(), boundaryAdjDevice.getId()));
                                state.add(edge);
                            }
                        }
                        // 规模相等时, Id 大的发包
                        if (adjClusterSize == clusterSize) {
                            if (cluster.getId() > adjCluster.getId()) {
                                if (!state.contains(edge)) {
                                    clusterPath.add(List.of(cluster.getHead().getId(), boundaryDevice.getId(), boundaryAdjDevice.getId()));
                                    state.add(edge);
                                }
                            }
                        }
                    }

                }

            }
        }
        return clusterPath;
    }


    /**
     * 节点有没有目标簇的非头邻居
     *
     * @param clusterId 目标簇 ID
     * @param device    节点
     * @return 目标簇的非头邻居列表。可能为空
     */
    private static List<Device> findMemberInAdjList(Integer clusterId, Device device) {
        List<Device> adjDeviceList = device.getAdjDevice();
        List<Device> targetMemberList = new ArrayList<>();
        for (Device adjDevice : adjDeviceList) {
            if (!adjDevice.isHead() && adjDevice.getBelongingCluster().getId() == clusterId) {
                targetMemberList.add(adjDevice);
            }
        }
        return targetMemberList;
    }


    public static List<Device> generateDevice(int[][] matrix) {
        int row = matrix.length;
        int column = matrix[0].length;
        List<Device> list = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            list.add(new Device(i));
        }

        for (int i = 0; i < row; i++) {
            Device device = list.get(i);
            List<Device> adjDevice = device.getAdjDevice();
            for (int j = 0; j < column; j++) {
                if (matrix[i][j] != 0) {
                    adjDevice.add(list.get(j));
                }
            }
        }
        return list;
    }


//    public static void main(String[] args) {
//
////        Device device1 = new Device(1);
////        Device device2 = new Device(2);
////        Device device3 = new Device(3);
////        Device device4 = new Device(4);
////        Device device5 = new Device(5);
////
////
////        ArrayList<Device> list1 = new ArrayList<>();
////        list1.add(device2);
////        list1.add(device3);
////        list1.add(device4);
////        list1.add(device5);
////        device1.setAdjDevice(list1);
////
////        ArrayList<Device> list2 = new ArrayList<>();
////        list2.add(device1);
////        list2.add(device3);
////        list2.add(device4);
////        device2.setAdjDevice(list2);
////
////        ArrayList<Device> list3 = new ArrayList<>();
////        list3.add(device1);
////        list3.add(device2);
////        list3.add(device5);
////        device3.setAdjDevice(list3);
////
////        ArrayList<Device> list4 = new ArrayList<>();
////        list4.add(device1);
////        list4.add(device2);
////        list4.add(device5);
////        device4.setAdjDevice(list4);
////
////        ArrayList<Device> list5 = new ArrayList<>();
////        list5.add(device1);
////        list5.add(device4);
////        list5.add(device3);
////        device5.setAdjDevice(list5);
////
////        List<List<Integer>> generator = PathGenerator.generatePath(device1);
////        System.out.println(generator);
//
//
//        int[][] graph = new int[][]{
//                {0, 1, 1, 1, 1},
//                {1, 0, 1, 1, 0},
//                {1, 1, 0, 0, 1},
//                {1, 1, 0, 0, 1},
//                {1, 0, 1, 1, 0}
//        };
//
//        List<Device> devices = PathGenerator.generateDevice(graph);
//        List<List<Integer>> lists = PathGenerator.generatePath(devices.get(0));
//        System.out.println(lists);
//    }


}
