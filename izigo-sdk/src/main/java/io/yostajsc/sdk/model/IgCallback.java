package io.yostajsc.sdk.model;

/**
 * Created by nphau on 4/26/17.
 */

public interface IgCallback<S, F> {

    void onSuccessful(S s);

    void onFail(F error);

    void onExpired();
}
