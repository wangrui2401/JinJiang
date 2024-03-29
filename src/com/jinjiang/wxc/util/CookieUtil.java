package com.jinjiang.wxc.util;

public class CookieUtil {
	
	static int[] base64DecodeChars = new int[]{
	        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
	        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
	        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
	        52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
	        -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
	        15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
	        -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
	        41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1};
	
	public static String[] decodeToken(String token) {
		String decodedToken = null;
		decodedToken = decode64(token);
		decodedToken = utf8to16(decodedToken);
		
		return decodedToken.split("\\|");
	}
	
	private static String decode64(String src) {
		String dest = "";
	    char c1, c2, c3, c4;
	    int i, len;
	    len = src.length();
	    i = 0;
	    while (i < len) {
	        do {
	            c1 = (char) base64DecodeChars[src.charAt(i++) & 0xff];
	        } while (i < len && c1 == -1);
	        if (c1 == -1 || i >= len)
	            break;
	        do {
	            c2 = (char) base64DecodeChars[src.charAt(i++) & 0xff];
	        } while (i < len && c2 == -1);
	        if (c2 == -1 || i >= len)
	            break;
	        dest += (char)((c1 << 2) | ((c2 & 0x30) >> 4));
	        do {
	            c3 = (char) (src.charAt(i++) & 0xff);
	            if (c3 == 61)
	                return dest;
	            c3 = (char) base64DecodeChars[c3];
	        } while (i < len && c3 == -1);
	        if (c3 == -1 || i >= len)
	            break;
	        dest += (char)(((c2 & 0XF) << 4) | ((c3 & 0x3C) >> 2));
	        do {
	            c4 = (char) (src.charAt(i++) & 0xff);
	            if (c4 == 61)
	                return dest;
	            c4 = (char) base64DecodeChars[c4];
	        } while (i < len && c4 == -1);
	        if (c4 == -1 || i >= len)
	            break;
	        dest += (char)(((c3 & 0x03) << 6) | c4);
	    }
	    return dest;
	}
	
	private static String utf8to16(String src) {
		String dest = "";
		int len = 0;
		
		if(src != null) {
			int i=0; 
			while(i<src.length()) {
				char c = src.charAt(i++);
				switch(c >> 4) {
				case 0:
	            case 1:
	            case 2:
	            case 3:
	            case 4:
	            case 5:
	            case 6:
	            case 7:
	                // 0xxxxxxx
	            	dest += c;
	                break;
	            case 12:
	            case 13:
	                // 110x xxxx    10xx xxxx
	            	char c1 = src.charAt(i++);
	                dest += (char)(((c & 0x1F) << 6) | (c1 & 0x3F));
	                break;
	            case 14:
	                // 1110 xxxx   10xx xxxx   10xx xxxx
	                char c2 = src.charAt(i++);
	                if(i < src.length()) {
	                	char c3 = src.charAt(i++);
	                	dest += (char)(((c & 0x0F) << 12) | ((c2 & 0x3F) << 6) |
	                			((c3 & 0x3F) << 0));
	                } else {
	                	dest += (char)(((c & 0x0F) << 12) | ((c2 & 0x3F) << 6));
	                }
	                break;
				}
			}
		}
		
		return dest;
	}

}
