package map.cookability;

/**
 * Created by xiaochengjiang on 3/17/18.
 */

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestInterface {

    @GET("android/jsonandroid")
    Call<JSONResponse> getJSON();
}
