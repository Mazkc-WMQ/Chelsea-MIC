package upbox.aes;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import com.alibaba.fastjson.JSON;

import sun.misc.BASE64Decoder;

public class CipherHelper {

	/**
	 * BASE64 编码.
	 * 
	 * @param src
	 *            String inputed string
	 * @return String returned string
	 * @throws UnsupportedEncodingException
	 */
	public static String base64Encode(String src)
			throws UnsupportedEncodingException {
		return new String(
				org.apache.commons.codec.binary.Base64.encodeBase64(src
						.getBytes()), "UTF-8");
	}

	/**
	 * BASE64 编码(byte[]).
	 * 
	 * @param src
	 *            byte[] inputed string
	 * @return String returned string
	 */
	public static String base64Encode(byte[] src) {
		return new String(
				org.apache.commons.codec.binary.Base64.encodeBase64(src));
	}

	/**
	 * BASE64 解码.
	 * 
	 * @param src
	 *            String inputed string
	 * @return String returned string
	 */
	public static String base64Decode(String src) {
		try {
			return new String(
					org.apache.commons.codec.binary.Base64.decodeBase64(src));
		} catch (Exception ex) {
			return null;
		}

	}

	/**
	 * BASE64 解码(to byte[]).
	 * 
	 * @param src
	 *            String inputed string
	 * @return String returned string
	 */
	public static byte[] base64DecodeToBytes(String src) {
		try {
			return org.apache.commons.codec.binary.Base64.decodeBase64(src);
		} catch (Exception ex) {
			return null;
		}

	}

	/**
	 * SHA或MD5不可逆加密
	 * 
	 * @param input
	 * @param algorithm
	 *            MD5/SHA-1/SHA-256
	 * @return
	 * @throws Exception
	 */
	public static byte[] hash(byte[] input, String algorithm) throws Exception {
		java.security.MessageDigest alg = java.security.MessageDigest
				.getInstance(algorithm);
		alg.update(input);
		byte[] digest = alg.digest();
		return digest;
	}

	/**
	 * md5信息加密, 不可逆
	 * 
	 * @param input
	 *            需要MD5加密的字符数组
	 * @return 经过MD5加密的字符数组
	 * @throws Exception
	 */
	public static byte[] md5(byte[] input) throws Exception {
		return hash(input, "MD5");
	}

	/**
	 * md5信息加密, 不可逆(String)
	 * 
	 * @param input
	 *            需要MD5加密的字符串
	 * @return 经过MD5加密的字符串
	 * @throws Exception
	 */
	public static String md5(String input, String charset) {
		byte[] byteArray;
		try {
			byteArray = md5(input.getBytes(charset));
		} catch (Exception e) {
			return "";
		}

		return byte2Hex(byteArray);
	}

	/**
	 * MD5加密
	 * 
	 * @param input
	 * @return
	 */
	public static String md5(String input) {
		return md5(input, "utf-8");
	}

	/**
	 * SHA信息加密, 不可逆
	 * 
	 * @param input
	 *            需要SHA加密的字符数组
	 * @return 经过SHA加密的字符数组
	 * @throws Exception
	 */
	public static byte[] sha(byte[] input) throws Exception {
		return hash(input, "SHA-1");
	}

	/**
	 * SHA信息加密, 不可逆(String)
	 * 
	 * @param input
	 *            需要SHA加密的字符串
	 * @return 经过SHA加密的字符串
	 * @throws Exception
	 */
	public static String sha(String input, String charset) {
		byte[] byteArray;
		try {
			byteArray = sha(input.getBytes(charset));
		} catch (Exception e) {
			return "";
		}

		return byte2Hex(byteArray);
	}

	/**
	 * SHA加密
	 * 
	 * @param input
	 * @return
	 */
	public static String sha(String input) {
		return sha(input, "utf-8");
	}

	/**
	 * 转换字节数组为十六进制字符串
	 * 
	 * @param byteArray
	 * @return
	 */
	public static String byte2Hex(byte[] byteArray) {
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
	}

	public static String showByteArray(byte[] data) {
		if (null == data) {
			return null;
		}
		StringBuilder sb = new StringBuilder("{");
		for (byte b : data) {
			sb.append(b).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * 3-DES加密
	 * 
	 * @param String
	 *            src 要进行3-DES加密的String
	 * @param String
	 *            spkey分配的SPKEY
	 * @return String 3-DES加密后的String
	 */
	public static String get3DESEncrypt(String src, String spkey) {
		String requestValue = "";
		try {
			// 得到3-DES的密钥匙
			byte[] enKey = getEnKey(spkey);
			// 要进行3-DES加密的内容在进行/"UTF-16LE/"取字节
			byte[] src2 = src.getBytes("UTF-16LE");
			// 进行3-DES加密后的内容的字节
			byte[] encryptedData = Encrypt(src2, enKey);
			// 进行3-DES加密后的内容进行BASE64编码
			String base64String = base64Encode(encryptedData);
			// BASE64编码去除换行符后
			String base64Encrypt = filter(base64String);
			// 对BASE64编码中的HTML控制码进行转义的过程
			requestValue = getURLEncode(base64Encrypt);
			// System.out.println(requestValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requestValue;
	}

	/**
	 * 得到3-DES的密钥匙 根据根据需要，如密钥匙为24个字节，md5加密出来的是16个字节，因此后面补8个字节的0
	 * 
	 * @param String
	 *            原始的SPKEY
	 * @return byte[] 指定加密方式为md5后的byte[]
	 */
	private static byte[] getEnKey(String spKey) {
		byte[] desKey = null;
		try {
			byte[] desKey1 = md5byte(spKey);
			desKey = new byte[24];
			int i = 0;
			while (i < desKey1.length && i < 24) {
				desKey[i] = desKey1[i];
				i++;
			}
			if (i < 24) {
				desKey[i] = 0;
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return desKey;
	}

	/**
	 * 进行MD5加密
	 * 
	 * @param String
	 *            原始的SPKEY
	 * @return byte[] 指定加密方式为md5后的byte[]
	 */
	private static byte[] md5byte(String strSrc) {
		byte[] returnByte = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			returnByte = md5.digest(strSrc.getBytes("UTF8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnByte;
	}

	/**
	 * 3-DES加密
	 * 
	 * @param byte[] src 要进行3-DES加密的byte[]
	 * @param byte[] enKey 3-DES加密密钥
	 * @return byte[] 3-DES加密后的byte[]
	 */
	public static byte[] Encrypt(byte[] src, byte[] enKey) {
		byte[] encryptedData = null;
		try {
			DESedeKeySpec dks = new DESedeKeySpec(enKey);
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance("DESede");
			SecretKey key = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DESede");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			encryptedData = cipher.doFinal(src);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedData;
	}

	/**
	 * 去掉字符串的换行符号 base64编码3-DES的数据时，得到的字符串有换行符号 ，一定要去掉，否则uni-wise平台解析票根不会成功，
	 * 提示“sp验证失败”。在开发的过程中，因为这个问题让我束手无策， 一个朋友告诉我可以问联通要一段加密后 的文字，然后去和自己生成的字符串比较，
	 * 这是个不错的调试方法。我最后比较发现我生成的字符串唯一不同的 是多了换行。 我用c#语言也写了票根请求程序，没有发现这个问题。
	 *
	 */
	private static String filter(String str) {
		String output = null;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			int asc = str.charAt(i);
			if (asc != 10 && asc != 13)
				sb.append(str.subSequence(i, i + 1));
		}
		output = new String(sb);
		return output;
	}

	/**
	 * 对字符串进行URLDecoder.encode(strEncoding)编码
	 * 
	 * @param String
	 *            src 要进行编码的字符串
	 *
	 * @return String 进行编码后的字符串
	 */
	public static String getURLEncode(String src) {
		String requestValue = "";
		try {
			requestValue = URLEncoder.encode(src, "UTF8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requestValue;
	}

	/**
	 * 3-DES解密
	 * 
	 * @param String
	 *            src 要进行3-DES解密的String
	 * @param String
	 *            spkey分配的SPKEY
	 * @return String 3-DES加密后的String
	 */
	public static String get3DESDecrypt(String src, String spkey) {
		String requestValue = "";
		try {
			// 得到3-DES的密钥匙
			// URLDecoder.decodeTML控制码进行转义的过程
			String URLValue = getURLDecoderdecode(src);
			// 进行3-DES加密后的内容进行BASE64编码
			BASE64Decoder base64Decode = new BASE64Decoder();
			byte[] base64DValue = base64Decode.decodeBuffer(URLValue);
			// 要进行3-DES加密的内容在进行/"UTF-16LE/"取字节
			requestValue = deCrypt(base64DValue, spkey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requestValue;
	}

	/**
	 * 对字符串进行URLDecoder.decode(strEncoding)解码
	 * 
	 * @param String
	 *            src 要进行解码的字符串
	 *
	 * @return String 进行解码后的字符串
	 */
	public static String getURLDecoderdecode(String src) {
		String requestValue = "";
		try {
			requestValue = URLDecoder.decode(src, "UTF8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requestValue;
	}

	/**
	 *
	 * 进行3-DES解密（密钥匙等同于加密的密钥匙）。
	 * 
	 * @param byte[] src 要进行3-DES解密byte[]
	 * @param String
	 *            spkey分配的SPKEY
	 * @return String 3-DES解密后的String
	 */
	public static String deCrypt(byte[] debase64, String spKey) {
		String strDe = null;
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("DESede");
			byte[] key = getEnKey(spKey);
			DESedeKeySpec dks = new DESedeKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance("DESede");
			SecretKey sKey = keyFactory.generateSecret(dks);
			cipher.init(Cipher.DECRYPT_MODE, sKey);
			byte ciphertext[] = cipher.doFinal(debase64);
			strDe = new String(ciphertext, "UTF-16LE");
		} catch (Exception ex) {
			strDe = "";
			ex.printStackTrace();
		}
		return strDe;
	}
	
	public static void main(String[] args) {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("userId", "abcd123");
		String s = JSON.toJSONString(map);
		System.out.println(CipherHelper.get3DESEncrypt(s, CipherHelper.md5("20160901UPBOX")));
		System.out.println(CipherHelper.get3DESDecrypt(CipherHelper.get3DESEncrypt(s, CipherHelper.md5("20160901UPBOX")), CipherHelper.md5("20160901UPBOX")));
	}
}
