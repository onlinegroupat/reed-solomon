package com.variocube.code.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.variocube.code.ParityCoderException;

public class RandomReedSolomonParityTest {

	@Test
	public void testRandomCode() throws ParityCoderException {
		byte[] byteCode = new byte[] { 104, -67, -36, 72, -46, 34, 127, 19 };
		byte[][] expectedMatrix = new byte[][] {
			{ 104, -67 },
			{ -36, 72 },
			{ -46, 34 },
			{ 127, 19 } 
		};
		RandomReedSolomonParityCoder coder = new RandomReedSolomonParityCoder();
		byte[][] matrix = coder.arrayToMatrix(byteCode);
		assertArrayEquals(expectedMatrix, matrix);
		byte[] decodedByteArray = new byte[byteCode.length];
		coder.copyMatrixToArray(matrix, decodedByteArray);
	}
	
	@Test
	public void testEncodeParity() throws ParityCoderException {
		byte[] byteCodeRandom = new byte[] { 104, -67, -36, 72, -46, 34, 127, 19 };
		byte[] byteCodeWithZeros = new byte[] { 104, -67, -36, 72, -46, 34, 0, 0 };
		RandomReedSolomonParityCoder coder = new RandomReedSolomonParityCoder();
		coder.encodeParity(byteCodeRandom);
		coder.encodeParity(byteCodeWithZeros);
		assertArrayEquals(byteCodeRandom, byteCodeWithZeros);
		assertTrue(coder.isParityCorrect(byteCodeRandom));
		assertTrue(coder.isParityCorrect(byteCodeWithZeros));
		
		DictionaryCode dict = new DictionaryCode();
		String encodedString = dict.encodeData(byteCodeRandom);
		System.out.println(encodedString);
		byte[] decodedData = dict.decodeData(encodedString);
		assertArrayEquals(byteCodeRandom, decodedData);
		
		byteCodeRandom[3] = 7;
		assertFalse(coder.isParityCorrect(byteCodeRandom));
		
		coder.decodeMissing(byteCodeRandom);
		for(int i=0; i<6; i++) {
			assertEquals(byteCodeWithZeros[i], byteCodeRandom[i]);
		}
	}
	
}
