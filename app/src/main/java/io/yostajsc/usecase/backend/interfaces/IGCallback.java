package io.yostajsc.usecase.backend.interfaces;

/**
 * Created by nphau on 4/26/17.
 */

public interface IGCallback<S, F> {

    void onSuccessful(S s);

    void onFail(F f);

    void onExpired();
}
