package com.epam.esm.transferobj;

import javax.validation.constraints.PositiveOrZero;

public class Pagination {

	@PositiveOrZero
	private int limit;
	@PositiveOrZero
	private int offset;
	
	private static final int LIMIT_DEFAULT = 5;
	
	public Pagination() {}
	
	public Pagination(@PositiveOrZero int limit, @PositiveOrZero int offset) {
		this.limit = limit;
		this.offset = offset;
	}

	public int getLimit() {
		if (limit == 0) {
			return LIMIT_DEFAULT;
		}
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

}
