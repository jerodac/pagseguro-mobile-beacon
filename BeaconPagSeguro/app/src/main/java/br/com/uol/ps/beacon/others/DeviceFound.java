package br.com.uol.ps.beacon.others;

/**
 * Created by jeanrodrigo on 06/11/15.
 */
public class DeviceFound {
    private String mac;
    private int signal;

    public DeviceFound(String mac, int signal) {
        this.mac = mac;
        this.signal = signal;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getSignal() {
        return signal;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    @Override
    public String toString() {
        return "DeviceFound{" +
                "mac='" + mac + '\'' +
                ", signal=" + signal +
                '}';
    }
}