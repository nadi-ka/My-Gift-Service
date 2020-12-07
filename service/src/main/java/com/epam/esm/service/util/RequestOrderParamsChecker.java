package com.epam.esm.service.util;

import java.util.List;

import com.epam.esm.transferobj.OrderParam;

public final class RequestOrderParamsChecker {
	
	private static final String paramNamePattern = "^date|name$";
	private static final String paramDirectionPattern = "^asc|desc$";
	private static final String emptyString = "";
	
	public static List<OrderParam> checkAndCorrectOrderParams(List<OrderParam> orderParams) {	
		for (OrderParam param: orderParams) {
			if (!param.getName().matches(paramNamePattern)) {
				param.setName(emptyString);
			}
			if (!param.getDirection().matches(paramDirectionPattern)) {
				param.setDirection(emptyString);
			}
		}	
		return orderParams;	
	}
}
