package restaurant.votingsystem.util.exception;

public class NotContainException extends ApplicationException{
    public static final String NOT_CONTAIN_EXCEPTION = "exception.common.notContain";

    public NotContainException(String[] arg) {
        super(ErrorType.DATA_NOT_FOUND, NOT_CONTAIN_EXCEPTION, arg);
    }
}
