package com.epam.esm.entity;

import javax.validation.constraints.PositiveOrZero;

public class Pagination {

	@PositiveOrZero
	private int limit;
	@PositiveOrZero
	private int offset;
	
	public Pagination() {}
	
	public Pagination(@PositiveOrZero int limit, @PositiveOrZero int offset) {
		this.limit = limit;
		this.offset = offset;
	}

	public int getLimit() {
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
