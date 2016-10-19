package lucascb.appaluno;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    // Constantes
    private final String TAG = "CADASTRO_ALUNO";
    private final String ALUNOS_KEY = "LISTA";
    // Atributos da tela
    private EditText editTextNome;
    private Button buttonAddAluno;
    private ListView listViewAlunos;
    private List<String> listaAlunos;
    private ArrayAdapter<String> adapter;
    private int adapterLayout = android.R.layout.simple_list_item_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Pega os elementos do layout
        this.editTextNome   = (EditText) findViewById(R.id.editTextNome);
        this.buttonAddAluno = (Button)   findViewById(R.id.buttonAddAluno);
        this.listViewAlunos = (ListView) findViewById(R.id.listViewAlunos);
        // Cria a lista de alunos
        this.listaAlunos = new ArrayList<>();
        this.adapter = new ArrayAdapter<>(this, adapterLayout, listaAlunos);
        this.listViewAlunos.setAdapter(this.adapter);

        this.listViewAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Aluno: " + listaAlunos.get(position), Toast.LENGTH_LONG).show();
            }
        });

        this.listViewAlunos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(MainActivity.this, "Aluno: " + listaAlunos.get(position) + "[clique longo]",
                    Toast.LENGTH_LONG).show();
                return true; // Nao executa o clique simples
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(ALUNOS_KEY, (ArrayList<String>) this.listaAlunos);
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState(): " + listaAlunos);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.listaAlunos = savedInstanceState.getStringArrayList(ALUNOS_KEY);
        Log.i(TAG, "onSaveRestoreState(): " + this.listaAlunos);
    }

    public void onClickAddAluno(View view) {
        // Pega o texto do EditText
        String nome = editTextNome.getText().toString();
        if (nome.length() > 0) {
            // Se o texto nao for vazio, insere na lista de alunos
            this.listaAlunos.add(nome);
            this.editTextNome.setText("");
            this.adapter.notifyDataSetChanged();
        }
        else {
            // Senao sinaliza um erro
            Toast.makeText(MainActivity.this, R.string.erroAddAluno, Toast.LENGTH_LONG).show();
        }
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
