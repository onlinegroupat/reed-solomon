package com.variocube.code.impl;

import com.variocube.code.BinaryToHumanReadableCoder;

public class DictionaryCoder implements BinaryToHumanReadableCoder {
	
	//                                               0123456789ABCDEF
	public static final String DEFAULT_DICTIONARY = "123456ACDEHKLPRS";
	public static final int DEFAULT_CODE_LENGTH = 8;
	
	private final String dictionary;
	private final int codeLength;
	
	public DictionaryCoder(String dictionary, int codeLength) {
		this.dictionary = dictionary;
		this.codeLength = codeLength;
	}
	
	public DictionaryCoder() {
		this(DEFAULT_DICTIONARY, DEFAULT_CODE_LENGTH);
	}
	
	@Override
	public String encodeData(byte[] data) {
		char[] encoded = new char[data.length * 2];
		for(int i=0; i<data.length; i++) {
			byte b = data[i];
			byte highByte = (byte) (0x0F & (b >> 4));
			byte lowByte = (byte) (0x0F & b);
			encoded[i * 2] = this.dictionary.charAt(highByte);
			encoded[i * 2 + 1] = this.dictionary.charAt(lowByte);
		}
		return new String(encoded);
	}
	
	@Override
	public byte[] decodeData(String input) {
		byte[] data = new byte[input.length() / 2];
		for(int i=0; i<data.length; i++) {
			byte highByte = (byte) this.dictionary.indexOf(input.charAt(i * 2));
			byte lowByte = (byte) this.dictionary.indexOf(input.charAt(i * 2 + 1));
			data[i] = (byte) (highByte << 4 | lowByte);
		}
		return data;
	}

	@Override
	public String detectionPattern() {
		String nonFormattedCode = String.format("(\\s[%s]{%d}\\s)", this.dictionary, this.codeLength);
		int halfCode = this.codeLength / 2;
		String formattedCode = String.format("(\\s[%s]{%d}[ \\.:][%s]{%d}\\s)", this.dictionary, halfCode, this.dictionary, halfCode);
		return String.format("%s|%s", nonFormattedCode, formattedCode);
	}
	
}
