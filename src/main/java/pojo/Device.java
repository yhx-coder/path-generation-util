package pojo;

import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author: ming
 * @date: 2022/3/16 14:33
 */

public class Device {
    /**
     * 设备ID
     */
    @Getter
    private int id;
    /**
     * 相连的设备
     */
    @Getter
    private final List<Device> adjDevice;

    /**
     * 是否是簇头
     */
    @Getter @Setter
    private boolean isHead;


    /**
     * 所属的簇
     */
    @Getter @Setter
    private Cluster belongingCluster;


    /**
     * 节点状态.
     * false 是未染色
     * true 是已染色
     */
    @Getter @Setter
    private boolean dyeing;

    public Device(int id) {
        this.id = id;
        adjDevice = new ArrayList<>();
        dyeing = false;
    }

    /**
     * 获取该节点的邻居数
     * @return 相邻节点数
     */
    public int getAdjDeviceNum(){
        return adjDevice.size();
    }

    /**
     * 计算节点的非头邻居数
     * @return 非头邻居数
     */
    public int slaveAdjDeviceNum(){
        int count = 0;
        for (Device d:adjDevice) {
            if (!d.isHead){
                count++;
            }
        }
        return count;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return id == device.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }



    @Override
    public String toString() {
        return ""+id;
    }
}
