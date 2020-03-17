package com.ningmeng.auth;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BCryptPasswordEncoderTest {

    @Test
    public void testPasswordEncoder(){
        String password = "111111";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        for (int i=0 ; i < 10; i++){
            //每个计算出的Hash值都不一样
            String hashPass = passwordEncoder.encode(password);
            System.out.println(hashPass);
            //虽然每次计算的密码hash值不一样但是效验是通过的
            boolean f = passwordEncoder.matches(password,hashPass);
            System.out.println(f);
        }
    }

}
