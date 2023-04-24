package com.hello.tools.landray;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class DESEncrypt {
    private static String originKey = "kmssAdmi";
    private static String originKey2 = "kmssPropertiesKey";
    /**
     * 加密算法
     * @param clearText
     * @param originKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String desEncript(String clearText, String originKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher= Cipher.getInstance("DES"); /*提供加密的方式：DES*/
        SecretKeySpec key=getKey(originKey);  /*对密钥进行操作，产生16个48位长的子密钥*/
        cipher.init(Cipher.ENCRYPT_MODE,key); /*初始化cipher，选定模式，这里为加密模式，并同时传入密钥*/
        byte[] doFinal=cipher.doFinal(clearText.getBytes());   /*开始加密操作*/
        String encode= Base64.encode(doFinal);    /*对加密后的数据按照Base64进行编码*/
        return encode;
    }

    /**
     * 解密算法 zeropadding解密算法
     * @param cipherText base加密密文
     * @param originKey 密码key
     * @return
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String zeroDesDecrypt(String cipherText, String originKey) throws BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException
    {
        Cipher cipher=Cipher.getInstance("DES");   /*初始化加密方式*/
        Key key=getKey(originKey);  /*获取密钥*/
        cipher.init(Cipher.DECRYPT_MODE,key);  /*初始化操作方式*/
        byte[] decode=Base64.decode(cipherText);  /*按照Base64解码*/
        byte[] doFinal=cipher.doFinal(decode);   /*执行解码操作*/
        return new String(doFinal);   /*转换成相应字符串并返回*/
    }

    /**
     * 解密算法 pkcs5padding解密算法
     * @param cipherText base加密密文
     * @param originKey 密码key
     * @return
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String pk5DesDecrypt(String cipherText, String originKey) throws BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException
    {
        byte[] sourceBytes = Base64.decode(cipherText);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        byte[] keyByte = getKeyByte(originKey);/*获取密钥*/
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyByte, "DES"));
        byte[] decoded = cipher.doFinal(sourceBytes);
        try {
            return new String(decoded, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取密钥算法
     * @param originKey
     * @return
     */
    private static SecretKeySpec getKey(String originKey)
    {
        byte[] buffer=new byte[8];
        byte[] originBytes=originKey.getBytes();
        /**
         * 防止输入的密钥长度超过64位
         */
        for(int i=0;i<8&&i<originBytes.length;i++)
        {
            buffer[i]=originBytes[i];  /*如果originBytes不足8,buffer剩余的补零*/
        }
        SecretKeySpec key=new SecretKeySpec(buffer,"DES"); /*第一个参数是密钥字节数组，第二个参数是加密方式*/
        return key;  /*返回操作之后得到的密钥*/
    }

    /**
     * key 不足8位补位
     * @param keyRule 密钥
     */
    public static byte[] getKeyByte(String keyRule) {
        Key key = null;
        byte[] keyByte = keyRule.getBytes();
        // 创建一个空的八位数组,默认情况下为0
        byte[] byteTemp = new byte[8];
        // 将用户指定的规则转换成八位数组
        for (int i = 0; i < byteTemp.length && i < keyByte.length; i++) {
            byteTemp[i] = keyByte[i];
        }
        key = new SecretKeySpec(byteTemp, "DES");
        return key.getEncoded();
    }
    public static String defaultDes(String cipherText){
        String result="";
        try {
            result=zeroDesDecrypt(cipherText,originKey);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static String pk5Des(String cipherText){
        String result="";
        try {
            result = pk5DesDecrypt(cipherText, originKey2);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        return result;

    }

    public static void main(String[] args) throws Exception {
        String text = "sC0RXliEiqM02Xy1mTEfLMgaA2n2j+BPiLMySuGqavI/Qy4zQ4seF/gEZTTYPmNRw0ue+FcrgRFr\n" +
                "gtweZaDQ6wjDFeyac4GLVY6VPF6SVCq+hiXpM9ijr5iDu5+942Fnn6GPvn8lLz7ckBVCbHr2gN+y\n" +
                "3N5B3cLlknaMixC4Z0RlhSVT8lcytNXFXXbvRLNOVTB0+KeaAkd4LsnCEyQDSRszKzPVZlevuL7p\n" +
                "H92o/HF4qrZ7F7BW8ZctcWI84aHln7J0q8Y9WSpXZCS4SPf+LNjK57+24w/0TVXhyIxVAmFsn9vj\n" +
                "vvFmE2pamTGuK878L/9YsF4RbVEIHFVaGypBowu4Dw0N6x5UGBbilDpikqJeSBhwEavzze3MAaAx\n" +
                "16dfgmcr8W1qr1uHI6mYWOXWEAddKNNmK2m1eC7JwhMkA0kbMysz1WZXr7i+6R/dqPxxHbmi4mjI\n" +
                "B5CFTLdnmL/KgP971bcC6NlNBxnZugXYUbbTcAx9008SY3b0OgKuKC9avIus5gw8bDBEvvRLKI8M\n" +
                "ch9fbgei0Ur17cwBoDHXp1+CZyvxbWqvW3/V9NRAQUKZJ2YbcGRUYqHuyqINUoahfBLOOGeBFCzp\n" +
                "JYULZLDtqUhNPKXaJFOSc9JhlucTWc3p01jtbr1+QehBym51oOVi/pKv26VXq2+UvEOTWO/AGxmi\n" +
                "jfIvn8XAO2YrPPfN3O8AQBf00sWGnfW2XfYkRexLYuPyNkCa9F0t25thBohNGa3tOkzSxiLPRb5D\n" +
                "GdP6TV8d4EWq5IjbqngAchz4SyO59OEIqSTgNrJdWEtKsNtqjb6UsVGoYP1O2Tv2FegV++Az4WjB\n" +
                "E8Okd+4Vfnk7aKD/sv9hCsjf4tYDtl32JEXsS2Lj8jZAmvRdLX+w+FUbImCm2W8swZ+TtqHEg2+n\n" +
                "dQX49h6MfuHQ/fJxlImp6RgDlvoWvg6nYMPukR6MfuHQ/fJxG/KUsJNUmB0X602ShqeNQsm+0gK2\n" +
                "kDfyHD+QVQZ3caPioSBFfbHMJPrOEnSzvAH0pcjnMejPRnNj7TyJtJVkcaXI5zHoz0ZzsTQeche4\n" +
                "6AtV43H3in9tydFkOpYxmQFdtRIrcYV7fdf2T7aYrSrn7UpujHzGGP6H+jqKWdh69dsW6fTUGycT\n" +
                "JUoJk2SWNTbOrEvJ6/711kUJWcDhB1t38I7Tj1uRhpU51PSJGppWerdLsSvFk+GggAlm3a8cxxMe\n" +
                "FYw7wwTx0AICByWhEAPzyCnRYBBpJzNZo7JNNr0S1xSHJ0fcFbKYg9gH8ZA/peVrA8l9mUODjM7I\n" +
                "W1MGQVsz4oxEKbxyuAuY0IocXMG2M9svfKEuytbKPuy3D7q3jt/gSu/pcKap3pPaGfMyS+uzdpYW\n" +
                "TgtTiYFdR4YBmRoXi76orQhJQxm28XzPMvb6bTR1EM4ZKEM6mUv6ufFxudXNZF8ra6sz7qymFSHm\n" +
                "JjU3Yi88WvZzGp0xMOWOV32jcaNpNsD9Iz0ZgMo0npQ7jn9agXnwruHJuaAPpvwIVeRB8u8dYtrb\n" +
                "468XyTAmDDsDXC6TR3dvoyjDAkaBjbbIlVFEaM+rO36vV72lEBgOn9cB180cqBtDhDl70Ls9WSBg\n" +
                "lAdeWyMnSDibrVwwJgw7A1wukxfPB9G3goBV0Gef+izHtwsa9ru+k6BtMpVxPCEmjMbb4qQLH+EB\n" +
                "4XwVsQRKFZ2H2pum4b6K4CtSjITYYVOMAFiTFOavL1zrZfabW807jM8lt72QgtLV1g1lI7HHulla\n" +
                "fU12WaZadD8puJdgFI/jnGqolWLeN9Dc2PMt1rZPu9YbHgDLur9yaKD0/pAHDBFJiJTtEGE8uZJm\n" +
                "gKWW8BAttHzubeDE5PzSeUe11oBwcgGtBQvfeeL51ZGabN9qBhClqDbDyE0VQ99ZdMu6HPA7YBib\n" +
                "cbPkm3797MnmFedyGOUoyJP809UM4h8VjvpvMKGcZDKodBdPsYGCCQ5+vNdgobztZlhEFxvqcohC\n" +
                "GLpDzopyAwhAsmIlIow1wLJXKkQ9zoR9x8UjMxdehe52XXeSlI6dcrF4B0o1yv95p3tLK64N7x0J\n" +
                "belqrAsAqNCeINqg4RMHU4nTmycqAwhAsmIlIozK8JVci+J2TgixIbZzDk2ahjw2ZsBAeyaHzFT5\n" +
                "8eusAVVKK94CxaXcgWW5LBw27nX1/u1FXXAaqEFqaLcUkeHRVLdfb+uLfj1WVor7iwNL0SYshbfj\n" +
                "8vFaeiZkTS1QlzTbAOvInHeVYiC4EIDZTE3uqh6wnOy3+ovIGgNp9o/gT4awRbBl+ML8ogtcNFWP\n" +
                "OY9B/dkAdTMCJ4fMVPnx66wBTKC1Fd9a4cV+uv/hjZJHST+Out3/oYUpfImtbXEtZGtxBtEEVYBE\n" +
                "g0ohfMnZrOmlNJ6UO45/WoEF91OoNxlPhMu6gCS6vU78kiGqkDT7SJTUuGrgotdlk+wDXXDNEK1n\n" +
                "/tHRhrufoavUuGrgotdlk+wDXXDNEK1ngzjTDK8QyDMjJTx6PrjSruPz1bmutX89KoCF0OnuCZHI\n" +
                "j6U0BnJ6aUl0+By/cApI1OLmX5mvesnGfsQJR4lKJ1NtHxURxul8wtkr8V5vAnzU4uZfma96ybYf\n" +
                "HwrhwLM5FLz/bCTtOzWGj5vLuh+Hx8i9g7jTjxdr+mYF/MufwVBcjbbmvI/dNzzDF3L5ertwrXay\n" +
                "RzLZMlYQW4FbDwoImiSfsDv7Dl99nJLwHRN4jBMXYH5YyLi875+v46C3Rs/kE99Z3uAcrXsQf4Wx\n" +
                "6KIWd9nYWWaqGPUlNDsRwizqVtsdDfbsUhJ1kffXzQELDno/gxPyaASWtF/+vUzBdDZLbDZ8nfO6\n" +
                "XJCU17+4wp2yzc1sw/I6ZcFGT+rE4FwN+qQK9jATlZC6Qr5VQMHk27/5xau65wk3EuljyhSDYc5U\n" +
                "6IVMDXwwQdwsi+dc/W8mEgkhQFfINrn7KmbNk9VdX08BfKqwyS7m7t9KPMMXcvl6u3BBKpOi467Y\n" +
                "Px7OTomemdFB7o0sLEFMmXkFocmp8qBUmVlhNnLDybB3iOvdIIm6sGu3+lbiuGBzx8Tl9qBACp2Y\n" +
                "GrLCEisl7d7RKkZ8J5t9YO4ct2tJGXXcl6A3PP+GZnzSMH1WM8zyqqZsz6CA6nomfj50EB8BvLzx\n" +
                "AGdxrGvqTdsar2f2SD1/FPClxM8fwgOcXo3+0Ib6+OEdz3Jy3Fu2Ck8PBDGG9wXVO9MIk1Yp7euz\n" +
                "FdUmkoIsUn8cNEn794hArGHDnNmpRdsar2f2SD1/HLrZpk+zP7w4tqVsu8zuKTQ2mKeXFpcr7ANd\n" +
                "cM0QrWfSMH1WM8zyqv5UBguryP4uCMKZnt+SvJrIGgNp9o/gT4awRbBl+ML8GrLCEisl7d6JdlnA\n" +
                "6/0lzO4ct2tJGXXcfImtbXEtZGtROPxrfVxNs3+8u2kGN5pkHsJD52knxYhSfxw0Sfv3iECsYcOc\n" +
                "2alFVdcXmmEr+Y/gmRZKRUxAsGb79ywECmvbp0AcKWpNDooGtXmRlZb8eF7bppcQ+RB9hnwSa37J\n" +
                "LLZ0iIiSsrvxR6PHKiKz+cS7cudsZ8VjxveF2B+EOGM6+NDNpwe4sRG1W80cib0BepTpLVi6U4oI\n" +
                "6YH0L3nJ4tNCfj9VKDjf0QXmfN/WPEkyWH/emHZ8bwWQg8460+xN6aiHzFT58eusAbqXW96MsSj4\n" +
                "isOgSiOdMi5Sfxw0Sfv3iECsYcOc2alFvRBMhExfZctv1rzypy31RBwNVdtBBKer4CAKipUwg4IM\n" +
                "wgw9ZBPbBHlK1tEKUqo7tqrFSmOk/es0npQ7jn9agUFnszQUgA0rQsfXjvzuYt5L9VO5T2vWZpxG\n" +
                "zWe3Vf/EFfEi/T9oBCcA+itPnlbLbfGcQChoAxRm2GkRhZiT7Sz58N/0PSDkK2aVE35peFBJjcus\n" +
                "/qnnb0CstdjORaXVCFPcNcxSwEHOf5VLCi7lHVWZy94zUzZM5FGd4ACblETAb/kyd/YZVlcZMMmd\n" +
                "JpWIW6lEtZ+dpntu6BTZJlv4vd8Fe6q3zC2zTEbWe5qwbec3/cjZw4mxH1UyvJ78hHUEJk0uInib\n" +
                "vDwaa+pnqWvcxPaGDEUTwm5s95wIrZ5wEqQC5+QQsB6+IlNcTBI91gAm5QaWNY43tjOoa5rJLibc\n" +
                "Jmodk6SdkqUEDg5lAaIVGdvbxTkPagq3lpKrGmE4q8yQbt7OBLciKLo2OrhwQlgHniEABTaHn0jv\n" +
                "6Y+cNmK8bMK6JmXVmXUN+ILkgI9jx9lR4QlJy8Ol/Jm51Qip0o5qoZdFWVFByrbZ0PYieKcIqUAo\n" +
                "dHB2Pb3YLY8Eo5mFfZFrXet2WWNKm4giKRBGocTAW7S6lwkKIUhBekAIpB75U0WXRkSSpmSYbPAC\n" +
                "fO7tAadDv3hpPvpMec9mjCy1pztn1V27IS1Y4O8T2s2swhw29Ks1t1Xdrk0edCvRYlIkBnI7QhAq\n" +
                "Xs1ZD54uMtK6cxYXIl/Mh0K1TB5rX6/S5xP1fBGXJvsItjxxifAnxHY+NYoB2c5SWqltu/oNZ1RY\n" +
                "VQ82zC1LLkqhUoiQe99zVnGz8PZsSvgxcGPJJHnNIimRSL31GEeB/UB229uxZcPFXnTpS0wfYfiH\n" +
                "eGBXCF5C/ZC86dQEj5w2Yrxswrp8tdOKsbCetxNbKfJXrsnW/NeN6LJPV+GA7X5TCzD2vepByPL8\n" +
                "+RBV1Lhq4KLXZZMpc1Msl5xnMA8K+eaReCaMbjZ6+zlt0PED76SwwhpFoqkd5QrO7xm8H+aqpiTv\n" +
                "KqcxDVOEo8jQtoe41jIGnMvLTqbTjmn/ZUf7xJb2+gu0Y2E5NVgUs/Ii+MH6dNYcF2TsGOn3H57z\n" +
                "QskZUbQ3vU1Z4fc72qzBjDFMnADkvv9kUbhVNpUsogCjnAuC3fqXo2wukvwEn+NXXZxCn7oSJSe6\n" +
                "Nl7NEpcGVle9MrM5A+MQYDvAG1djZZjjCJ80/4j5g4bXEWXaafi6Mj7fhjafftTp3r0Rfu2s7BqK\n" +
                "b9UVmlv8TCSlJja0CnsQ0dX0s3RN4x7NFBFRbX7DjLKrjzV4sYx6Ngyb8s+4y1+xdthd4nqaTHpr\n" +
                "xG7O3xt6w6OQIZaDXnv9914uB79+eYkLda9Fu35Ro1umvRxyboIQzVVFT8A3LKLIjXuWbN8fByJ1\n" +
                "V9pVc8ebN2caRJkinm++30kKdVurhdcU4KSnvzU2diN68PgGImldAIhkA/2ACTsKOgir6LIZVxPD\n" +
                "viinu+MT+3dGQkxjrlqgWGtBgzRQLMt68+UJXaoqI1WpAX2Y/ngcbUAnONst+E7K1a6wAzvnnyvw\n" +
                "QLzHd0bRx7098NAbVuqC2TYbwBiphvjXZsIznT7VwZ7+NBH7XjyWUVrG3OPlxOPLb3J7nfxOdKEK\n" +
                "EvLB1Z2MgAlki2jTMus3Bn5OgV4lsYW5JGVhJRxt4ed38Rn8qK6osWrYnyuuUxUUO1JFxwW1mwfY\n" +
                "8zVfHjc6OkDIeC0WSWLlDM3VCYiNFNdOqyA7wAbCI+Rvwr8qC2w/ydeOgONBTM8p1/e6ImvOr+Rs\n" +
                "Tim/beaZ7mani5Z/TK9W9eZm9J0NihmW9TtwlF84/Pj02LkgvoeDcsHntKywklTH/T7zDtcREQ6h\n" +
                "aJS51w/LiWfbyQU1lmz+PpoYBfSmP22I14RTDAqdqMccOyS733ZFKVH8qATh52JlnUlim2mrPIe4\n" +
                "1jIGnMvL+aWl8ziVxcO38XMPn3BeUlQ4gSyRdvqd4ANP9p4EEMJxjlNqy5p2XO1OQVIi80SKgLto\n" +
                "Q9cDLzxhgzGzHMfTcm4w8IXd4O7LgL1DnWWa3DD1/u1FXXAaqEVZUUHKttnQTvCyl8uXyFV3+bYP\n" +
                "mBPGCzFPFbBtY8Dhh7jWMgacy8siZrF2OelPrlpF96+4JFBC5Q61/gSvczVgsxepkUcOQY+cNmK8\n" +
                "bMK6L/8tl1iycXm8dswIBuZGbUbGEPPLTNMrRFmFdhwhH65uMPCF3eDuy1tfolijLTMR7fGyyhFt\n" +
                "j6jRxiaJVyrQMvu+FW2HEp3uFNzWrZuaNvrtTkFSIvNEiuDKeo1WpCscXV2sx6ZBMR8naHN49U70\n" +
                "00VZUUHKttnQNovdtLdOC2sAxjhIPFNUEXMtAWXazd5H4AzeprcEmOV5ka7hOMapEEYPilNYRpts\n" +
                "itfdtfiVjJEl52UvJKcQukVZUUHKttnQ94kHSgV1J/dyMgJnelxLkXyJrW1xLWRrRVlRQcq22dAR\n" +
                "N7INEv/c+pru3xGimFB2ah5jA2MalZrIsGNZbQfvJ4rX3bX4lYyRKXo9s0G4nFBJSoY4Z9CzlKe6\n" +
                "pMsuKXwUMQZCrvhaOSZbm2aXEZRlU6C6pxjop2Ze+8X6Y81EaeN63XpCDghLDDp/G1+q+7Nlk+Cf\n" +
                "YLtBa6QwEDhbMFG2TKXJCLqpucghXfllrqk4p7lMTgie/HhiOZlUkqbuPn/kX/qnx/DiGXFLmk60\n" +
                "V4gkSY399rkjcIz8TvGXupN+RsB6D4lgotaMGCKPBPVitXmxuLbHOwkhZuOqEANhi3rem4E9jjzW\n" +
                "PsrCAv08GTukYVrbwmzEiWQyyOpIDRtXGRiKpEnj48DhH4U9fn519Qt3TXN/bqql767e2PguwnfM\n" +
                "KqrameJATAsdDyQrAyr2D9LUexSeiTn1X1YiN5M3KjDOm9l/77q8pbnma1TIFwLcYmo7Gphwyzrw\n" +
                "StPp/GUlxCix7cn77e1Z6fqUpDQeBi4NauK/RTGa2TyWIFstPkBbgo/BHiv2oIKt9LiIKdMaAxXJ\n" +
                "sCD36ZP0VLwEYMxkp0AQWYT3Chz5pH+J2iTFyAEYpFz5RhOLwzA08JM4kaBFJ74PWdGKWVDeBxU3\n" +
                "aV1XSkNv3PB98EbG/NWjHgKhWgFjFCDF/DGIiyszD5LcCU53323tp487TPG449XoXt+RKTnH3baU\n" +
                "+HQL+7Rmw/vQgH5zda1AOK2HXJv0U/BU/P0tuISrEiPlr2FgThATQl2dufaGvkxPT+4D3kpoPPij\n" +
                "NujKp8HXxOvOywRSOCLcarmjTJ0vHIurZ2Oguii0FkBIFv0nUIhYInw4XYriMkETXnKPuoMrJa5e\n" +
                "a/JeOd2XnI0n4QFf/HnR1yK8YnyFcYf4tYB323+xfQk7vBZMmMD3+6nPsACHQ1YFVWA/fFaQal7T\n" +
                "W/puVB79lMa2JXUoTBTjBeV4zFZOI3W+kT1ear6ViAm8IRDdK13DDVfvJ5C2Udda47z9NAE4RJsx\n" +
                "uxNPfOeYWud4bx2W4992nwWGF1aWjMwULDlOyK5KEzdjpKZLwWXAool3jaHjPNOcay+Jk3wN04Ec\n" +
                "nP3XFvXfUdJuFGeZfb6nk5JpOFNltDtgleCHZ9mlblO6QOjz4r5H2S1SYRjC3snWlnFr9hN0Yf2M\n" +
                "iKvEDa0RwYBGhIx/TNDBVLqOJ/vwTJyhjyPyhyzJBhMwf+n7pYXXaWHoaQDNFeDoE1JmJ2GZby8o\n" +
                "i6I8huLA91ZC2I4C52ABnF2w+QVPbP3p4oDqsbISoGHoaQDNFeDoE1JmJ2GZby9fFFUVoYokBUta\n" +
                "9IBmPEOV+znJ5xmzprQr7ejHLCx4muJH20mCJsJtSXMfdDDuwY29qvTUr3zklzuHr1r/gYtn3R9f\n" +
                "fg3wv8Cb2I5gJRvVN2r8sbQ9WlS+GTGPGtIuxHzdm72rUoGaFVAH9THO3x2vt4D6syLCKi1Jr6L0\n" +
                "EIKUt6ZANoECLbsAlvnJAGSqycplfso/aFSrO8M4bO4BSfn7RT1P4WabCLUsZCQ/+UvGLTqTWTq3\n" +
                "kP5bCy1qtCGy4d8=";
        String kmssPropertiesKey = pk5DesDecrypt(text, "kmssPropertiesKey");
        System.out.println(kmssPropertiesKey);
    }
}
