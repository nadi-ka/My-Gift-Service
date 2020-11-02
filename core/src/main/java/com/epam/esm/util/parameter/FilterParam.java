package com.epam.esm.util.parameter;

public class FilterParam {
	
	private String name;
	private String value;
	
	public FilterParam() {}
	
	public FilterParam(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "FilterParam [name=" + name + ", value=" + value + "]";
	}
	
}
