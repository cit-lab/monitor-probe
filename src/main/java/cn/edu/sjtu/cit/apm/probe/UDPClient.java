package cn.edu.sjtu.cit.apm.probe;

import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

public class UDPClient {

    private DatagramSocket socket;
    private InetAddress ip;
    private Integer port;
    private Integer timeout;
    private Timer timer;
    private TimerTask task;

    public UDPClient(String host, Integer port, Integer timeout) throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        ip = InetAddress.getByName(host);
        this.port = port;
        this.timeout = timeout;
        socket.setSoTimeout(timeout);
    }


    public UDPClient(String host, String port, Integer timeout) throws SocketException, UnknownHostException {
        this(host, Integer.parseInt(port), timeout);
    }

    public String send(String data) throws IOException {
        byte[] receiveData = new byte[1024];
//        System.out.println("want to send: " + data);
        byte[] sendData = data.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, port);
        socket.send(sendPacket);
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);
        return new String(receivePacket.getData());
    }

    public boolean registerInstance(String instanceName) throws IOException {
        String req = "APM_REGISTER:" + instanceName + ":" + Util.getPID();
        String res;
        try {
            res = send(req);
        } catch (SocketTimeoutException e) {
            // TODO: use log4j
            System.err.println("Timeout for " + timeout);
            // TODO: Add a delay job in another thread
            return false;
        }
        // FIXME: the receive string has a long empty part ....
        if (res.startsWith("registered")) {
//            System.out.println("registered");
            return true;
        } else {
            // TODO: store the error message and return in another method
//            System.err.println("fail to register, collector return: " + res);
            return false;
        }
    }

    // Keep registering instance until success
    // TODO: interval should be larger than socket timeout
    public void registerInstance(final String instanceName, final Integer interval) {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                try {
                    System.out.println("Try to register instance every " + interval + "ms");
                    if (registerInstance(instanceName)) {
                        task.cancel();
                        timer.cancel();
                        System.out.println("Registered! cancel timer!");
                    }
                } catch (IOException e) {
                    System.out.println("Got IOException " + e.getClass());
                }
            }
        };
        timer.schedule(task, interval, interval);
    }
}