package lucascb.appaluno;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CadastroActivity extends Activity {
    private Button botao;
    private CadastroHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        this.helper = new CadastroHelper(this);

        final Aluno alunoParaSerAlterado = (Aluno) getIntent().getSerializableExtra("ALUNO_SELECIONADO");
        if (alunoParaSerAlterado != null) {
            helper.setAluno(alunoParaSerAlterado);
        }

        this.botao = (Button) this.findViewById(R.id.buttonCadastrar);
        this.botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Aluno aluno = helper.getAluno();

                AlunoDAO dao = new AlunoDAO(CadastroActivity.this);
                if (alunoParaSerAlterado == null) {
                    dao.cadastrarAluno(aluno);
                }
                else {
                    dao.alterarAluno(aluno);
                }
                dao.close();

                finish();
            }
        });
    }

}
