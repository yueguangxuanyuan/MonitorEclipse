package com.xclenter.test.util;

import java.net.NetworkInterface;
import java.util.Enumeration;

public class MAC_Address {
	public static  final String mac_address;
    
    static{
    	mac_address = getLocalMac();
    }
    
	public static String getLocalMac(){
		// TODO Auto-generated method stub
		String mac_str = "unknown";
		try{
			byte[] mac = null;
			final Enumeration<NetworkInterface> interfaces = NetworkInterface
					.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				final NetworkInterface ni = interfaces.nextElement();
				if (ni.isLoopback() || ni.isPointToPoint() || ni.isVirtual())
					continue;
				byte[] macAddress = ni.getHardwareAddress();
				if (macAddress != null && macAddress.length > 0)
					mac = macAddress;
			}
			StringBuffer sb = new StringBuffer("");
			for (int i = 0; i < mac.length; i++) {
				if (i != 0) {
					sb.append("-");
				}
				int temp = mac[i] & 0xff;
				String str = Integer.toHexString(temp);
				if (str.length() == 1) {
					sb.append("0" + str);
				} else {
					sb.append(str);
				}
			}
			mac_str = sb.toString().toUpperCase();
		}catch(Exception e){
			e.printStackTrace();
		}
		return mac_str;
	}
}
