package com.cabbuddieslib.data.helper;

import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DateConverter implements AttributeConverter<Date, Long> {

	@Override
	public Long convertToDatabaseColumn(Date attribute) {
		// TODO Auto-generated method stub
		return attribute.getTime();
	}

	@Override
	public Date convertToEntityAttribute(Long dbData) {
		// TODO Auto-generated method stub
		return new Date(dbData);
	}

}

