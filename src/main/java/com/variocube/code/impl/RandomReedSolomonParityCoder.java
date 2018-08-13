package com.variocube.code.impl;

import java.security.SecureRandom;

import com.variocube.code.ParityCoder;
import com.variocube.code.ParityCoderException;

public class RandomReedSolomonParityCoder extends ReedSolomonParityCoder implements ParityCoder {

	private static SecureRandom random = new SecureRandom();
	
	public RandomReedSolomonParityCoder(int numberOfShards, int numberOfParity, int shardLength) {
		super(numberOfShards, numberOfParity, shardLength);
	}
	
	public RandomReedSolomonParityCoder() {
		this(DEFAULT_NUMBER_OF_SHARDS, DEFAULT_PARITY, DEFAULT_SHARD_LENGTH);
	}
	
	/**
	 * Generates a random code and adds parity using the ReedSolomon parity algorithm
	 * 
	 * @return a random code with parity in the last this.numberOfParity * this.shardLength bytes
	 * @throws ParityCoderException 
	 */
	@Override
	public byte[] generateRandomCode() throws ParityCoderException {
		byte[] code = new byte[this.codeLength()];
		random.nextBytes(code);
		this.encodeParity(code);
		return code;
	}

}
