package com.variocube.code.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.variocube.code.ParityCoderException;

public class DictionaryCoderTest {

	@Test
	public void testRandomCode() throws ParityCoderException {
		assertEquals("The dictionary must be exactly 16 characters long", 16, DictionaryCoder.DEFAULT_DICTIONARY.length());
		byte[] byteCode = new byte[] { 104, -67, -36, 72, -46, 34, 127, 19 };
		DictionaryCoder coder = new DictionaryCoder();
		String encoded = coder.encodeData(byteCode);
		byte[] decoded = coder.decodeData(encoded);
		assertArrayEquals(byteCode, decoded);
	}
	
	@Test
	public void testSimpleEncodeAndDecode() {
		byte[] data = new byte[] { 0x03, 0x4A, (byte) 0xA1, (byte) 0xFF};
		DictionaryCoder coder = new DictionaryCoder();
		String encoded = coder.encodeData(data);
		assertEquals("Encoded output is of inappropriate size", 8, encoded.length());
		String expected = "145HH2SS";
		assertEquals("Encoded output is not correct", expected, encoded);
		byte[] decoded = coder.decodeData(encoded);
		assertArrayEquals(data, decoded);
	}
	
	private static final String[] MATCHING = {
			"onlinegroup.at creative online systems GmbH\nc/oMatthias Steinbauer 25KD PKCC\nFranckstrasse 45\n4020 Linz",
			"VARIOCUBE GmbH\nc/oFranz Spindler 25KDPKCC\nFranckstrasse 45\n4020 Linz",
			"solutiongroup.at GmbH\nc/oDietmar Hauber 25KD.PKCC\nFranckstrasse 45\n4020 Linz"
	};
	
	private static final String[] NON_MATCHING = {
			"onlinegroup.at creative online systems GmbH\nc/oMatthias Steinbauer\nFranckstrasse 45\n4020 Linz",
			"VARIOCUBE GmbH\nc/oFranz Spindler 25 KDPKC\nFranckstrasse 45\n4020 Linz",
			"solutiongroup.at GmbH\nc/oDietmar Hauber PKCC\nFranckstrasse 45\n4020 Linz",
			"solutiongroup.at GmbH\nc/oDietmar Hauber 25KDPKCC4\nFranckstrasse 45\n4020 Linz",
	};
	
	@Test
	public void testRegexes() {
		DictionaryCoder coder = new DictionaryCoder();
		Pattern pattern = Pattern.compile(coder.detectionPattern());
		for(String m : MATCHING) {
			Matcher matcher = pattern.matcher(m);
			assertTrue(matcher.find());
		}
		for(String m : NON_MATCHING) {
			Matcher matcher = pattern.matcher(m);
			assertFalse(matcher.find());
		}
	}
	
}
