/**
 * Created by Artemy on 17.03.2018.
 */
public class NetAddress {
    private String ipAddress;
    private int mask;

    public NetAddress(String ipAddress, int mask) {
        this.ipAddress = ipAddress;
        this.mask = mask;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }
}
