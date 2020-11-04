package com.epam.esm.dal.util;

import java.util.ArrayList;
import java.util.List;

public final class DuplicateResultsRemover {

	public static <T> List<T> removeDuplicateResults(List<T> listWithDuplicates) {
		
		List<T> newList = new ArrayList<T>(); 
		   
        for (T element : listWithDuplicates) { 
  
            if (!newList.contains(element)) { 
  
                newList.add(element); 
            } 
        } 
  
        return newList; 
	}

}
