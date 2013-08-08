package com.jinjiang.wxc.util;

public class StringUtil {
	
	private final static char CHAR_START = '<';
	private final static char CHAR_END = '>';
	private final static char CHAR_B = 'b';
	private final static char CHAR_R = 'r';
	
	public static String getPureString(String source) {
		String dest = "";
		if(source != null) {
			int recordIndex = 0;
			int startIndex = source.indexOf(CHAR_START);
			while(startIndex != -1) {
				dest = dest + source.substring(recordIndex, startIndex);
				if(source.charAt(startIndex + 1) == CHAR_B
						&& source.charAt(startIndex + 2) == CHAR_R
						&& source.charAt(startIndex + 3) == CHAR_END) {
					if(!dest.equals("")) {
						dest = dest + "\n";
					}
					recordIndex = startIndex + 4;
					startIndex = source.indexOf(CHAR_START, recordIndex);
				} else {
					int i = startIndex;
					while(i < source.length() - 1
							&& source.charAt(i) != CHAR_END ) {
						i++;
					}
					if(i < source.length()) {
						recordIndex = i + 1;
						startIndex = source.indexOf(CHAR_START, recordIndex);
					}
				}
			}
		}
		return dest;
	}

}
