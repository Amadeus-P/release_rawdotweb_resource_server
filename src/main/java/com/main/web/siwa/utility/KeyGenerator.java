package com.main.web.siwa.utility;

import javax.crypto.SecretKey;
import java.util.Base64;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class KeyGenerator {
    // 해시 알고리즘(JWT의 서명이 해쉬알고리즘으로 암호화 됨) 키 + 나만의 키를 위해 비밀키 생성 프로그램
    public static void main(String[] args) {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512); // HS512 알고리즘에 적합한 키 생성
        System.out.println("key" + key);
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded()); // Base64로 인코딩하여 출력
        System.out.println("Generated Key: " + base64Key);
    }
}
