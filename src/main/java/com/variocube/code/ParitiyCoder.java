package com.variocube.code;

public interface ParitiyCoder {

	/**
	 * Adds parity data to the passed in byte[] according to the configured this.numberOfShards, this.numberOfParity and this.shardLength 
	 *  
	 * @param bytes the byte[] over which to operate
	 * @throws ParityCoderException 
	 */
	void encodeParity(byte[] data) throws ParityCoderException;
	
	/**
	 * Validates a given code for parity
	 * 
	 * @param data a data block with parity
	 * @return iff the partiy bit is valid
	 * @throws ParityCoderException 
	 */
	boolean isParityCorrect(byte[] data) throws ParityCoderException;
	
	/**
	 * Given an input data block which is assumed to have broken parity
	 * will iterate over all possible broken shard configurations and 
	 * try to restore the data.
	 * 
	 * @param data a datablock with potentially broken parity
	 * @throws ParityCoderException
	 */
	byte[][] decodeMissing(byte[] data) throws ParityCoderException;
	
	/**
	 * Generates a random code and adds parity using the ReedSolomon parity algorithm
	 * 
	 * @return a random code with parity in the last this.numberOfParity * this.shardLength bytes
	 * @throws ParityCoderException 
	 */
	byte[] generateRandomCode() throws ParityCoderException;

}