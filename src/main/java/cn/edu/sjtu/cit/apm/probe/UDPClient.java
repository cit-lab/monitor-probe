package cn.edu.sjtu.cit.apm.probe;

import java.io.*;
import java.net.*;

class UDPClient {
    public static void main(String args[]) throws Exception {
        System.out.println("udp client start!");
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        String sentence = "pia!";
        sendData = sentence.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
        clientSocket.send(sendPacket);
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        String modifiedSentence = new String(receivePacket.getData());
        System.out.println("FROM SERVER:" + modifiedSentence);
        clientSocket.close();
    }

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

    public boolean registerProc() throws IOException {
        System.out.println(send("APM_REGISTER"));
        return false;
    }
}