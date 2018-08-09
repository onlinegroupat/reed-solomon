package com.variocube.code.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.variocube.code.ParityCoderException;
import com.variocube.code.impl.DictionaryCode;

public class DictionaryCodeTest {

	@Test
	public void testRandomCode() throws ParityCoderException {
		assertEquals("The dictionary must be exactly 16 characters long", 16, DictionaryCode.DEFAULT_DICTIONARY.length());
		byte[] byteCode = new byte[] { 104, -67, -36, 72, -46, 34, 127, 19 };
		DictionaryCode coder = new DictionaryCode();
		String encoded = coder.encodeData(byteCode);
		byte[] decoded = coder.decodeData(encoded);
		assertArrayEquals(byteCode, decoded);
	}
	
	@Test
	public void testSimpleEncodeAndDecode() {
		byte[] data = new byte[] { 0x03, 0x4A, (byte) 0xA1, (byte) 0xFF};
		DictionaryCode coder = new DictionaryCode();
		String encoded = coder.encodeData(data);
		assertEquals("Encoded output is of inappropriate size", 8, encoded.length());
		String expected = "145HH2SS";
		assertEquals("Encoded output is not correct", expected, encoded);
		byte[] decoded = coder.decodeData(encoded);
		assertArrayEquals(data, decoded);
	}
	
}
