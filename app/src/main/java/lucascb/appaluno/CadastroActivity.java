package lucascb.appaluno;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class CadastroActivity extends Activity {
    private Button botao;
    private CadastroHelper helper;
    private Aluno alunoParaSerAlterado = null;
    private String localArquivo;
    private final int FAZER_FOTO = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        this.helper = new CadastroHelper(this);
        this.helper.getFoto().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localArquivo = Environment.getExternalStorageDirectory() + "/" +
                        System.currentTimeMillis() + ".jpg";

                File arquivo = new File(localArquivo);
                Uri localFoto = Uri.fromFile(arquivo);

                Intent irParaCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                irParaCamera.putExtra(MediaStore.EXTRA_OUTPUT, localFoto);

                // Pede permissao para utilizar a camera
                ActivityCompat.requestPermissions(CadastroActivity.this,
                        new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 3);

                int permissionCheck = ContextCompat.checkSelfPermission(CadastroActivity.this,
                        Manifest.permission.CAMERA);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(irParaCamera, FAZER_FOTO);
                }
                else {
                    Toast.makeText(CadastroActivity.this, "Nao foi possivel abrir a camera",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        this.alunoParaSerAlterado = (Aluno) getIntent().getSerializableExtra("ALUNO_SELECIONADO");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FAZER_FOTO) {
            if (resultCode == Activity.RESULT_OK) {
                this.helper.carregarFoto(this.localArquivo);
            }
            else {
                this.localArquivo = null;
            }
        }
    }
}
