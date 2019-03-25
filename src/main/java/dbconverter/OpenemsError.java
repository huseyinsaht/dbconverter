package dbconverter;

import java.util.TreeMap;

/**
 * Holds named OpenEMS Errors.
 */
public enum OpenemsError {
	/*
	 * Generic error.
	 */
	GENERIC(1, "%s"),
	/*
	 * Common errors. 1000-1999
	 */
	COMMON_NO_VALID_CHANNEL_ADDRESS(1000, "This [%s] is not a valid channel address"), //
	COMMON_USER_NOT_AUTHENTICATED(1001, "User is not authenticated. [%s]"), //
	COMMON_ROLE_ACCESS_DENIED(1002, "Access to this resource [%s] is denied for User with Role [%s]"), //
	COMMON_AUTHENTICATION_FAILED(1003, "Authentication failed"), //
	COMMON_USER_UNDEFINED(1004, "User [%s] is not defined"), //
	COMMON_ROLE_UNDEFINED(1005, "Role for User [%s] is not defined"), //
	/*
	 * Edge errors. 2000-2999
	 */
	EDGE_NO_COMPONENT_WITH_ID(2000, "Unable to find OpenEMS Component with ID [%s]"), //
	EDGE_MULTIPLE_COMPONENTS_WITH_ID(2001, "Found more than one OpenEMS Component with ID [%s]"), //
	EDGE_UNABLE_TO_APPLY_CONFIG(2002, "Unable to apply configuration to Component [%s]: [%s]"), //
	EDGE_UNABLE_TO_CREATE_CONFIG(2003, "Unable to create configuration for Factory [%s]: [%s]"), //
	EDGE_UNABLE_TO_DELETE_CONFIG(2004, "Unable to delete configuration for Component [%s]: [%s]"), //
	EDGE_CHANNEL_NO_OPTION(2005, "Channel has no Option [%s]. Existing options: %s"), //
	/*
	 * Backend errors. 3000-3999
	 */
	BACKEND_EDGE_NOT_CONNECTED(3000, "Edge [%s] is not connected"), //
	BACKEND_UI_TOKEN_MISSING(3001, "Token for UI connection is missing"), //
	BACKEND_NO_UI_WITH_TOKEN(3002, "No open connection with Token [%s]"), //
	/*
	 * JSON-RPC Request/Response/Notification. 4000-4999
	 */
	JSONRPC_ID_NOT_UNIQUE(4000, "A Request with this ID [%s] had already been existing"), //
	JSONRPC_UNHANDLED_METHOD(4001, "Unhandled JSON-RPC method [%s]"), //
	JSONRPC_INVALID_MESSAGE(4002, "JSON-RPC Message is not a valid Request, Result or Notification: %s"), //
	JSONRPC_RESPONSE_WITHOUT_REQUEST(4003, "Got Response without Request: %s"), //

	/*
	 * JSON Errors. 5000-5999
	 */
	JSON_HAS_NO_MEMBER(5000, "JSON [%s] has no member [%s]"), //
	JSON_NO_INTEGER_MEMBER(5001, "JSON [%s:%s] is not an Integer"), //
	JSON_NO_OBJECT(5002, "JSON [%s] is not a JSON-Object"), //
	JSON_NO_OBJECT_MEMBER(5003, "JSON [%s] is not a JSON-Object"), //
	JSON_NO_PRIMITIVE(5004, "JSON [%s] is not a JSON-Primitive"), //
	JSON_NO_PRIMITIVE_MEMBER(5005, "JSON [%s] is not a JSON-Primitive"), //
	JSON_NO_ARRAY(5006, "JSON [%s:%s] is not JSON-Array"), //
	JSON_NO_ARRAY_MEMBER(5007, "JSON [%s:%s] is not JSON-Array"), //
	JSON_NO_DATE_MEMBER(5008, "JONS [%s:%s] is not a Date. Error: %s"), //
	JSON_NO_STRING(5009, "JSON [%s] is not a String"), //
	JSON_NO_STRING_MEMBER(5010, "JSON [%s:%s] is not a String"), //
	JSON_NO_BOOLEAN(5011, "JSON [%s] is not a Boolean"), //
	JSON_NO_BOOLEAN_MEMBER(5012, "JSON [%s:%s] is not a Boolean"), //
	JSON_NO_NUMBER(5013, "JSON [%s] is not a Number"), //
	JSON_NO_NUMBER_MEMBER(5014, "JSON [%s:%s] is not a Number"), //
	JSON_PARSE_ELEMENT_FAILED(5015, "JSON failed to parse [%s]. %s: %s"), //
	JSON_PARSE_FAILED(5016, "JSON failed to parse [%s]: %s"), //
	JSON_NO_FLOAT_MEMBER(5017, "JSON [%s:%s] is not a Float"), //
	;

	/**
	 * Gets an OpenEMS-Error from its code.
	 * 
	 * @param code the error code
	 * @return the OpenEMS-Error
	 */
	public static OpenemsError fromCode(int code) {
		OpenemsError error = ALL_ERRORS.get(code);
		if (error == null) {
			System.out.println("OpenEMS-Error with code [" + code + "] does not exist");
			error = OpenemsError.GENERIC;
		}
		return error;
	}

	private final static TreeMap<Integer, OpenemsError> ALL_ERRORS = new TreeMap<>();

	private final int code;
	private final String message;
	private final int noOfParams;

	private OpenemsError(int code, String message) {
		this.code = code;
		this.message = message;
//		this.noOfParams = CharMatcher.is('%').countIn(message);
		this.noOfParams = 0;
	}

	public int getCode() {
		return code;
	}

	public String getRawMessage() {
		return message;
	}

	public String getMessage(Object... params) {
		if (params.length != this.noOfParams) {
			System.out.println("OpenEMS-Error [" + this.name() + "] expects [" + this.noOfParams + "] params, got ["
					+ params.length + "]");
		}
		return String.format(this.message, params);
	}

	/*
	 * Fill ALL_ERRORS map and check for duplicate Error codes.
	 */
	static {
		for (OpenemsError error : OpenemsError.values()) {
			OpenemsError duplicate = ALL_ERRORS.putIfAbsent(error.code, error);
			if (duplicate != null) {
				System.out.println("Duplicate OpenEMS-Error with code [" + error.code + "]");
			}
		}
	}

	/**
	 * Creates a OpenEMS Named Exception from this Error.
	 * 
	 * @param params the params for the Error message
	 * @return never happens
	 * @throws OpenemsNamedException always
	 */
	public OpenemsNamedException exception(Object... params) throws OpenemsNamedException {
		throw new OpenemsNamedException(this, params);
	}

	public static class OpenemsNamedException extends Exception {

		private static final long serialVersionUID = 1L;

		private final OpenemsError error;
		private final Object[] params;

		public OpenemsNamedException(OpenemsError error, Object... params) {
			super(String.format(error.getMessage(params)));
			this.error = error;
			this.params = params;
		}

		public OpenemsError getError() {
			return error;
		}

		public int getCode() {
			return this.error.getCode();
		}

		public Object[] getParams() {
			return params;
		}
	}

//	public JsonrpcResponseError asJsonrpc(UUID id, Object... params) {
//		return new JsonrpcResponseError(id, this.getCode(), String.format(this.getMessage(), params));
//	}
//
//	public CompletableFuture<JsonrpcResponseError> asJsonrpc(UUID id, Object... params) {
//		CompletableFuture<JsonrpcResponseError> result = new CompletableFuture<>();
//		result
//		
//		return new JsonrpcResponseError(id, this.getCode(), String.format(this.getMessage(), params));
//		error.completeExceptionally(new OpenemsException("Unhandled JSON-RPC method [" + request.getMethod() + "]"));
//		return error;
//	}
}
