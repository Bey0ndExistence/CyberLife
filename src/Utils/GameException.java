package Utils;

public class GameException extends Exception {
    private int errorCode;
    private String errorCategory;

    public GameException(String message, int errorCode, String errorCategory) {
        super(message);
        this.errorCode = errorCode;
        this.errorCategory = errorCategory;
    }

    public GameException(String message, Throwable cause, int errorCode, String errorCategory) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorCategory = errorCategory;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorCategory() {
        return errorCategory;
    }

    @Override
    public String toString() {
        return "GameException{" +
                "errorCode=" + errorCode +
                ", errorCategory='" + errorCategory + '\'' +
                ", message='" + getMessage() + '\'' +
                '}';
    }
}
