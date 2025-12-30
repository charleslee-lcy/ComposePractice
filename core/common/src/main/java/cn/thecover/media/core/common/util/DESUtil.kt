package cn.thecover.media.core.common.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.Key;
import java.security.SecureRandom;

/**
 * des工具
 *
 */
public class DESUtil {
    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "DES";
    /**
     * 加密/解密算法-工作模式-填充模式
     */
    private static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";
    /**
     * 默认编码
     */
    private static final String CHARSET = "utf-8";

    /**
     * 签名算法
     */
    public static final String SIGN_ALGORITHMS = "SHA1PRNG";

    /**
     * 简单key
     */
    public static final String SIMPLE_KEY = "682864A5";

    /**
     * 生成key
     *
     * @param password
     * @return
     * @throws Exception
     */
    private static Key generateKey(String password) throws Exception {
        DESKeySpec dks = new DESKeySpec(password.getBytes(CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(dks);
    }


    /**
     * DES加密字符串
     *
     * @param key 加密密码，长度不能够小于8位
     * @param data 待加密字符串
     * @return 加密后内容
     */
    public static String encrypt(String key, String data) {
        if (key== null || key.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        if (data == null){
            return null;
        }
        try {
            Key secretKey = generateKey(key);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            SecureRandom random = SecureRandom.getInstance(SIGN_ALGORITHMS);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
            byte[] bytes = cipher.doFinal(data.getBytes(CHARSET));
            return parseByte2HexStr(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * DES解密字符串
     *
     * @param key 解密密码，长度不能够小于8位
     * @param data 待解密字符串
     * @return 解密后内容
     */
    public static String decrypt(String key, String data) {
        if (key== null || key.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        if (data == null){
            return null;
        }
        try {
            Key secretKey = generateKey(key);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            SecureRandom random = SecureRandom.getInstance(SIGN_ALGORITHMS);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, random);
            return new String(cipher.doFinal(parseHexStr2Byte(data)), CHARSET);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将二进制转换成16进制
     */
    public static String parseByte2HexStr(byte[] buf) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr==null||hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 简单加密
     * @return
     */
    public static String simpleEncrypt(String data){
        return encrypt(SIMPLE_KEY,data);
    }

    /**
     * 简单解密
     * @return
     */
    public static String simpleDecrypt(String data){
        return decrypt(SIMPLE_KEY,data);
    }

    public static void main(String[] args) {
        String key="12345678";
        String msg="111111";
        String res="cc2739c64fefb199";
//        System.out.println(DESUtil.encrypt(key,msg));
//        System.out.println(DESUtil.decrypt(key,res));
//        String s = simpleEncrypt(msg);
//        System.out.println(s);
//        System.out.println(simpleDecrypt(s));

        System.out.println(simpleDecrypt("5098098c32b3867c"));
        System.err.println(simpleEncrypt("15813733781"));
    }
}
