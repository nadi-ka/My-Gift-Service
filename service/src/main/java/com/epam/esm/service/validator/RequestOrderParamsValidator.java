package com.epam.esm.service.validator;

import java.util.List;

import com.epam.esm.transferobj.OrderParam;

public class RequestOrderParamsValidator {
	
	private static final String paramNamePattern = "^creationDate|name$";
	private static final String paramDirectionPattern = "^asc|desc$";
	
	public static boolean validateOrderParams(List<OrderParam> orderParams) {	
		for (OrderParam param: orderParams) {
			if (!param.getName().matches(paramNamePattern)) {
				return false;
			}
			if (!param.getDirection().matches(paramDirectionPattern)) {
				return false;
			}
		}	
		return true;	
	}

}
