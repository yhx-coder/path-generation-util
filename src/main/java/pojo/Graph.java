package pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.*;


/**
 * @author: ming
 * @date: 2022/4/11 16:54
 */
public class Graph {
    /**
     * 拓扑中的所有节点
     */
    @Getter
    private Map<Integer, Device> nodes;


    public Graph() {
        nodes = new HashMap<>();
    }

    public int getNumDevices() {
        return nodes.size();
    }

    public List<Device> getDevices() {
        return new ArrayList<>(nodes.values());
    }

    public void addNode(Device device){
        if (!nodes.containsKey(device.getId())){
            nodes.put(device.getId(),device);
        }
    }


}
