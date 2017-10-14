package ro.mta.se.chat.utils;

import java.nio.ByteBuffer;

/**
 * Created by Alberto-Daniel on 11/26/2015.
 * This is a class that contains function responsable for converting
 * different data types to other types
 */
public class Convert {
    /**
     * This function converts a byte value to an int value
     *
     * @param bvalue byte value
     * @return the int value
     */
    public static int byteToInt(byte bvalue) {
        ByteBuffer data = Utils.getByteBuffer(4);
        data.put(bvalue);
        return data.getInt(0);
    }

    /**
     * Thi function converts an int value to a byte value
     *
     * @param value int value
     * @return byte value
     */
    public static byte intToByte(int value) {
        ByteBuffer data = Utils.getByteBuffer(4);
        data.putInt(value);
        return data.get(0);
    }

    /**
     * This function converts an int value to hex format
     *
     * @param value the int value
     * @return the hex format
     */
    public static String intToHex(int value) {
        String hexValue = Integer.toHexString(value).toUpperCase();
        if (hexValue.length() == 1)
            hexValue = "0" + hexValue;
        return hexValue;
    }

    /**
     * This function converts the byte value to  hex format
     *
     * @param value the byte value
     * @return the hex format
     */
    public static String byteToHex(byte value) {
        return intToHex(byteToInt(value));
    }

    /**
     * This function converts the int value to long
     *
     * @param value the int value
     * @return the long value
     */
    public static long intToLong(int value) {
        ByteBuffer data = Utils.getByteBuffer(8);
        data.putInt(value);
        return data.getLong(0);
    }

    /**
     * This function converts the ip array to string
     *
     * @param array the byte array
     * @return the string format
     */
    public static String ipToString(byte[] array) {
        String IPAddress = "";
        for (int i = 0; i < array.length - 1; i++)
            IPAddress = IPAddress + "" + Convert.byteToInt(array[i]) + ".";
        IPAddress = IPAddress + "" + Convert.byteToInt(array[array.length - 1]);
        return IPAddress;
    }
    

    /**
     * This function converts the string ip to byte[] format
     *
     * @param IPAddress
     * @return
     */
    public static byte[] stringIPToArray(String IPAddress) {
        int j = 0;
        byte[] data = new byte[4];
        for (int i = 0; i < 3; i++) {
            String p = "";
            while (IPAddress.charAt(j) != '.')
                p = p + IPAddress.charAt(j++);
            j++;
            data[i] = (byte) Short.parseShort(p);

        }
        String p = "";
        for (int i = j; i < IPAddress.length(); i++)
            p = p + IPAddress.charAt(i);
        data[3] = ((byte) Short.parseShort(p));

        return (data);
    }
}
