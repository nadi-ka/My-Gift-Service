package com.epam.esm.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.epam.esm.dal.TagDao;
import com.epam.esm.dal.constant.ColumnNameHolder;
import com.epam.esm.entity.Tag;

@Repository
public class TagDaoSql implements TagDao {

	private final JdbcTemplate jdbcTemplate;

	private static final String sqlFindAllTags = "SELECT * FROM GiftService.Tag;";
	private static final String sqlFindTagById = "SELECT * FROM GiftService.Tag WHERE Id = (?)";
	private static final String sqlFindTagByName = "SELECT * FROM GiftService.Tag WHERE Name = (?);";
	private static final String sqlAddTag = "INSERT INTO GiftService.Tag (Name) VALUES (?)";
	private static final String sqlUpdateTag = "Update GiftService.Tag set Name = (?) where Id = (?);";
	private static final String sqlDeleteTagById = "DELETE FROM  GiftService.Tag WHERE Id = (?);";
	private static final String sqlFindCertificateIdByTagId = "SELECT IdCertificate FROM  "
			+ "GiftService.`Tag-Certificate` WHERE IdTag = (?) LIMIT 1;";

	@Autowired
	public TagDaoSql(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Tag addTag(Tag tag) {

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

				String paramToReturn = "Id";

				PreparedStatement preparedStatement = con.prepareStatement(sqlAddTag, new String[] { paramToReturn });
				preparedStatement.setString(1, tag.getName());

				return preparedStatement;
			}
		}, keyHolder);

		long newTagId = keyHolder.getKey().longValue();
		tag.setId(newTagId);

		return tag;
	}

	@Override
	public int updateTag(Tag tag) {

		int affectedRows = 0;

		affectedRows = jdbcTemplate.update(sqlUpdateTag, tag.getName(), tag.getId());

		return affectedRows;

	}

	@Override
	public List<Tag> findAllTags() {

		List<Tag> tags = new ArrayList<Tag>();
		try {
			tags = jdbcTemplate.query(sqlFindAllTags, ROW_MAPPER);
		} catch (DataAccessException e) {
			// nothing was found by the request
			return tags;
		}
		return tags;
	}

	@Override
	public Tag findTag(long id) {

		Tag tag;
		try {
			tag = jdbcTemplate.queryForObject(sqlFindTagById, new Object[] { id }, ROW_MAPPER);
		} catch (DataAccessException e) {
			tag = null;
		}
		return tag;
	}

	@Override
	public int deleteTag(long id) {

		int affectedRows = 0;

		int[] types = { Types.BIGINT };
		affectedRows = jdbcTemplate.update(sqlDeleteTagById, new Object[] { id }, types);

		return affectedRows;

	}

	// The method is used for checking, if there is at least one certificate,
	// bounded with given tag's Id

	@Override
	public long findCertificateIdByTagId(long tagId) {

		long certificateId;
		try {
			certificateId = (Long) jdbcTemplate.queryForObject(sqlFindCertificateIdByTagId, new Object[] { tagId },
					Long.class);
		} catch (DataAccessException e) {
			certificateId = 0;
		}
		return certificateId;
	}
	
	@Override
	public Tag findTagByName(String name) {

		Tag tag;
		try {
			tag = jdbcTemplate.queryForObject(sqlFindTagByName, new Object[] { name }, ROW_MAPPER);
		} catch (DataAccessException e) {
			tag = null;
		}
		return tag;
	}

	RowMapper<Tag> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> {
		return new Tag(resultSet.getLong(ColumnNameHolder.TAG_ID), resultSet.getString(ColumnNameHolder.TAG_NAME));
	};

}
