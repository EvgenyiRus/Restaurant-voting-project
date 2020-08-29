package restaurant.votingsystem.util.exception;

public class NotSuchElementException extends ApplicationException {
    public static final String NOT_FOUND_EXCEPTION = "exception.common.notFound";
    public static final String NOT_CONTAIN_EXCEPTION = "exception.common.notContain";

    public NotSuchElementException(String[] arg) {
        super(ErrorType.DATA_NOT_FOUND, NOT_FOUND_EXCEPTION, arg);
    }
}