package com.ningmeng.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class JwtTest {


    @Test
    public void testCreateJwt(){
        //证书文件
        String key_location = "nm.keystore";
        //密钥库密码
        String keystore_password = "ningmeng";
        //访问证书路径
        ClassPathResource resource = new ClassPathResource(key_location);
        //密钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource,keystore_password.toCharArray());
        //密钥的密码，此密码和别名要匹配
        String keypassword = "ningmeng";
        //密钥别名
        String alias = "nmkey";
        //公钥 和 密钥
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, keypassword.toCharArray());
        //私钥
        RSAPrivateKey aPrivate = (RSAPrivateKey)keyPair.getPrivate();
        //定义payload信息
        Map<String,Object> tokenMap = new HashMap<>();
        tokenMap.put("id","857");
        tokenMap.put("name","fuceng");
        tokenMap.put("roles","admin,user");
        tokenMap.put("ext","1");
        //生成Jwt令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(tokenMap), new RsaSigner(aPrivate));
        //取出jwt令牌
        String token = jwt.getEncoded();
        System.out.println("token =====" + token);
        //eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9
        //.eyJleHQiOiIxIiwicm9sZXMiOiJhZG1pbix1c2VyIiwibmFtZSI6ImZ1Y2VuZyIsImlkIjoiODU3In0
        //.X1p__cqslSh-TLq7SZyskw7rwyyhl65a3fpi0ThVYE4c8gxllVi7qNStsRqSIGDz8hqgOzLF9EB8jdPJ-0ms2mdDgzMFqUdyxYEm4KeoOlU1PoPIuI-avtnCe566YeQ0pTuIgifCWFJqkSlwup1U79-BczYdXU9cH9INSglmUSbCnsDyFTwVaq5_bBDLyNZMGkExeQB3wGW1Uf7tJGz9T866p5ySe3xTeLkktqPvIhaj_85-TunJKC7VyAdhoCv1gLN0CwoYWzrQLmSWnnDRTQYf1rf9YLFkkd7BaPrmFgSvwwrym43K5QgYw9Gf3GoXf6jrOZeLO7Ec3Ex8zgc_7w
    }




    @Test
    public void testVerify(){
        //jwt 令牌
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHQiOiIxIiwicm9sZXMiOiJhZG1pbix1c2VyIiwibmFtZSI6ImZ1Y2VuZyIsImlkIjoiODU3In0.X1p__cqslSh-TLq7SZyskw7rwyyhl65a3fpi0ThVYE4c8gxllVi7qNStsRqSIGDz8hqgOzLF9EB8jdPJ-0ms2mdDgzMFqUdyxYEm4KeoOlU1PoPIuI-avtnCe566YeQ0pTuIgifCWFJqkSlwup1U79-BczYdXU9cH9INSglmUSbCnsDyFTwVaq5_bBDLyNZMGkExeQB3wGW1Uf7tJGz9T866p5ySe3xTeLkktqPvIhaj_85-TunJKC7VyAdhoCv1gLN0CwoYWzrQLmSWnnDRTQYf1rf9YLFkkd7BaPrmFgSvwwrym43K5QgYw9Gf3GoXf6jrOZeLO7Ec3Ex8zgc_7w";
        //公钥
        String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApxEWmTUcEKwjUvVn/xrtgTNdc+3vGumgx1Ky8+yjWeHI6WxziBj0UFmJ78be4he8LsKflD8Rfp6wt0GvZrLnjfJx8a92+6MYFXUw3ukcPrqGbkX1nzjEw3CCQBGZC6xahkok891x9P6xhokUJA3W668U0kQ6xonEzDiiGxCNwb3Dy7zLLO1MBtxTGmEn+r3YY9R403c8pJ+DF2rvhQj2WbZO1s18QddD6Eb2r8avjzWrwz/xuAUnXd5k0j1cLH/bFytta8gHElv9NzYS19O0WXk2DPGcH8UeYGshAO58MANo6SVNVHJp092ZmnDTE1qbYJaiUMZHmPdtJKkZH9omoQIDAQAB-----END PUBLIC KEY-----";

        //效验jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publicKey));
        //获取jwt原始内容
        String claims = jwt.getEncoded();
        //jwt令牌
        String encode = jwt.getEncoded();
        System.out.println(encode);

    }


}
