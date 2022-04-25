package iotree.authservice.constants;

public enum ResultCode {
    SUCCESS(0),
    BAD_CREDENTIALS(-1),
    INVALID_REQUEST(-2),
    UNKNOWN_ERR(-99);

    private final int value;

    ResultCode(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
