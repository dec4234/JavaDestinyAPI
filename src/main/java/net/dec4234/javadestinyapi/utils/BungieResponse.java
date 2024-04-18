package net.dec4234.javadestinyapi.utils;

import net.dec4234.javadestinyapi.exceptions.APIException;

/**
 * Describes a response received from any HTTP request to a Bungie endpoint. This should be used to properly pass
 * errors to the place where calls are made, rather than upstream in {@link HttpUtils}. The goal is to reduce runtime
 * exceptions by allowing users to properly handle error conditions in their code, or optionally (poor decision) to
 * ignore them.
 * @param <T> The "parsed" object that is returned by the object. This could just be a JsonObject or it could be
 *           an api object like {@link net.dec4234.javadestinyapi.material.user.BungieUser}
 */
public class BungieResponse<T> {

    private T parsedResponse;
    private boolean isApiOnline = true;
    private APIException apiException = null;

    public BungieResponse(T parsedResponse) {
        this.parsedResponse = parsedResponse;
    }

    public BungieResponse(boolean isApiOnline, APIException apiException) {
        this.parsedResponse = null;
        this.isApiOnline = isApiOnline;
        this.apiException = apiException;
    }

    /**
     * Get the parsed object contained witihin, but only if {@link #isError} returns false
     * @return The parsed object
     */
    public T getParsedResponse() {
        return parsedResponse;
    }

    /**
     * Is the API online?
     * @return Returns true if the API is online. False if the bungie api returns an error that servers are offline
     */
    public boolean isApiOnline() {
        return isApiOnline;
    }

    /**
     * Was an error returned while trying to complete the request?
     * @return True if an error was returned
     */
    public boolean isError() {
        return apiException != null;
    }

    /**
     * Get the error mentioned by {@link #isError()}
     * @return An ApiException representing the error received while trying to complete the request
     */
    public APIException getApiException() {
        return apiException;
    }

    /**
     * "Inherit" the errors from a predecessor. Say when taking a BungieResponse<JsonObject> and converting it to a
     * BungieUser one, you want it to carry forward any error messages.
     * @param other The previous BungieResponse that is being parsed and then repackaged
     * @return The BungieResponse with inherited error conditions, if there are any
     */
    public BungieResponse<?> inherit(BungieResponse<?> other) {
        if(other == null) {
            return null;
        }

        this.update(other.getApiException(), other.isApiOnline);

        return this;
    }

    private void update(APIException exception, boolean isApiOnline) {
        this.apiException = exception;
        this.isApiOnline = isApiOnline;
    }
}
