package com.eliassilva.popularmoviesstage1.customExceptions;

/**
 * Created by Elias on 19/02/2018.
 */

public class FetchDataException extends Exception {
    public FetchDataException(String message) {
        super(message);
    }
}
