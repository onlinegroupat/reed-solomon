package com.variocube.code;

import java.util.regex.Pattern;

import com.variocube.code.impl.DictionaryCoder;
import com.variocube.code.impl.RandomReedSolomonParityCoder;

/**
 * Is capable of generating, statically verifiying and correcting VARIOCUBE
 * postbox codes. It relys on two backend algorithms:
 * 
 * (1) An implementation of {@link BinaryToHumanReadableCoder} which is able
 *     to convert a byte[] to a human readable string and vice versa.
 * (2) A parity algorithm which can create, statically verify and correct byte[]
 *     encoded data.
 * 
 * @author matthias
 */
public class VCPostboxCoder {
	
	private final BinaryToHumanReadableCoder binaryCoder;
	private final ParityCoder parityCoder;
	
	public VCPostboxCoder(BinaryToHumanReadableCoder binaryToHumanReadableCoder, ParityCoder parityCoder) {
		this.binaryCoder = binaryToHumanReadableCoder;
		this.parityCoder = parityCoder;
	}
	
	public static VCPostboxCoder createEightDigitCoder() {
		return new VCPostboxCoder(new DictionaryCoder(), new RandomReedSolomonParityCoder(3, 1, 1));
	}
	
	public static VCPostboxCoder createSixteenDigitCoder() {
		return new VCPostboxCoder(new DictionaryCoder(), new RandomReedSolomonParityCoder(3, 1, 2));
	}
	
	public static VCPostboxCoder createDefault() {
		return createEightDigitCoder();
	}
	
	/**
	 * Generate a random code which contains some parity to check and fix broken codes
	 * 
	 * @return The generated random code as String
	 * @throws ParityCoderException when parity coding failed
	 */
	public String generateRandomCode() throws ParityCoderException {
		byte[] byteCode = this.parityCoder.generateRandomCode();
		return this.binaryCoder.encodeData(byteCode);
	}
	
	/**
	 * Check if a supplied code contains valid parity and thus can be assumed to be correct
	 * 
	 * @param code code to be checked
	 * @return if the code contains valid parity
	 * @throws ParityCoderException when parity could not be verified
	 */
	public boolean isValidCode(String code) throws ParityCoderException {
		byte[] byteCode = this.binaryCoder.decodeData(code);
		return this.parityCoder.isParityCorrect(byteCode);
	}
	
	/**
	 * Computes all reconstructed codes
	 * 
	 * @param code a potentially broken code
	 * @return all possible options to reconstruct the code
	 * @throws ParityCoderException when unexpected errors occur in the ParityCoder
	 */
	public String[] reconstructionCandidates(String code) throws ParityCoderException {
		if(this.isValidCode(code)) {
			return new String[] { code };
		}else {
			byte[] byteCode = this.binaryCoder.decodeData(code);
			byte[][] decodedMissingCandidates = this.parityCoder.decodeMissing(byteCode);
			String[] result = new String[decodedMissingCandidates.length];
			for(int i=0; i<decodedMissingCandidates.length; i++) {
				byte[] candidate = decodedMissingCandidates[i];
				result[i] = this.binaryCoder.encodeData(candidate);
			}
			return result;
		}
	}
	
	/**
	 * @return a compiled Regex Pattern for detecting codes of this coder
	 */
	public Pattern detectionPattern() {
		return Pattern.compile(this.binaryCoder.detectionPattern());
	}
	
}
