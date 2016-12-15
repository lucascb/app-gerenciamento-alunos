package lucascb.appaluno;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by lucas on 15/12/16.
 */

public interface AlunosAPI {

    @GET("getAll")
    Call<List<Aluno>> getAlunos();

    @POST("saveAll")
    Call<ResponseBody> postAlunos(@Body List<Aluno> alunos);
}
