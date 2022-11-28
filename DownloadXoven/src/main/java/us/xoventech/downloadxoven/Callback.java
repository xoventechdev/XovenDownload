package us.xoventech.downloadxoven;


import us.xoventech.downloadxoven.engine.Response;

/**
 * Created by nickyang on 2018/3/28.
 */

public interface Callback {
    void onSuccess(Response response);
}
