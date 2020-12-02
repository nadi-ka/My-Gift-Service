package com.epam.esm.service.validator;

import java.util.List;

import com.epam.esm.transferobj.FilterParam;
import com.epam.esm.transferobj.OrderParam;

public class RequestParamsValidator {
	
	private static final String PARAM_VALUE_PATTERN = "'(''|[^'])*'";
	private static final String PARAM_NAME_PATTERN = "^creationDate|name$";
	private static final String PARAM_DIRECTION_PATTERN = "^asc|desc$";
	
	public static boolean validateFilterParams(List<FilterParam> filterParams) {
		for (FilterParam param: filterParams) {
			if (param.getValue().matches(PARAM_VALUE_PATTERN)) {
				return false;
			}
		}	
		return true;	
	}
	
	public static boolean validateOrderParams(List<OrderParam> orderParams) {
		for (OrderParam param: orderParams) {
			if (!param.getName().matches(PARAM_NAME_PATTERN)) {
				return false;
			}
			if (!param.getDirection().matches(PARAM_DIRECTION_PATTERN)) {
				return false;
			}
		}	
		return true;	
	}
	
//	public static boolean validateTagIds(Long[] tagIds) {
//		if (tagIds != null && tagIds.length != 0) {
//			for (Long id: tagIds) {
//				if (id == null) {
//					return false;
//				}
//			}
//		}
//		return true;
//	}

}
