package com.epam.esm.dal.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.epam.esm.dal.constant.ColumnNameHolder;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

public class CertificateWithTagsMapper implements RowMapper<GiftCertificate> {
	
	Map<Long, GiftCertificate> certificateMap = new HashMap<Long, GiftCertificate>();

	@Override
	public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		GiftCertificate certificate = certificateMap.get(rs.getLong(ColumnNameHolder.CERTIFICATE_ID));
		
		if (certificate == null) {
			certificate = new GiftCertificate();
			certificate.setId(rs.getLong(ColumnNameHolder.CERTIFICATE_ID));
			certificate.setName(rs.getString(ColumnNameHolder.CERTIFICATE_NAME));
			certificate.setDescription(rs.getString(ColumnNameHolder.CERTIFICATE_DESCRIPTION));
			certificate.setPrice(rs.getDouble(ColumnNameHolder.CERTIFICATE_PRICE));
			certificate.setCreationDate(rs.getTimestamp(ColumnNameHolder.CERTIFICATE_CREATION_DATE).toLocalDateTime());
			Timestamp lastUpdateDate = rs.getTimestamp(ColumnNameHolder.CERTIFICATE_UPDATE_DATE);
			LocalDateTime lastUpdateDateTime = ((lastUpdateDate == null) ? null : lastUpdateDate.toLocalDateTime());
			certificate.setLastUpdateDate(lastUpdateDateTime);
			certificate.setDuration(rs.getInt(ColumnNameHolder.CERTIFICATE_DURATION));
			certificate.setTags(new ArrayList<Tag>());
			
			certificateMap.put(rs.getLong(ColumnNameHolder.CERTIFICATE_ID), certificate);
		}
	
		long tagId = rs.getLong(ColumnNameHolder.TAG_ID);
		String tagName = rs.getString(ColumnNameHolder.TAG_NAME);
		Tag tag = new Tag(tagId, tagName);
		
		List<Tag> currentCertificateTagList = certificateMap.get(rs.getLong(ColumnNameHolder.CERTIFICATE_ID)).getTags();
		currentCertificateTagList.add(tag);
		certificate.setTags(currentCertificateTagList);

		return certificate;
	}

}
