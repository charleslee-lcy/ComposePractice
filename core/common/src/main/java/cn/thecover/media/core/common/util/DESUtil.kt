package cn.thecover.media.core.common.util

import java.security.Key
import java.security.SecureRandom
import java.util.Locale
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

/**
 * des工具
 *
 */
object DESUtil {
    /**
     * 密钥算法
     */
    private const val ALGORITHM = "DES"

    /**
     * 加密/解密算法-工作模式-填充模式
     */
    private const val CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding"

    /**
     * 默认编码
     */
    private const val CHARSET = "utf-8"

    /**
     * 签名算法
     */
    const val SIGN_ALGORITHMS: String = "SHA1PRNG"

    /**
     * 简单key
     */
    const val SIMPLE_KEY: String = "682864A5"

    /**
     * 生成key
     *
     * @param password
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun generateKey(password: String): Key? {
        val dks = DESKeySpec(password.toByteArray(charset(CHARSET)))
        val keyFactory = SecretKeyFactory.getInstance(ALGORITHM)
        return keyFactory.generateSecret(dks)
    }


    /**
     * DES加密字符串
     *
     * @param key 加密密码，长度不能够小于8位
     * @param data 待加密字符串
     * @return 加密后内容
     */
    fun encrypt(key: String, data: String?): String? {
        if (key.length < 8) {
            throw RuntimeException("加密失败，key不能小于8位")
        }
        if (data == null) {
            return null
        }
        try {
            val secretKey = generateKey(key)
            val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
            val random = SecureRandom.getInstance(SIGN_ALGORITHMS)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, random)
            val bytes = cipher.doFinal(data.toByteArray(charset(CHARSET)))
            return parseByte2HexStr(bytes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * DES解密字符串
     *
     * @param key 解密密码，长度不能够小于8位
     * @param data 待解密字符串
     * @return 解密后内容
     */
    fun decrypt(key: String, data: String?): String? {
        if (key.length < 8) {
            throw RuntimeException("加密失败，key不能小于8位")
        }
        if (data == null) {
            return null
        }
        try {
            val secretKey = generateKey(key)
            val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
            val random = SecureRandom.getInstance(SIGN_ALGORITHMS)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, random)
            return String(cipher.doFinal(parseHexStr2Byte(data)), charset(CHARSET))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 将二进制转换成16进制
     */
    fun parseByte2HexStr(buf: ByteArray): String {
        val sb = StringBuffer()
        for (i in buf.indices) {
            var hex = Integer.toHexString(buf[i].toInt() and 0xFF)
            if (hex.length == 1) {
                hex = '0'.toString() + hex
            }
            sb.append(hex.uppercase(Locale.getDefault()))
        }
        return sb.toString()
    }

    /**
     * 将16进制转换为二进制
     */
    fun parseHexStr2Byte(hexStr: String?): ByteArray? {
        if (hexStr == null || hexStr.isEmpty()) {
            return null
        }
        val result = ByteArray(hexStr.length / 2)
        for (i in 0..<hexStr.length / 2) {
            val high = hexStr.substring(i * 2, i * 2 + 1).toInt(16)
            val low = hexStr.substring(i * 2 + 1, i * 2 + 2).toInt(16)
            result[i] = (high * 16 + low).toByte()
        }
        return result
    }

    /**
     * 简单加密
     * @return
     */
    fun simpleEncrypt(data: String?): String {
        return encrypt(SIMPLE_KEY, data) ?: ""
    }

    /**
     * 简单解密
     * @return
     */
    fun simpleDecrypt(data: String?): String? {
        return decrypt(SIMPLE_KEY, data)
    }
}
