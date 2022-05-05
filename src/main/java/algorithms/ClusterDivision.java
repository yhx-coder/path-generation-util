package algorithms;

import pojo.Device;
import pojo.Graph;

import java.util.*;

/**
 * @author: ming
 * @date: 2022/4/20 15:12
 */
public class ClusterDivision {

//    // 保障兼容的无奈选择
//    static class Info{
//        // 簇头节点列表
//        List<Device> headList;
//
//        // 簇头:[簇内节点]
//        Map<Device,List<Device>> clusterMap;
//
//        Info(List<Device> headList, Map<Device, List<Device>> clusterMap) {
//            this.headList = headList;
//            this.clusterMap = clusterMap;
//        }
//    }
//
//    /**
//     * 网络分簇
//     * @param graph 网络拓扑
//     * @return 分簇结果信息
//     */
//    public static Info clusterTheNetwork(Graph graph){
//        // 构成结果集,保障兼容
//        List<Device> headList = new ArrayList<>();
//        Map<Device,List<Device>> clusterMap = new HashMap<>();
//
//        // 图中所有节点
//        List<Device> deviceList = graph.getDevices();
//
//
//        // 未分配节点集合
//        Set<Device> unassignedDeviceSet = new HashSet<>(deviceList);
//        // 簇头的被选集
//        Set<Device> clusterHeadCandidateSet = new HashSet<>(deviceList);
//        // 暂时的分簇情况
//        Map<Device,Map<Integer,Device>> tempClusterMap = new HashMap<>();
//
//        while (!unassignedDeviceSet.isEmpty()){
//            // 找到一个头
//            Device head = findHead(clusterHeadCandidateSet);
//            head.setAssignment(1);
//            headList.add(head);
//            // 自身属性确定
//            unassignedDeviceSet.remove(head);
//            // 对邻居节点进行染色, 只侵染未被分配的节点.
//            List<Device> adjDeviceList = head.getAdjDevice();
//            for (Device adjDevice: adjDeviceList) {
//                if (adjDevice.isAssigned() == -1)
//            }
//
//            // 成为头就失去竞选资格
//            clusterHeadCandidateSet.remove(head);
//
//        }
//
//
//
//        return new Info(headList,clusterMap);
//
//    }

    /**
     * 选取拓扑中的簇头节点列表
     * <p>
     * 历史原因，只能返回列表。
     *
     * @param graph 拓扑
     * @return 簇头列表
     */
    public static List<Device> findClusterHead(Graph graph) {
        // 簇头节点列表
        List<Device> headList = new ArrayList<>();

        // 图中所有节点
        List<Device> deviceList = graph.getDevices();

        // 未分配节点集合
        Set<Device> unassignedDeviceSet = new HashSet<>(deviceList);
        // 簇头的被选集
        Set<Device> clusterHeadCandidateSet = new HashSet<>(deviceList);

        while (!unassignedDeviceSet.isEmpty()) {
            // 找到一个头
            Device head = findHead(clusterHeadCandidateSet);
            headList.add(head);
            head.setHead(true);
            // 成为头就失去竞选资格
            clusterHeadCandidateSet.remove(head);

            // 自身属性确定
            head.setDyeing(true);
            unassignedDeviceSet.remove(head);
            // 对邻居节点进行染色, 只侵染未被分配的节点.
            List<Device> adjDeviceList = head.getAdjDevice();
            for (Device adjDevice : adjDeviceList) {
                if (!adjDevice.isDyeing()) {
                    adjDevice.setDyeing(true);
                    unassignedDeviceSet.remove(adjDevice);
                }
            }
        }
        return headList;
    }


    /**
     * 从被选集中选一个簇头
     *
     * @param candidateSet 被选集
     * @return 簇头节点
     */
    private static Device findHead(Set<Device> candidateSet) {
        Device head = null;
        int num = -1;
        for (Device d : candidateSet) {
            int unassignedNeighborNum = getUnassignedNeighborNum(d);
            if (unassignedNeighborNum > num) {
                head = d;
                num = unassignedNeighborNum;
            }
        }

        return head;

    }


    /**
     * 获取邻居节点中未染色的节点个数
     *
     * @param d 节点
     * @return 未染色的邻居节点个数
     */
    private static int getUnassignedNeighborNum(Device d) {
        int count = 0;
        List<Device> adjDevice = d.getAdjDevice();
        for (Device device : adjDevice) {
            if (!device.isDyeing()) {
                count++;
            }
        }
        return count;
    }


    /**
     * 判别各节点归属于哪个头
     *
     * @param headList 头结点列表
     * @param graph    拓扑
     * @return 簇头: [成员节点]
     */
    public static Map<Device, List<Device>> findClusterSlave(List<Device> headList, Graph graph) {
        // 结果集
        Map<Device, List<Device>> clusterMap = new HashMap<>();

        // 已分配好的非头节点
        Set<Device> assignedDeviceSet = new HashSet<>();

        List<Device> copyOfHeadList = new ArrayList<>(headList);

        while (!copyOfHeadList.isEmpty()){
            // 找打分最低的头
            int lowestScoreIndex = lowestScoreIndex(copyOfHeadList, assignedDeviceSet);

            // 删除打分最低的头
            Device head = copyOfHeadList.remove(lowestScoreIndex);
            // 寻找能被该头支配的节点
            List<Device> memberList = new ArrayList<>();
            List<Device> adjDeviceList = head.getAdjDevice();
            for (Device adjDevice : adjDeviceList) {
                if (!adjDevice.isHead() && !assignedDeviceSet.contains(adjDevice)){
                    memberList.add(adjDevice);
                    assignedDeviceSet.add(adjDevice);
                }
            }
            clusterMap.put(head,memberList);
        }

        return clusterMap;
    }


    /**
     * 给头结点打分。分数就是非头且没有被划分的邻居数。选一个最小的。
     *
     * @param headList          头结点列表
     * @param assignedDeviceSet 已经分配好的非头节点
     * @return 分数最低的节点标号
     */
    private static int lowestScoreIndex(List<Device> headList, Set<Device> assignedDeviceSet) {
        int size = headList.size();
        int[] score = new int[size];
        int curLowestScore = Integer.MAX_VALUE;
        int curIndex = -1;
        for (int i = 0; i < size; i++) {
            Device device = headList.get(i);
            List<Device> adjDeviceList = device.getAdjDevice();
            for (Device adjDevice : adjDeviceList) {
                if (!adjDevice.isHead() && !assignedDeviceSet.contains(adjDevice)) {
                    score[i]++;
                }
            }
            if (score[i] < curLowestScore) {
                curLowestScore = score[i];
                curIndex = i;
            }
        }
        return curIndex;
    }


}
