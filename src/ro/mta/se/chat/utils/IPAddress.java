package ro.mta.se.chat.utils;

/**
 * Created by Alberto-Daniel on 12/24/2015.
 * This is an utility class that can handle
 * operations on IPAdress
 */
public class IPAddress {
    /**
     * The int value from IPAdress
     */
    private final int value;

    /**
     * Constructor that takes an int value as param
     *
     * @param value the int value
     */
    public IPAddress(int value) {
        this.value = value;
    }

    /**
     * This is the constructor that takes as arguments
     * the string representation of an IPv4 adress
     *
     * @param stringValue the string representation of ipAdress
     */
    public IPAddress(String stringValue) {
        String[] parts = stringValue.split("\\.");
        if (parts.length != 4) {
            throw new IllegalArgumentException();
        }
        value =
                (Integer.parseInt(parts[0], 10) << (8 * 3)) & 0xFF000000 |
                        (Integer.parseInt(parts[1], 10) << (8 * 2)) & 0x00FF0000 |
                        (Integer.parseInt(parts[2], 10) << (8 * 1)) & 0x0000FF00 |
                        (Integer.parseInt(parts[3], 10) << (8 * 0)) & 0x000000FF;
    }

    /**
     * This function returns an octet from an int
     *
     * @param i the int parameter
     * @return the octet value
     */
    public int getOctet(int i) {

        if (i < 0 || i >= 4) throw new IndexOutOfBoundsException();

        return (value >> (i * 8)) & 0x000000FF;
    }

    /**
     * Returns the string representation of an IP Adress
     *
     * @return the string ip
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 3; i >= 0; --i) {
            sb.append(getOctet(i));
            if (i != 0) sb.append(".");
        }

        return sb.toString();

    }

    /**
     * This function check if an object is equal with another
     *
     * @param obj the rhs object
     * @return true if objects are equal false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IPAddress) {
            return value == ((IPAddress) obj).value;
        }
        return false;
    }

    /**
     * return the hashcode of object
     *
     * @return
     */
    @Override
    public int hashCode() {
        return value;
    }

    /**
     * This function return the ip adress
     *
     * @return the ip adress
     */
    public int getValue() {
        return value;
    }

    /**
     * This function iterate through ipAdress
     *
     * @return the nextIp adress
     */
    public IPAddress next() {
        return new IPAddress(value + 1);
    }
}
