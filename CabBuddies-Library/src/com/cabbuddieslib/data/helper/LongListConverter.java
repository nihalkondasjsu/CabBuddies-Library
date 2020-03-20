package com.cabbuddieslib.data.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.AttributeConverter;

public class LongListConverter implements AttributeConverter<List<Long>, String> {

    @Override
    public String convertToDatabaseColumn(List<Long> longList) {
    	StringBuilder sb = new StringBuilder();
    	for(Long l:longList) {
    		sb.append( l+" " );
    	}
    	return sb.toString().trim();
    }

	@Override
    public List<Long> convertToEntityAttribute(String string) {
		List<Long> list = new ArrayList<Long>();
		for(String s:string.split(" ")) {
			list.add(Long.parseLong(s));
		}
    	return list;
    }
	
}