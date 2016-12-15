package lucascb.appaluno;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lucas on 14/12/16.
 */

public class AlunoAPI {
    private static final String BASE_URL = "http://10.15.92.127:8080/CM/webresources/aluno/";
    private static Retrofit retrofit = null;
    private static AlunosAPI apiService = null;

    public static AlunosAPI getAPI() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiService = retrofit.create(AlunosAPI.class);
        }
        return apiService;
    }

}
