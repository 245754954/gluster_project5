package cn.edu.nudt.hycloudclient.crypto;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class AES {
	public static int AES_Block_Size = 16; // in bytes
	
	
	/**
	 * Encrypts a string with a given key
	 * @param content
	 * @param intKey
	 * @return cipehrtext in bytes
	 */
	public static byte[] encrypt(String content, BigInteger intKey) {
		try {
			Cipher cipher = initAESCipher(intKey, Cipher.ENCRYPT_MODE);
			byte[] byteContent = content.getBytes("utf-8");
			byte[] result = cipher.doFinal(byteContent);
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param content
	 * @param intKey
	 * @return
	 */
	public static byte[] encrypt(byte[] content, int len, BigInteger intKey) {
		try {
			Cipher cipher = initAESCipher(intKey, Cipher.ENCRYPT_MODE);
			byte[] result = cipher.doFinal(content, 0, len);
			return result;
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Decrypts bytes
	 * @param content
	 * @param intKey
	 * @return
	 */
	public static byte[] decrypt(byte[] content, BigInteger intKey) {
		try {
			Cipher cipher = initAESCipher(intKey, Cipher.DECRYPT_MODE);
			byte[] result = cipher.doFinal(content);
			return result;
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Encrypts a file in segments
	 * 
	 * @param sourceFile
	 * @param outputFile
	 * @param segmentSize in bytes
	 * @return
	 */
	public static File encryptFileInSegments(File sourceFile, File outputFile, List<BigInteger> segmentKeys, int segmentSize) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(sourceFile);
			outputStream = new FileOutputStream(outputFile);
			
			byte[] inBuf = new byte[segmentSize];
			byte[] outBuf = new byte[segmentSize + AES_Block_Size];
			int i = 0;
			int nread = 0;
			while( (nread = inputStream.read(inBuf)) != -1) {
				outBuf = encrypt(inBuf, nread, segmentKeys.get(i));
				
//				System.out.println(inBuf.length + ", " + outBuf.length);
				outputStream.write(outBuf, 0, outBuf.length);
				outputStream.flush();
				i++;
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}finally {
			
		}
		return outputFile;
	}
	
	/**
	 * Decrypts a file that encrypted in segments. 
	 * <p>Note that the size of the ciphertext of a segment equals the size of the plaintext plus 16 bytes (the IV)
	 * 
	 * @param sourceFile
	 * @param outputFile
	 * @param segmentKeys
	 * @param segmentSize
	 * @return
	 */
	public static File DecryptFileInSegments(File sourceFile, File outputFile, List<BigInteger> segmentKeys, int segmentSize) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(sourceFile);
			outputStream = new FileOutputStream(outputFile);
			
			byte[] inBuf = new byte[segmentSize + AES_Block_Size];
			byte[] outBuf = new byte[segmentSize];
			int i = 0;
			while( (inputStream.read(inBuf)) != -1) {
				outBuf = decrypt(inBuf, segmentKeys.get(i));
				
				outputStream.write(outBuf, 0, outBuf.length);
				outputStream.flush();
				i++;
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}finally {
			
		}
		return outputFile;
	}
	
	/**
	 * Decrypt the given file with the deleted segments undecrypted.
	 * @param sourceFile
	 * @param outputFile
	 * @param delSegments
	 * @param segmentsKeys
	 * @param segmentSize
	 * @return
	 */
	public static File DecryptFileInSegments(File sourceFile, File outputFile, List<Integer> delSegments, List<BigInteger> segmentsKeys, int segmentSize) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(sourceFile);
			outputStream = new FileOutputStream(outputFile);
			
			byte[] inBuf = new byte[segmentSize + AES_Block_Size];
			byte[] outBuf = new byte[segmentSize];
			int keyIdx = 0;
			int segIdx = 0;
			while( (inputStream.read(inBuf)) != -1) {
				if(!delSegments.contains(segIdx)) {
					outBuf = decrypt(inBuf, segmentsKeys.get(keyIdx));
				
					outputStream.write(outBuf, 0, outBuf.length);
					outputStream.flush();
					keyIdx++;
				}
				segIdx++;
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}finally {
			
		}
		return outputFile;
	}
	
	/**
	 * Encrypts a file
	 * @param sourceFile
	 * @param outputFile
	 * @param intKey
	 * @return
	 */
	public static File encryptFile(File sourceFile, File outputFile, BigInteger intKey) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(sourceFile);
			outputStream = new FileOutputStream(outputFile);
			
			Cipher cipher = initAESCipher(intKey, Cipher.ENCRYPT_MODE);
			// using stream cipher
			CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
			byte[] cache = new byte[1024];
			int nRead = 0;
			while ((nRead = cipherInputStream.read(cache)) != -1) {
				outputStream.write(cache, 0, nRead);
				outputStream.flush();
			}
			cipherInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace(); 
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace(); 
			}
		}
		return outputFile;
	}
 
 
	/**
	 * Decrypts an encrypted file
	 * @param sourceFile
	 * @param outputFile
	 * @param intKey
	 * @return file in plaintext
	 */
	public static File decryptFile(File sourceFile, File outputFile, BigInteger intKey) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			Cipher cipher = initAESCipher(intKey, Cipher.DECRYPT_MODE);
			inputStream = new FileInputStream(sourceFile);
			outputStream = new FileOutputStream(outputFile);
			CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
			byte[] buffer = new byte[1024];
			int r;
			while ((r = inputStream.read(buffer)) >= 0) {
				cipherOutputStream.write(buffer, 0, r);
			}
			cipherOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace(); 
			}
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return outputFile;
	}

	/**
	 * Initializes an AES cipher
	 * 
	 * @param key
	 * @param cipherMode
	 * - the operation mode of this cipher (this is one of the following: ENCRYPT_MODE, DECRYPT_MODE, WRAP_MODE or UNWRAP_MODE).
	 * @return 
	 * AES cipher initialized with given key and operation mode.
	 */
	public static Cipher initAESCipher(BigInteger key, int cipherMode) {
		Cipher cipher = null;
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			byte[] buffer = sha.digest(key.toByteArray());
			buffer = Arrays.copyOf(buffer, 16); // AES block size is 16 
			SecretKeySpec secretKey = new SecretKeySpec(buffer, "AES");
			
			cipher = Cipher.getInstance("AES");
			cipher.init(cipherMode, secretKey);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace(); 
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return cipher;
	}
	
//	public static void main(String[] args) {
//		File file = new File("/root/workspace/yhbigdata/TestDir/LocalDir/testfile.txt");
//		BigInteger key = new BigInteger(DeletionConfig.XBits, new SecureRandom());
//		
////		try {
////			Cipher cipher = initAESCipher(key, Cipher.ENCRYPT_MODE);
////			byte[] buffer = cipher.doFinal(filepath.getBytes());
////			
////			
////			 cipher = initAESCipher(key, Cipher.DECRYPT_MODE);
////			 byte[] outBuffer = cipher.doFinal(buffer);
////			 Log.print(outBuffer.toString());
////		} catch (IllegalBlockSizeException | BadPaddingException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		
//		Log.print("getPath: " + file.getPath());
//		Log.print("getParent: " + file.getParent());
//		Log.print("getName: " + file.getName());
//		Log.print("getParentFile: " + file.getParentFile());
//		
//		int segSize = 38 * 1024;
//		int inBufferSize = segSize;
//		byte[] inBuffer = new byte[inBufferSize];
//		byte[] outBuffer;
//		
//		FileInputStream fis;
//		FileOutputStream fos;
//		Cipher cipher = initAESCipher(key, Cipher.ENCRYPT_MODE);
//		try {
//			fis = new FileInputStream(file);
//			fos = new FileOutputStream(file.getParent() + "/Encrypted_" + file.getName());
//			int nread = 0;
//			while( (nread = fis.read(inBuffer)) != -1) {
//				outBuffer = cipher.doFinal(inBuffer, 0, nread);
//				fos.write(outBuffer);
//			}
//			fis.close();
//			fos.close();
//		} catch (IOException | IllegalBlockSizeException | BadPaddingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		inBufferSize = segSize + AES.AES_Block_Size;
//		inBuffer = new byte[inBufferSize];
//		cipher = initAESCipher(key, Cipher.DECRYPT_MODE);
//		try {
//			fis = new FileInputStream(file.getParent() + "/Encrypted_" + file.getName());
//			fos = new FileOutputStream(file.getParent() + "/Retrieved_" + file.getName());
//			int nread = 0;
//			while( (nread = fis.read(inBuffer)) != -1) {
//				outBuffer = cipher.doFinal(inBuffer, 0, nread);
//				fos.write(outBuffer);
//			}
//			fis.close();
//			fos.close();
//		} catch (IOException | IllegalBlockSizeException | BadPaddingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
}
