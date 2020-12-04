package com.epam.esm.dal.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import javax.persistence.criteria.Subquery;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Purchase;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.transferobj.FilterParam;
import com.epam.esm.transferobj.OrderParam;
import com.epam.esm.transferobj.ParameterConstant;

@Component
public class SqlQueryBuilder {

	private static final String TAGS = "tags";
	private static final String CERTIFICATES = "certificates";
	private static final String PURCHASES = "purchases";
	private static final String USER = "user";
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

//	public CriteriaQuery<Tag> buildSearchMostPopularTagOfUserWithHighestCostOfAllPurchases(SessionFactory sessionFactory) {
//		CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
//		CriteriaQuery<Tag> query = criteriaBuilder.createQuery(Tag.class);
//		Root<Tag> tag = query.from(Tag.class);
//		Join<Tag, GiftCertificate> certificate = tag.join(CERTIFICATES);
//		Join<GiftCertificate, Purchase> purchase = certificate.join(PURCHASES);
//		Join<Purchase, User> user = certificate.join(USER);
//		
//		Subquery<Double> subquerySum = query.subquery(Double.class);
//		Root<Purchase> fromPurchase = subquerySum.from(Purchase.class);
//		subquerySum.select(criteriaBuilder.sum(fromPurchase.get("cost")))
//		.where(criteriaBuilder.equal(fromPurchase.get("Id_user"), user.get("id")));
//		
//		Subquery<Long> subqueryUserId = query.subquery(Long.class);
//		Root<User> fromUser = subqueryUserId.from(User.class);
//		Order sumCost = criteriaBuilder.desc(subquerySum);
//		subqueryUserId.select(fromUser.get("id")).orderBy(sumCost);
//		
//		query.select(tag).where(criteriaBuilder.equal(user.get("id"), subqueryUserId))
//		.groupBy(tag.get("id")).orderBy(criteriaBuilder.desc(criteriaBuilder.count(tag)));
//		
//	}

}
