package io.yostajsc.izigo.usecase.trip;

/**
 * Created by nphau on 5/12/17.
 */

public class AddTripActivityHelper {

    private static final String ERROR_UNBOUND = "You must bind first!";

    private static AddTripActivity mActivity = null;
    private static AddTripActivityHelper mInstance = null;

    private AddTripActivityHelper(AddTripActivity activity) {
        mActivity = activity;
    }

    public static synchronized AddTripActivityHelper bind(AddTripActivity activity) {
        mInstance = new AddTripActivityHelper(activity);
        return mInstance;
    }


    public static AddTripActivityHelper getInstance() {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UNBOUND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mInstance;
    }
}
