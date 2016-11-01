package org.com.ca.ca.exceptions;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.json.JSONObject;

/**
 * Use this exception to report the errors to the client from the Library
 * implementation. VIN Framework catches this exception and transforms
 * in to json/xml/yaml etc... based on the response client is expecting.
 * 
 */
public class DGSMSNestableException extends NestableRuntimeException {
	static final long serialVersionUID = 50;

	/**
	 * Helpful constants, define custom where applicable and pass in to the
	 * exception
	 */
	public static final long CODE_SUCCESS = 0;
	public static final long CODE_INTERNAL_SERVER_ERROR = 50;
	public static final long CODE_SESSION_ERROR = 20;
	public static final long CODE_AUTHENTICATION_ERROR = 30;
	public static final long CODE_RESOURCE_NOT_FOUND = 40;
	public static final long CODE_RESOURCE_NOT_SUPPORTED = 42;
	public static final long CODE_INTERFACE_NOT_SUPPORTED = 44;

	/**
	 * Helpful error messages
	 */
	public static final String MSG_SUCCESS = "Request processed successfully";
	public static final String MSG_INTERNAL_SERVER_ERROR = "Internal Server error";
	public static final String MSG_SESSION_ERROR = "Failed to open session";
	public static final String MSG_AUTHENTICATION_ERROR = "Authentication failed";
	public static final String MSG_RESOURCE_NOT_FOUND = "Requested resource is not found";
	public static final String MSG_RESOURCE_NOT_SUPPORTED = "Method is not supported/implemented";
	public static final String MSG_INTERFACE_NOT_SUPPORTED = "Interface is not supported/implemented";

	/**
	 * error number. This along with the message will be returned to the
	 * applications using the Libraries
	 */
	private long code = CODE_SUCCESS;

	public DGSMSNestableException(final long code, final String message) {
		super(message);
		this.code = code;
	}

	public final long getCode() {
		return code;
	}

	public final String toJSONString() {
		String result = "";
		try {
			final JSONObject jsonobj = new JSONObject();
			jsonobj.put("statusMessage", getMessage());
			jsonobj.put("status", getCode());
			result = jsonobj.toString();
		} catch (Exception ex) {
			result = "{'error':'Error Preparing JSON data for LibraryNestableException'}";
			ex.printStackTrace();
		}
		return result;
	}
}
