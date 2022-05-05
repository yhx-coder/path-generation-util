package util;


import com.sun.jdi.PathSearchingVirtualMachine;
import constant.ConfigConsts;
import pojo.Device;
import pojo.Graph;

import java.io.*;
import java.util.Map;

/**
 * @author: ming
 * @date: 2022/4/11 19:27
 */
public class GraphGenerator {

    private  Graph graph;

    public Graph genGraph(int deviceNum){
        graph = new Graph();
        genDevice(deviceNum);
        genAdjDevice();
        return graph;
    }


    private void genDevice(int deviceNum){
        for (int i = 0; i < deviceNum; i++) {
            Device d = new Device(i);
            graph.addNode(d);
        }
    }

    private void genAdjDevice(){
        Map<Integer, Device> deviceMap = graph.getNodes();

        String filePath = GraphGenerator.class.getClassLoader()
                .getResource(ConfigConsts.TOPO_FILE_NAME).getPath();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            int count = 0;
            String temp;
            while ((temp = br.readLine()) != null){
                if (count > 0){
                    String[] s = temp.split(" ");

                    int deviceId1 = Integer.parseInt(s[0]);
                    int deviceId2 = Integer.parseInt(s[1]);
                    Device device1 = deviceMap.get(deviceId1);
                    Device device2 = deviceMap.get(deviceId2);
                    device1.getAdjDevice().add(device2);
                    device2.getAdjDevice().add(device1);
                }
                count++;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
