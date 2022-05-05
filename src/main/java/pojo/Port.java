package pojo;

import java.util.List;

/**
 * @author: ming
 * @date: 2022/3/16 14:34
 */
public class Port {
    private int id;

    /**
     * 0 是关闭, 1 是开启
      */
    private byte state;

    private long bytesReceived;

    private long bytesSent;

    private int correspondingSwitchId;

    private int correspondingPortId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public long getBytesReceived() {
        return bytesReceived;
    }

    public void setBytesReceived(long bytesReceived) {
        this.bytesReceived = bytesReceived;
    }

    public long getBytesSent() {
        return bytesSent;
    }

    public void setBytesSent(long bytesSent) {
        this.bytesSent = bytesSent;
    }

    public int getCorrespondingSwitchId() {
        return correspondingSwitchId;
    }

    public void setCorrespondingSwitchId(int correspondingSwitchId) {
        this.correspondingSwitchId = correspondingSwitchId;
    }

    public int getCorrespondingPortId() {
        return correspondingPortId;
    }

    public void setCorrespondingPortId(int correspondingPortId) {
        this.correspondingPortId = correspondingPortId;
    }

    @Override
    public String toString() {
        return "Port{" +
                "id=" + id +
                ", state=" + state +
                ", bytesReceived=" + bytesReceived +
                ", bytesSent=" + bytesSent +
                ", correspondingSwitchId=" + correspondingSwitchId +
                ", correspondingPortId=" + correspondingPortId +
                '}';
    }
}
