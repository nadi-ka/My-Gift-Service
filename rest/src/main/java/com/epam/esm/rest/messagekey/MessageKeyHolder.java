package com.epam.esm.rest.messagekey;

public final class MessageKeyHolder {
	
	private MessageKeyHolder() {}
	
	public static final String TAG_NOT_FOUND_KEY = "exception.tag.not_found";
	public static final String NOTHING_FOUND_KEY = "exception.nothing_found";
	public static final String TAG_NOT_UPDATED_KEY = "exception.tag.cannot.update";
	public static final String TAG_ABSENT_KEY = "exception.tag.absent";
	public static final String TAG_BOUNDED_KEY = "exception.tag.bounded";
	public static final String TAG_DELETED_KEY = "message.tag.successfully.deleted";
	public static final String TAG_NAME_NOT_UNIQUE = "exception.name.not.unique";
	
	public static final String CERTIFICATE_NOT_FOUND_KEY = "exception.certificate.not_found";
	public static final String CERTIFICATE_INVALID_TAGS_KEY = "exception.certificate.invalid_tags";
	public static final String CERTIFICATE_NOT_UPDATED_KEY = "exception.certificate.cannot.update";
	public static final String CERTIFICATE_JSON_PATCH_ERROR = "exception.certificate.jsonpatch.error";
	public static final String CERTIFICATE_NOT_VALID = "exception.certificate.invlid.properties";
	public static final String CERTIFICATE_ABSENT_KEY = "exception.certificate.absent";
	public static final String CERTIFICATE_DELETED_KEY = "message.certificate.successfully.deleted";
	public static final String CERTIFICATE_INVALID_REQUEST_PARAM_KEY = "exception.certificate.invalid.params";
	
	public static final String USER_NOT_FOUND_KEY = "exception.user.not_found";
	public static final String USER_LOGIN_NOT_UNIQUE = "exception.not_unique_login";
	
	public static final String PURCHASE_NOT_FOUND_KEY = "exception.purchase.not_found";
	public static final String PURCHASE_EMPTY_CERTIFICATES_KEY = "exception.purchase.empty.certificates";
	public static final String PURCHASE_NOT_CREATED = "exception.purchase.not_created";

}
