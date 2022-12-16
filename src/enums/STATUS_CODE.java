package enums;

public enum STATUS_CODE {
    OK(200),
    NOT_FOUND(404),
    BAD_REQUEST(400),
    NOT_IMPLEMENTED(501),
    INTERNAL_SERVER_ERROR(500),
    METHOD_NOT_ALLOWED(405);

    public int value;

    STATUS_CODE(int i) {
        this.value = i;
    }

    public static STATUS_CODE fromValue(int status) {
        for (STATUS_CODE code : STATUS_CODE.values()) {
            if (code.value == status) {
                return code;
            }
        }
        return null;
    }
}
