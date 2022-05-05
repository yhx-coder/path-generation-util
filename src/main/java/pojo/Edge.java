package pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * @author: ming
 * @date: 2022/3/16 14:47
 */
@Getter
@Setter
@ToString
public class Edge {

    private Device from;
    private Device to;

    public Edge(Device from, Device to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge link = (Edge) o;
        int fromId = from.getId();
        int toId = to.getId();
        int linkFromId = link.getFrom().getId();
        int linkToId = link.getTo().getId();
        if (fromId == linkFromId && toId == linkToId){
            return true;
        }
        if (fromId == linkToId && toId == linkFromId){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

}
