package lucascb.appaluno;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends Activity {
    // Constantes
    private static final String TAG = "CADASTRO_ALUNO";
    private static final String ALUNOS_KEY = "LISTA";
    // Atributos da tela
    private ListView listViewAlunos;
    private List<Aluno> listaAlunos;
    private ArrayAdapter<Aluno> adapter;
    private Aluno alunoSelecionado = null;
    // URL da API
    public static final String BASE_URL = "http://localhost:8080/CM/webresources/aluno/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Pega os elementos do layout
        this.listViewAlunos = (ListView) findViewById(R.id.listViewAlunos);
        registerForContextMenu(this.listViewAlunos);
        // Clique simples: mostra as informacoes do aluno
        this.listViewAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent form = new Intent(MainActivity.this, CadastroActivity.class);
                alunoSelecionado = (Aluno) listViewAlunos.getItemAtPosition(position);
                form.putExtra("ALUNO_SELECIONADO", alunoSelecionado);
                startActivity(form);
            }
        });
        // Clique longo: abre o context menu
        this.listViewAlunos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                alunoSelecionado = adapter.getItem(position);
                return false; // Nao executa o clique simples
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.carregarLista();
    }

    private void carregarLista() {
        AlunoDAO dao = new AlunoDAO(this);
        this.listaAlunos = dao.getAlunos();
        dao.close();

        this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaAlunos);
        this.listViewAlunos.setAdapter(adapter);
    }

    private void excluirAluno() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Confirma a exclusão de: " + this.alunoSelecionado.getNome());
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlunoDAO dao = new AlunoDAO(MainActivity.this);
                dao.excluirAluno(alunoSelecionado);
                dao.close();
                carregarLista();
                alunoSelecionado = null;
            }
        });
        builder.setNegativeButton("Não", null);

        AlertDialog dialog = builder.create();
        dialog.setTitle("Confirmação de Operação");
        dialog.show();
    }

    /* Metodos relacionados ao MENU */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_novo:
                Intent intent = new Intent(MainActivity.this, CadastroActivity.class);
                this.startActivity(intent);
                return false;
            case R.id.menu_enviar:
                Call<ResponseBody> callPost = AlunoAPI.getAPI().postAlunos(listaAlunos);
                callPost.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            int statusCode = response.code();
                            ResponseBody r = response.body();
                            Toast.makeText(MainActivity.this, "Usuarios enviados com sucesso",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                Toast.makeText(MainActivity.this, response.errorBody().string().toString(),
                                        Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                        // Log error here since request failed
                        Toast.makeText(MainActivity.this, "Falha ao inserir os dados na API",
                                Toast.LENGTH_LONG).show();
                    }
                });
                return false;
            case R.id.menu_sincronizar:
                Call<List<Aluno>> callGet = AlunoAPI.getAPI().getAlunos();
                callGet.enqueue(new Callback<List<Aluno>>() {
                    @Override
                    public void onResponse(Call<List<Aluno>> call, Response<List<Aluno>> response) {
                        if (response.isSuccessful()) {
                            AlunoDAO dao = new AlunoDAO(MainActivity.this);
                            int statusCode = response.code();
                            List<Aluno> lista = response.body();
                            for (Aluno a : lista) {
                                dao.cadastrarAluno(a);
                            }
                            dao.close();
                            carregarLista();
                            Toast.makeText(MainActivity.this, listaAlunos.toString(),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                Toast.makeText(MainActivity.this, response.errorBody().string().toString(),
                                        Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Aluno>> call, Throwable t) {
                        // Log error here since request failed
                        t.printStackTrace();
                        Toast.makeText(MainActivity.this, "Falha ao recuperar os dados da API",
                                Toast.LENGTH_LONG).show();
                    }
                });
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_contexto, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Intent intent;
        int permissionCheck;

        switch (item.getItemId()) {
            case R.id.menu_excluir:
                this.excluirAluno();
                break;
            case R.id.menu_ligar:
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + alunoSelecionado.getTelefone()));
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                        Manifest.permission.CALL_PHONE
                }, 3);
                // Checa se o android possui permissão
                permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CALL_PHONE);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                }
                break;
            case R.id.menu_enviarSMS:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("sms:" + alunoSelecionado.getTelefone()));
                intent.putExtra("sms_body", "Mensagem de boas vindas :)");
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                        Manifest.permission.SEND_SMS
                }, 3);
                // Checa se o android possui permissão
                permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.SEND_SMS);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                }
                break;
            case R.id.menu_mapa:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setClassName("com.google.android.apps.maps",
                                    "com.google.android.maps.MapsActivity");
                intent.setData(Uri.parse("geo:0,0?z=14&q" + alunoSelecionado.getEndereco()));
                startActivity(intent);
                break;
            case R.id.menu_navegar:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http:" + alunoSelecionado.getSite()));
                startActivity(intent);
                break;
            case R.id.menu_enviarEmail:
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { alunoSelecionado.getEmail() });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Falando sobre o curso");
                intent.putExtra(Intent.EXTRA_TEXT, "O curso foi muito legal");
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }
}

