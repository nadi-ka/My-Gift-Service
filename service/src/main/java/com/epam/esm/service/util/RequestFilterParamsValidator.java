package com.epam.esm.service.util;

import java.util.List;

import com.epam.esm.transferobj.FilterParam;

public final class RequestFilterParamsValidator {
	
	private static final String paramValuePattern = "'(''|[^'])*'";
	
	public static boolean validateFilterParams(List<FilterParam> filterParams) {	
		for (FilterParam param: filterParams) {
			if (param.getValue().matches(paramValuePattern)) {
				return false;
			}
		}	
		return true;	
	}

}
