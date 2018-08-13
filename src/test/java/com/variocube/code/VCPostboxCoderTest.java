package com.variocube.code;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.variocube.code.impl.DictionaryCoder;
import com.variocube.code.impl.RandomReedSolomonParityCoder;

public class VCPostboxCoderTest {
	
	@Test
	public void testRandomCodeGeneration() throws ParityCoderException {
		VCPostboxCoder coder = VCPostboxCoder.createSixteenDigitCoder();
		String random = coder.generateRandomCode();
		assertEquals(16, random.length());
	}
	
	@Test
	public void testCodeValidation() throws ParityCoderException {
		String validCode = "ADKPPL5DP333AAPC";
		String invalidCode = "ADKP1L5DP333AAPC";
		VCPostboxCoder coder = VCPostboxCoder.createSixteenDigitCoder();
		assertTrue(coder.isValidCode(validCode));
		assertFalse(coder.isValidCode(invalidCode));
	}
	
	@Test
	public void testRandomCodeValidation() throws ParityCoderException {
		VCPostboxCoder coder = VCPostboxCoder.createDefault();
		String randomCode = coder.generateRandomCode();
		assertTrue(coder.isValidCode(randomCode));
	}
	
	@Test
	public void testInvalidCodeReconstruction() throws ParityCoderException {
		String validCode = "ADKPPL5DP333AAPC";
		String invalidCode = "ADKP1L5DP333AAPC";
		VCPostboxCoder coder = VCPostboxCoder.createSixteenDigitCoder();
		String[] candidates = coder.reconstructionCandidates(invalidCode);
		int matching = 0;
		for(String c : candidates) {
			if(c.equals(validCode)) {
				matching ++;
				System.out.println(String.format("MATCH %s == %s", c, validCode));
			} else {
				System.out.println(String.format("NO M. %s == %s", c, validCode));
			}
		}
		assertTrue(matching > 0);
	}
	
	@Test
	public void testShorterCode() throws ParityCoderException {
		String validCode = "25KDPKCC";
		String invalidCode = "26KDPKCC";
		
		VCPostboxCoder coder = VCPostboxCoder.createEightDigitCoder();
		String[] candidates = coder.reconstructionCandidates(invalidCode);
		int matching = 0;
		for(String c : candidates) {
			if(c.equals(validCode)) {
				matching ++;
				System.out.println(String.format("MATCH %s == %s", c, validCode));
			} else {
				System.out.println(String.format("NO M. %s == %s", c, validCode));
			}
		}
		assertTrue(matching > 0);
	}
	
	@Test
	public void testShorterCodeWithNonDictionaryChar() throws ParityCoderException {
		String validCode = "25KDPKCC";
		String invalidCode = "2ZKDPKCC";
		
		VCPostboxCoder coder = new VCPostboxCoder(new DictionaryCoder(), new RandomReedSolomonParityCoder(3, 1, 1));
		String[] candidates = coder.reconstructionCandidates(invalidCode);
		int matching = 0;
		for(String c : candidates) {
			if(c.equals(validCode)) {
				matching ++;
				System.out.println(String.format("MATCH %s == %s", c, validCode));
			} else {
				System.out.println(String.format("NO M. %s == %s", c, validCode));
			}
		}
		assertTrue(matching > 0);
	}
	
	@Test
	public void testIntermediateCode() throws ParityCoderException {
		String validCode = "E3CR6CKK";
		String invalidCode = "E3CR7CKK";
		
		VCPostboxCoder coder = new VCPostboxCoder(new DictionaryCoder(), new RandomReedSolomonParityCoder(2, 2, 1));
		String[] candidates = coder.reconstructionCandidates(invalidCode);
		int matching = 0;
		for(String c : candidates) {
			if(c.equals(validCode)) {
				matching ++;
				System.out.println(String.format("MATCH %s == %s", c, validCode));
			} else {
				System.out.println(String.format("NO M. %s == %s", c, validCode));
			}
		}
		assertTrue(matching > 0);
	}
	
	@Test
	public void testIntermediateCode2() throws ParityCoderException {
		VCPostboxCoder coder = new VCPostboxCoder(new DictionaryCoder(), new RandomReedSolomonParityCoder(2, 1, 2));
		String validCode = "D44E2HDKHL51";
		String invalidCode = "D45E2HDKHL51";
		
		String[] candidates = coder.reconstructionCandidates(invalidCode);
		int matching = 0;
		for(String c : candidates) {
			if(c.equals(validCode)) {
				matching ++;
				System.out.println(String.format("MATCH %s == %s", c, validCode));
			} else {
				System.out.println(String.format("NO M. %s == %s", c, validCode));
			}
		}
		assertTrue(matching > 0);
	}

}
