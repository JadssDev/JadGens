package ml.jadss.jadgens.utils;


public class APIException extends RuntimeException {

    /**
     * Summon a API Exception
     * @param message the message why it happened
     * @param cause the cause
     */
    public APIException(String message, Throwable cause) {
        super(message, cause, false, true);
    }


    /**
     * Summon a API Exception
     * @param message the message.
     */
    public APIException(String message) {
        super(message);
    }

}
