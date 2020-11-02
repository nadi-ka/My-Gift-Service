package com.epam.esm.dal.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.esm.util.parameter.FilterParam;
import com.epam.esm.util.parameter.OrderParam;

@Component
public final class SqlQueryBuilder {

	@Autowired
	private ColumnNameRequestParamMapper columnMapper;

	private static final String sqlFindCertificatesWithTags = "SELECT GiftCertificate.Id, GiftCertificate.Name, Description, Price, CreateDate, LastUpdateDate, Duration, Tag.Id, Tag.Name FROM GiftService.GiftCertificate JOIN GiftService.`Tag-Certificate` ON GiftCertificate.Id = `Tag-Certificate`.IdCertificate JOIN GiftService.Tag ON Tag.Id = `Tag-Certificate`.IdTag";
	private static final String WHERE = " WHERE ";
	private static final String AND = " AND ";
	private static final String OR = " OR ";
	private static final String LIKE = " LIKE ";
	private static final String ORDER_BY = " ORDER BY ";
	private static final String EQUALS_SIGN = "=";
	private static final String PERCENT_SIGN = "%";
	private static final String BRACKET_OPEN = "(";
	private static final String BRACKED_CLOSE = ")";
	private static final String QUOTE_SIGN = "'";
	private static final String EMPTY_STRING = " ";

	public String buildCertificatesQuery(List<FilterParam> filterParams, List<OrderParam> orderParams) {

		String sqlQuery = sqlFindCertificatesWithTags;

		if (filterParams.size() > 0) {
			sqlQuery += WHERE;
			for (FilterParam filter : filterParams) {
				sqlQuery += BRACKET_OPEN + columnMapper.getColumnByParamName(filter.getName());
				sqlQuery += EQUALS_SIGN;
				sqlQuery += QUOTE_SIGN + filter.getValue() + QUOTE_SIGN;
				sqlQuery += OR;
				sqlQuery += columnMapper.getColumnByParamName(filter.getName());
				sqlQuery += LIKE;
				sqlQuery += QUOTE_SIGN + PERCENT_SIGN + filter.getValue() + PERCENT_SIGN + QUOTE_SIGN;
				sqlQuery += BRACKED_CLOSE + EMPTY_STRING;
				sqlQuery += AND;
			}
			// remove last "AND ", if presents;
			String lastQueryCharacters = "AND ";
			if (sqlQuery.endsWith(lastQueryCharacters)) {
				sqlQuery = sqlQuery.substring(0, sqlQuery.length() - lastQueryCharacters.length());
			}
		}

		if (orderParams.size() > 0) {
			sqlQuery += ORDER_BY;
			for (OrderParam order : orderParams) {
				sqlQuery += columnMapper.getColumnByParamName(order.getName());
				sqlQuery += EMPTY_STRING;
				sqlQuery += order.getDirection();
				sqlQuery += EMPTY_STRING;
			}
			sqlQuery = sqlQuery.trim();
		}
		return sqlQuery;
	}

}
