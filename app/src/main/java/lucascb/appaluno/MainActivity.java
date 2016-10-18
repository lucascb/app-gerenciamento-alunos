package lucascb.appaluno;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
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
        listViewAlunos.setAdapter(this.adapter);
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
}
