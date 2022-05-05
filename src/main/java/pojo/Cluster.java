package pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.*;

/**
 * @author: ming
 * @date: 2022/4/11 12:00
 */
@ToString
public class Cluster {
    /**
     * 簇 Id
     */
    @Getter
    @Setter
    private Integer id;
    /**
     * 簇所含节点数
     */
    @Getter
    @Setter
    private Integer size;
    /**
     * 簇头
     */
    @Getter
    @Setter
    private Device head;

    /**
     * 簇的边界节点 map
     */
    @Getter
    private final Map<Integer, Device> boundaryMap;


    /**
     * 簇内的所有节点
     */
    @Getter
    private final List<Device> allDeviceList;

    /**
     * 簇内路径
     */
    @Getter
    @Setter
    private List<List<Integer>> intraClusterPath;

    /**
     * 簇间路径
     */
    @Getter
    @Setter
    private  List<List<Integer>> interClusterPath;

    public Cluster(Integer id) {
        this.id = id;
        boundaryMap = new HashMap<>();
        allDeviceList = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cluster cluster = (Cluster) o;
        return id.equals(cluster.id);
    }

    @Override
    public int hashCode() {
        return id;
    }


}
