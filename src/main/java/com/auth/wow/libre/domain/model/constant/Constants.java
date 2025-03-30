package com.auth.wow.libre.domain.model.constant;


public class Constants {
    private Constants() {
    }

    // CONSTANT
    public static final String CONSTANT_UNIQUE_ID = "uniqueID";
    public static final String CONSTANT_ROL_JWT_PROP = "roles";
    // HEADERS
    public static final String HEADER_TRANSACTION_ID = "transaction_id";

    /* JWT HEADERS */
    public static final String HEADER_EMAIL = "x-email";
    public static final String HEADER_USER_ID = "x-user-id";
    public static final String PARAM_ACCOUNT_ID = "account_id";

    public static class Errors {
        public static final String CONSTANT_GENERIC_ERROR_MESSAGE = "An unexpected error has occurred and it was not " +
                "possible to authenticate to the system, please try again later";
        public static final String CONSTANT_ERROR_NOT_POSSIBLE_OBTAIN_CHARACTER = "It was not possible to obtain the " +
                "requested character";
    }
}
