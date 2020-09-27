package restaurant.votingsystem.util.exception;

public class OverTimeVoteException extends ApplicationException{
    private static final String EXCEPTION_TIME_IS_AFTER_VOTE = "exception.vote.overTime";

    public OverTimeVoteException() {
        super(ErrorType.WRONG_REQUEST, EXCEPTION_TIME_IS_AFTER_VOTE);
    }
}
