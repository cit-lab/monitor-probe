package cn.edu.sjtu.cit.apm.probe;

import java.io.*;
import java.net.*;

public class UDPClient {

    private DatagramSocket socket;
    private InetAddress ip;
    private Integer port;

    public UDPClient(String host, Integer port) throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        ip = InetAddress.getByName(host);
        this.port = port;
    }

    public String send(String data) throws IOException {
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        System.out.println("want to send: " + data);
        sendData = data.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, port);
        socket.send(sendPacket);
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);
        return new String(receivePacket.getData());
    }

    public boolean registerInstance(String instanceName) throws IOException {
        String req = "APM_REGISTER:" + instanceName + ":" + Util.getPID();
        String res = send(req);
        // FIXME: the receive string has a long empty part ....
        if (res.startsWith("registered")) {
            System.out.println("registered");
            return true;
        } else {
            System.err.println("fail to register, collector return: " + res);
            return false;
        }
    }
}