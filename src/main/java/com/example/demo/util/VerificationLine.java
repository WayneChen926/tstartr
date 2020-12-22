package com.example.demo.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VerificationLine {
	@Value("${spring.boot.admin.notify.line.channelSecret}")
	private String channelSecret;
	
	public boolean checkFromLine(String requestBody, String X_Line_Signature) {
		SecretKeySpec key = new SecretKeySpec(channelSecret.getBytes(), "HmacSHA256");
		Mac mac;
		try {
			mac = Mac.getInstance("HmacSHA256");
			mac.init(key);
			byte[] source = requestBody.getBytes("UTF-8");
			String signature = Base64.encodeBase64String(mac.doFinal(source));
			if (signature.equals(X_Line_Signature)) {
				return true;
			}
		} catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		return false;
	}
}
