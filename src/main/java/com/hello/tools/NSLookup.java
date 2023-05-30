package com.hello.tools;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class NSLookup {
    public static List<String> lookup(String domain){
        ArrayList<String> ipLists = new ArrayList<>();
        try {
            InetAddress[] addresses = InetAddress.getAllByName(domain);
            for (InetAddress address : addresses) {
                ipLists.add(address.getHostAddress());
            }
        } catch (UnknownHostException e) {
            System.err.println("Could not find host: " + domain);
        }
        return ipLists;
    }
}
