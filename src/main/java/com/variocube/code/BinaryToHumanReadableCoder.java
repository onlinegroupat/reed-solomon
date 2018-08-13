package com.variocube.code;

public interface BinaryToHumanReadableCoder {

	String encodeData(byte[] data);
	
	byte[] decodeData(String input);
	
	String detectionPattern();
	
}
