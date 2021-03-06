package restaurant.votingsystem.util.exception;

public class NotFoundException extends ApplicationException {
    public static final String NOT_FOUND_EXCEPTION = "exception.common.notFound";
    public static final String NOT_CONTAIN_EXCEPTION = "exception.common.notContain";

    //  http://stackoverflow.com/a/22358422/548473
    public NotFoundException(String[] arg, String error) {
        super(ErrorType.DATA_NOT_FOUND, error, arg);
    }
}