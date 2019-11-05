package com.gram.gram_landlord.utils;


import android.util.Base64;
import com.orhanobut.logger.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.Charset;
import java.security.*;

public class EncryptUtil {
    private static final String RSA_ALGORITHM = "rsa";
    private static final String DES_ALGORITHM = "des";
    private static final String MD5_ALGORITHM = "md5";
    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    private static final String DES_KEY = "zhangxiaorong is god of gamblers";
    private static final int KEY_SIZE = 1024;
    private static KeyPair keyPair;
    private static SecureRandom random;
    private static MessageDigest md5;


    private EncryptUtil() {}

    static {
        try {
            md5 = MessageDigest.getInstance(MD5_ALGORITHM);
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            Logger.e(e.getMessage());
        }
        random = new SecureRandom();
    }

    /**
     * 加密解密处理流程
     * @param processData 待处理的数据
     * @param key 提供的密钥
     * @param opsMode 工作模式
     * @param algorithm 使用的算法
     */
    private static byte[] processCipher(byte[] processData, Key key, int opsMode, String algorithm) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(opsMode,key,random);
            return cipher.doFinal(processData);
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return null;
    }

    /**
     * 根据key创建SecretKey
     */
    private static SecretKey createSecretKey(String key) {
        SecretKey secretKey = null;
        try {
            DESKeySpec keySpec = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES_ALGORITHM);
            secretKey = keyFactory.generateSecret(keySpec);
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return secretKey;
    }

    /**
     * 功能简述: 创建私钥，用于RSA非对称加密.
     */
    private static PrivateKey createPrivateKey() {
        return keyPair.getPrivate();
    }

    /**
     * 功能简述: 创建公钥，用于RSA非对称加密.
     */
    private static PublicKey createPublicKey() {
        return keyPair.getPublic();
    }

    /**
     * RSA解密
     * @param plainData 加密数据
     * @param key 密钥
     */
    private static byte[] encryptRSA(byte[] plainData, Key key) {
        return processCipher(plainData, key, Cipher.ENCRYPT_MODE, RSA_ALGORITHM);
    }

    /**
     * RSA解密
     * @param cipherData 密文数据
     * @param key 密钥
     */
    private static byte[] decryptRSA(byte[] cipherData, Key key) {
        return processCipher(cipherData, key, Cipher.DECRYPT_MODE, RSA_ALGORITHM);
    }

    /**
     * md5单向加密，md5是hash算法，不可解密
     */
    private static String encryptMD5(String plainText) {
        byte[] cipherData = md5.digest(plainText.getBytes());
        StringBuilder builder = new StringBuilder();
        for(byte cipher : cipherData) {
            String toHexStr = Integer.toHexString(cipher & 0xff);
            builder.append(toHexStr.length() == 1?"0" + toHexStr : toHexStr);
        }
        return builder.toString();
    }

    /**
     * BASE64加密
     * @param plainData 明文数据
     * @return 加密后的文本
     */
    private static String encryptBASE64(byte[] plainData) {
        return bytes2String(Base64.encode(plainData, Base64.DEFAULT));
    }

    /**
     * BASE64解密
     * @param cipherText 密文文本
     * @return 解密后的数据
     */
    private static byte[] decryptBASE64(String cipherText) {
        return Base64.decode(cipherText.getBytes(), Base64.DEFAULT);
    }

    /**
     * DES加密
     * @param plainData 明文数据
     * @param key 加密密钥
     */
    private static byte[] encryptDES(byte[] plainData, String key) {
        return processCipher(plainData, createSecretKey(key), Cipher.ENCRYPT_MODE, DES_ALGORITHM);
    }

    /**
     * DES解密
     * @param cipherData 密文数据
     * @param key 解密密钥
     */
    private static byte[] decryptDES(byte[] cipherData, String key) {
        return processCipher(cipherData, createSecretKey(key), Cipher.DECRYPT_MODE, DES_ALGORITHM);
    }

    /**
     * 功能简述: 使用私钥对加密数据创建数字签名.
     * @param cipherData     已经加密过的数据
     * @param privateKey    私钥
     */
    private static byte[] createSignature(byte[] cipherData, PrivateKey privateKey) {
        try {
            Signature signature  = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(cipherData);
            return signature.sign();
        }
        catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return null;
    }

    /**
     * 功能简述: 使用公钥对数字签名进行验证.
     * @param signData  数字签名
     * @param publicKey 公钥
     */
    private static boolean verifySignature(byte[] cipherData, byte[] signData, PublicKey publicKey) {
        try {
            Signature signature  = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(cipherData);
            return signature.verify(signData);
        }
        catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return false;
    }

    private static String bytes2String(byte[] a) {
        return new String(a, Charset.forName("UTF-8"));
    }

    private static byte[] string2Bytes(String s) {
        return s.getBytes(Charset.forName("UTF-8"));
    }

    public static String passwordEncryptDES(String s) {
        return encryptBASE64(encryptDES(string2Bytes(s), DES_KEY));
    }

    public static String passwordDecryptDES(String s) {
        return bytes2String(decryptDES(decryptBASE64(s), DES_KEY));
    }

    public static void main(String[] args) {
        String password1 = "yangping233672";
        String password2 = "yangshu233672";
        String password3 = "zhangxiaorong233672";
        String password1Encrypt = passwordEncryptDES(password1);
        String password2Encrypt = passwordEncryptDES(password2);
        String password3Encrypt = passwordEncryptDES(password3);
        System.out.println(password1Encrypt);
        System.out.println(password2Encrypt);
        System.out.println(password3Encrypt);
        System.out.println(bytes2String(decryptDES(decryptBASE64(password1Encrypt), DES_KEY)));
        System.out.println(bytes2String(decryptDES(decryptBASE64(password2Encrypt), DES_KEY)));
        System.out.println(bytes2String(decryptDES(decryptBASE64(password3Encrypt), DES_KEY)));

//        System.out.println(CommonUtil.bytes2String(decryptDES(decryptBASE64("44X4bFlcamP9+0n5PvbPsQ=="), RequestKey.DES_KEY)));
    }
}
