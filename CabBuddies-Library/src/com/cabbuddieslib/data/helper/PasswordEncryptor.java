package com.cabbuddieslib.data.helper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.cabbuddieslib.utils.AESCrypto;

@Converter
public class PasswordEncryptor implements AttributeConverter<String, String> {

	@Override
	public String convertToDatabaseColumn(String attribute) {
		// TODO Auto-generated method stub
		return AESCrypto.encrypt(attribute, false);
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		// TODO Auto-generated method stub
		return AESCrypto.decrypt(dbData, false);
	}

}
