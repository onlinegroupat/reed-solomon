package com.variocube.code.impl;

import com.backblaze.erasure.ReedSolomon;
import com.variocube.code.ParityCoderException;

/**
 * Helper class which is capable of generating, verifiying and correcting
 * Reed-Solomon parity coded short codes. 
 * 
 * @author matthias
 */
public class ReedSolomonParityCoder {
	
	private final int numberOfShards;
	private final int numberOfParity;
	private final int shardLength;
	
	private final ReedSolomon codec;
	
	/**
	 * Generates a new coder
	 * 
	 * @param numberOfShards
	 * @param numberOfParity
	 * @param shardLength
	 */
	public ReedSolomonParityCoder(int numberOfShards, int numberOfParity, int shardLength) {
		this.numberOfShards = numberOfShards;
		this.numberOfParity = numberOfParity;
		this.shardLength = shardLength;
		this.codec = ReedSolomon.create(this.numberOfShards, this.numberOfParity);
	}
	
	/**
	 * @return the number of total blocks this code is made of
	 */
	public int totalBlocks() {
		return this.numberOfShards + this.numberOfParity;
	}
	
	/**
	 * @return the number of bytes which can be encoded
	 */
	public int dataLength() {
		return this.numberOfShards * this.shardLength;
	}
	
	/**
	 * @return the number of bytes the codes makes up with parity
	 */
	public int codeLength() {
		return this.dataLength() + this.numberOfParity * this.shardLength;
	}
	
	/* (non-Javadoc)
	 * @see com.variocube.code.impl.ParitiyCoder#encodeParity(byte[])
	 */
	public void encodeParity(byte[] data) throws ParityCoderException {
		byte[][] shards = this.arrayToMatrix(data);
		this.codec.encodeParity(shards, 0, this.shardLength);
		this.copyMatrixToArray(shards, data);
	}
	
	/* (non-Javadoc)
	 * @see com.variocube.code.impl.ParitiyCoder#hasValidParity(byte[])
	 */
	public boolean isParityCorrect(byte[] data) throws ParityCoderException {
		byte[][] shards = this.arrayToMatrix(data);
		return this.codec.isParityCorrect(shards, 0, this.shardLength);
	}
	
	public static int computeDistance(byte[] decoded, byte[] data) {
		int sum = 0;
		for(int i=0; i<decoded.length; i++) {
			sum +=  decoded[i] != data[i] ? 1 : 0;
		}
		return sum;
	}
	
	/* (non-Javdoc)
	 * @see com.variocube.code.impl.ParityCoder#decodeMissing(byte[], int) 
	 */
	public byte[] decodeMissing(byte[] data, int defectiveShard) throws ParityCoderException {
		if(this.isParityCorrect(data)) {
			return  data; // nothing to do
		}
		boolean[] present = this.generatePresentArray(this.numberOfShards + this.numberOfParity, defectiveShard);
		byte[][] shards = this.arrayToMatrix(data);
		this.codec.decodeMissing(shards, present, 0, this.shardLength);
		this.copyMatrixToArray(shards, data);
		return data;
	}
	
	/* (non-Javadoc)
	 * @see com.variocube.code.impl.ParitiyCoder#decodeMissing(byte[])
	 */	
	public byte[][] decodeMissing(byte[] data) throws ParityCoderException {
		if(this.isParityCorrect(data)) {
			return new byte[][] { data }; // nothing to do
		}
		
		byte[][] candidates = new byte[this.totalBlocks()][data.length];
		for(int i=0;i<this.totalBlocks(); i++) {
			System.arraycopy(data, 0, candidates[i], 0, data.length);
			this.decodeMissing(candidates[i], i);
		}
		return candidates;
	}
	
	private boolean[] generatePresentArray(int length, int posMissing) {
		boolean[] present = new boolean[length];
		for(int i=0; i<present.length; i++) {
			present[i] = i != posMissing;
		}
		return present;
	}
	
	/**
	 * Copy data from an array over to a matrix usable for the ReedSolomon class
	 * 
	 * @param arr an array
	 * @return
	 * @throws ParityCoderException 
	 */
	public byte[][] arrayToMatrix(byte[] arr) throws ParityCoderException {
		if(arr.length != (this.numberOfShards + this.numberOfParity) * this.shardLength) {
			throw new ParityCoderException(String.format("Only arrays of exactly %d length can be converted to a matrix of %d X %d size", this.codeLength(), this.numberOfShards + this.numberOfParity, this.shardLength));
		}
		byte[][] matrix = new byte[this.numberOfShards + this.numberOfParity][this.shardLength];
		int i = 0;
		for(int l=0; l<matrix.length; l++) {
			byte[] line = matrix[l];
			for(int c=0; c<line.length; c++) {
				line[c] = arr[i++]; 
			}
		}
		return matrix;
	}
	
	/**
	 * Copies over the data line by line from the matrix to the array
	 * 
	 * @param matrix matrix holding the data
	 * @param arr target array
	 */
	public void copyMatrixToArray(byte[][] matrix, byte[] arr) {
		int i = 0;
		for(int l=0; l<matrix.length; l++) {
			byte[] line = matrix[l];
			for(int c=0; c<line.length; c++) {
				arr[i++] = line[c];
			}
		}
	}

}
