/**
 * Created by Artemy on 15.03.2018.
 */
public class Netter {
    public static void main(String[] args) {
        String ip =  "169.219.0.0";
        int maskInt = 16;
        int subNetNumber = 86;

        new NetFrame(true);
    }


    public static String getHostAddr(String netAddr, int hostnum) {
        Long netAddrInt = Long.parseLong(netAddr, 2);
        return Long.toBinaryString(netAddrInt + hostnum);
    }

    public static int isIntegerValue(String str) {
        if (str == null) return -1;
        if (str.equals(""))  return -1;
        int result = 0;
        try {
            result = Integer.parseInt(str);
        } catch (NumberFormatException ne) {
            System.out.println("Its not a number");
            return -1;
        }
        return result;
    }

    public static String getBinaryIpAsString(String source) {
        if (source == null)
            return null;
        String[] array = source.split("\\.");
        StringBuilder str = new StringBuilder();
        for (String s : array) {
            s = Integer.toBinaryString(Integer.valueOf(s));
            str.append(padLeft(s, 8));
        }
        return str.toString();
    }

    public static int getBitsForSubNet(int subNetNumber) {
        int bitsForSubNet = 1;
        int minSubNetNumber = 2;
        while(true) {
            if (minSubNetNumber >= subNetNumber)
                break;
            minSubNetNumber = minSubNetNumber * 2;
            bitsForSubNet +=1;
        }
        return bitsForSubNet;
    }

    public static String getHumanReadableAddr(String source) {
        if (source.length() != 32)
            return "source String is not 32 characters long";
        return Integer.parseInt(source.substring(0,8), 2)
                + "." + Integer.parseInt(source.substring(8,16), 2)
                + "." + Integer.parseInt(source.substring(16, 24), 2)
                + "." + Integer.parseInt(source.substring(24, 32), 2);
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s).replace(" ", "0");
    }


    public static String getBroadcastAddr(String s, int n) {
        return String.format("%1$-" + n + "s", s).replace(" ", "1");
    }

    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s).replace(" ", "0");
    }

    private static String[] getOctets(String source) {
        String[] result = null;
        if (source == null)
            return null;
        result = source.split("\\.");
        for (String s : result) {
            s = Integer.toBinaryString(Integer.valueOf(s));
            System.out.println(s);
        }
        return result;
    }


    public static NetAddress checkInputValues(String source) {
        if (source.endsWith("/"))
            return null;
        String[] IpAndMask = (source.split("/"));
        int mask = Integer.parseInt(IpAndMask[1]);
        if ( mask > 24 )
            return null;
        String[] ip = IpAndMask[0].split("\\.");
        for (String s : ip) {
            if (s == null)
                return null;
            if (Integer.parseInt(s) > 255)
                return null;
        }
        System.out.println("AllRight");
        return new NetAddress(IpAndMask[0] ,mask);
    }

    public static String getMask4Octets(int mask) {
        StringBuilder tmp = new StringBuilder();
        for (int i = 0; i < mask; i++) {
            tmp.append("1");
        }
        System.out.println("tmp is = " + tmp);
        String bitMask = padRight(tmp.toString(), 32);
        return getHumanReadableAddr(bitMask);
    }

}
