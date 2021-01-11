package com.epam.esm.dal.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Component;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.transferobj.FilterParam;
import com.epam.esm.transferobj.OrderParam;
import com.epam.esm.transferobj.ParameterConstant;

@Component
public class SqlQueryBuilder {

	private static final String TAGS = "tags";
	private static final String PERCENT_SIGN = "%";

	public CriteriaQuery<GiftCertificate> buildCertificatesFilterOrderQuery(List<FilterParam> filterParams,
			List<OrderParam> orderParams, EntityManager entityManager) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
		Root<GiftCertificate> certificate = query.from(GiftCertificate.class);
		Join<GiftCertificate, Tag> tag = certificate.join(TAGS);

		if (filterParams.size() > 0) {
			List<Predicate> p = new ArrayList<Predicate>();
			for (FilterParam filter : filterParams) {
				if (filter.getName().equals(ParameterConstant.TAG)) {
					Predicate conditionTag = criteriaBuilder.like(tag.get(ParameterConstant.TAG_NAME),
							PERCENT_SIGN + filter.getValue() + PERCENT_SIGN);
					p.add(conditionTag);
				}
				if (filter.getName().equals(ParameterConstant.CERTIFICATE_NAME)) {
					Predicate conditionName = criteriaBuilder.like(certificate.get(ParameterConstant.CERTIFICATE_NAME),
							PERCENT_SIGN + filter.getValue() + PERCENT_SIGN);
					p.add(conditionName);
				}
				if (filter.getName().equals(ParameterConstant.DESCRIPTION)) {
					Predicate conditionDescription = criteriaBuilder.like(
							certificate.get(ParameterConstant.DESCRIPTION).as(String.class),
							PERCENT_SIGN + filter.getValue() + PERCENT_SIGN);
					p.add(conditionDescription);
				}
			}
			if (!p.isEmpty()) {
				Predicate[] predicate = new Predicate[p.size()];
				p.toArray(predicate);
				query.select(certificate).where(predicate);
			}
		}
		if (orderParams.size() > 0) {
			if (orderParams.get(0).getDirection().equals(ParameterConstant.DIRECTION_ASC)) {
				query.orderBy(criteriaBuilder.asc(certificate.get(orderParams.get(0).getName())));
			} else {
				query.orderBy(criteriaBuilder.desc(certificate.get(orderParams.get(0).getName())));
			}
		}
		return query.distinct(true);
	}

	public CriteriaQuery<GiftCertificate> buildSearchCertificatesByTagsQuery(Long[] tagIds,
			EntityManager entityManager) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
		Root<GiftCertificate> certificate = query.from(GiftCertificate.class);
		Join<GiftCertificate, Tag> tag = certificate.join(TAGS);
		List<Long> ids = Arrays.asList(tagIds);
		Expression<Long> expression = tag.get(ParameterConstant.TAG_ID);
		Predicate predicate = expression.in(ids);
		return query.select(certificate).where(predicate).groupBy(certificate.get(ParameterConstant.CERTIFICATE_ID))
				.having(criteriaBuilder.equal(criteriaBuilder.count(tag), ids.size()));
	}

}
