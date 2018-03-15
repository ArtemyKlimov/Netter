/**
 * Created by Artemy on 15.03.2018.
 */
public class Netter {
    public static void main(String[] args) {
        String ip = "94.0.0.0";
        String mask = "8";
        int maskInt = Integer.parseInt(mask);
        int neededSubNet = 1;
        int subNetNumber = 7 + 2;
        int minSubNetNumber = 2;
        int bitsForSubNet = 1;
        int neededHost = 672805;
        while(true) {
            if (minSubNetNumber >= subNetNumber)
                break;
            minSubNetNumber = minSubNetNumber * 2;
            bitsForSubNet +=1;
        }
        int newMask = maskInt + bitsForSubNet;
        System.out.println(minSubNetNumber);
        System.out.println(bitsForSubNet);

        int[] binaryIp = getBinaryIP(ip);
        for (int bit : binaryIp) {
            System.out.print(bit);
        }
        System.out.println();
        String originalIP = getBinaryIpAsString(ip);
        String originalNetAddr = originalIP.substring(0, maskInt);
        int subnetAddr = Integer.parseInt(originalIP.substring(maskInt, newMask), 2) + neededSubNet;
        System.out.println(originalNetAddr);
        System.out.println(Integer.toBinaryString(subnetAddr));
        String subnetAddrResult = padRight(originalNetAddr + Integer.toBinaryString(subnetAddr), 32);
        String broadcastSubNetAddr = getBroadcastAddr(originalNetAddr + Integer.toBinaryString(subnetAddr), 32);
        System.out.println(subnetAddrResult);
        System.out.println(broadcastSubNetAddr);
        String hostAddr = padLeft(getHostAddr(subnetAddrResult, neededHost), 32);
        System.out.println(hostAddr);
        System.out.println(getToHumanReadableAddr(hostAddr) + "/" + newMask);
    }

    private static String getBinaryIpAsString(String source) {
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

    private static String getToHumanReadableAddr(String source) {
        if (source.length() != 32)
            return "source String is not 32 characters long";
        return Integer.parseInt(source.substring(0,8), 2)
                + "." + Integer.parseInt(source.substring(8,16), 2)
                + "." + Integer.parseInt(source.substring(16, 24), 2)
                + "." + Integer.parseInt(source.substring(24, 32), 2);
    }

    private static int[] getBinaryIP(String source) {
        if (source == null)
            return null;
        String[] array = source.split("\\.");
        StringBuilder str = new StringBuilder();
        for (String s : array) {
            s = Integer.toBinaryString(Integer.valueOf(s));
            str.append(padLeft(s, 8));
        }
        int result[] = new int[32];
        String s = str.toString();
        for (int i = 0; i < s.length(); ++i)
            result[i] = Character.getNumericValue(s.charAt(i));
        return result;
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

    private static String getHostAddr(String netAddr, int hostnum) {
        int netAddrInt = Integer.parseInt(netAddr, 2);
        return Integer.toBinaryString(netAddrInt + hostnum);
    }

}
