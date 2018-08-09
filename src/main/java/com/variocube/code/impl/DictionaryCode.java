package com.variocube.code.impl;

import com.variocube.code.BinaryToHumanReadableCoder;

public class DictionaryCode implements BinaryToHumanReadableCoder {
	
	//                                               0123456789ABCDEF
	public static final String DEFAULT_DICTIONARY = "123456ACDEHKLPRS";
	
	private final String dictionary;
	
	public DictionaryCode(String dictionary) {
		this.dictionary = dictionary;
	}
	
	public DictionaryCode() {
		this(DEFAULT_DICTIONARY);
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
	
}
