package org.jeecg.common.util.encryption;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.codec.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

/**
 * AES 加密
 */
public class AesEncryptUtil {

    //使用AES-128-CBC加密模式，key需要为16位,key和iv可以相同！
    private static String KEY = EncryptedString.key;
    private static String IV = EncryptedString.iv;

    /**
     * 加密方法
     * @param data  要加密的数据
     * @param key 加密key
     * @param iv 加密iv
     * @return 加密的结果
     * @throws Exception
     */
    public static String encrypt(String data, String key, String iv) throws Exception {
        try {

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");//"算法/模式/补码方式"NoPadding PkcsPadding
            int blockSize = cipher.getBlockSize();

            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return Base64.encodeToString(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密方法
     * @param data 要解密的数据
     * @param key  解密key
     * @param iv 解密iv
     * @return 解密的结果
     * @throws Exception
     */
    public static String desEncrypt(String data, String key, String iv) throws Exception {
        try {
			byte[] encrypted1 = Base64.decode(data);

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用默认的key和iv加密
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) throws Exception {
        return encrypt(data, KEY, IV);
    }

    /**
     * 使用默认的key和iv解密
     * @param data
     * @return
     * @throws Exception
     */
    public static String desEncrypt(String data) throws Exception {
        return desEncrypt(data, KEY, IV);
    }


    /**
     * 微信支付退款解析 对加密串B做AES-256-ECB解密（PKCS7Padding）
     * @param reqInfo
     * @param apiKey
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String descrypt(String reqInfo, String apiKey) throws NoSuchPaddingException, NoSuchAlgorithmException,
            NoSuchProviderException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] reqInfoB = Base64.decode(reqInfo);
        String key_ = DigestUtils.md5Hex(apiKey).toLowerCase();
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key_.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        return new String(cipher.doFinal(reqInfoB));
    }



    /**
     * 测试
     */
    public static void main(String args[]) throws Exception {
//        String reqInfo = "hCB/lHhqvVO/j9S8IB8zttQGK+5ZIMdLfZJZArrLvMbk3Shb/iOfiCFwSe2B9eJyjZ4yQa+ENGj59kH67ua9ub2d/5le8pB36etMCZCRt0qy8yVR8ONd6wNgBegbTpha1ORdo6BMOwiGEdAqrX+Led8tkk5qLzdUKJHFWFbIWfdJbRy0v3OW6hr9OPHt1gMJhqCJnNbbjX1KMdgSbuoVMiypgl3KsQ3hTsJCFxk6do9eY9E7K8gg3NuEDhpRvZBpzi+W/1Xvm7ckqJ4Uhs2dF/ABBjT3MT3b5iutaEftSYxzieMBc5V6hz0XE4Hwq9U8UnprXLkv0pyd0OeYhGi7PVkz6YrEs895RD6gkU1A2b6ZaW/vmbJOQs97xO9zPMRzsiRFpwn7WnELuKh4GG5xy4R9RSba0IRiFtVPCr26rt5oYObu17lNsjl0RuOy/q0e6ULaNEDqffnYWI6PbKH+vIRxwwsn8MlXfpE09ZoWO8NvLnZ97IqzWOUlPSp3MOU7+sDtIfropyHc4KG4yLFaIHSspaEcUBs8mg0iN4hWQ/ZkwiTZS+FrQVily9S5zKFIIYiAleF0csrRNRGIH7tJSOn47V6QVQq7G6H6ZLDxrLrUkRrYuCmYDuu0xGTxAdUEqiY3GLtMp8WSTWS1sPylfiPnHaOaZd8W2op6gTgH2VJAOXeGM+R3zev8yM3k8MlXKABMwkw1XlxyafbiDwKuP3OlM+N55uIqbsU9ijx5aABuArA7bM1qsbQIsfc/eK9TUczLIrQ5ug2xhIBonWmcgrFKB19uYeJufAZIZI8c03tXjDRCdvD+NAn8Zv1YC1B9jtw9Af3olVKquGrg87Iln8+mr6Xi4tIHx+yJifMazozxsP6mEbk0zxm5usf1JMrxmu7DFn35J6/W35QmqbO4RM8C0YIKFj/dm7buloOGRZqIJD2WnVFE28fkplnxWMVMG8rvj8gfHoY++jy0BLV1ayIAnMuK6FLYpLEx5ar0jThhSZJ6WvMmlDhjn8/vYyWiEsu8Xd1r8I56NWZAr05odZzNoQdKkSG9MY66Z2ah79dcRit0EgoI42U7z+bHFzHj";
//        String keyMd5 = MD5Util.MD5Encode("12a0bbhua0b0f4e1fpicf84d351cfang", null).toLowerCase();
//        System.out.println(descrypt(reqInfo,keyMd5));
//        System.out.println(descrypt(reqInfo,"12a0bbhua0b0f4e1fpicf84d351cfang"));

//        String test1 = "sa";
//        String test =new String(test1.getBytes(),"UTF-8");
//        String data = null;
//        String key =  KEY;
//        String iv = IV;
//        // /g2wzfqvMOeazgtsUVbq1kmJawROa6mcRAzwG1/GeJ4=
//        data = encrypt(test, key, iv);
//        System.out.println("数据："+test);
//        System.out.println("加密："+data);
//        String jiemi =desEncrypt(data, key, iv).trim();
//        System.out.println("解密："+jiemi);

    }
}
