package lucascb.appaluno;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

/**
 * Created by lucas on 19/10/16.
 */

public class CadastroHelper {
    private EditText editTextNome;
    private EditText editTextTelefone;
    private EditText editTextSite;
    private EditText editTextEmail;
    private EditText editTextEndereco;
    private SeekBar seekBarNota;
    private ImageView imageViewFoto;
    private Aluno aluno;

    public CadastroHelper(CadastroActivity activity) {
        this.editTextNome = (EditText) activity.findViewById(R.id.editTextNome);
        this.editTextTelefone = (EditText) activity.findViewById(R.id.editTextTelefone);
        this.editTextSite = (EditText) activity.findViewById(R.id.editTextSite);
        this.editTextEmail = (EditText) activity.findViewById(R.id.editTextEmail);
        this.editTextEndereco = (EditText) activity.findViewById(R.id.editTextEndereco);
        this.seekBarNota = (SeekBar) activity.findViewById(R.id.seekBarNota);
        this.imageViewFoto = (ImageView) activity.findViewById(R.id.imageViewFoto);

        this.aluno = new Aluno();
    }

    public Aluno getAluno() {
        aluno.setNome(this.editTextNome.getText().toString());
        aluno.setTelefone(this.editTextTelefone.getText().toString());
        aluno.setSite(this.editTextSite.getText().toString());
        aluno.setEmail(this.editTextEmail.getText().toString());
        aluno.setEndereco(this.editTextEndereco.getText().toString());
        aluno.setNota(this.seekBarNota.getProgress());

        return aluno;
    }

    public void setAluno(Aluno aluno) {
        editTextNome.setText(aluno.getNome());
        editTextTelefone.setText(aluno.getTelefone());
        editTextSite.setText(aluno.getSite());
        editTextEmail.setText(aluno.getEmail());
        editTextEndereco.setText(aluno.getEndereco());
        seekBarNota.setProgress(aluno.getNota());
        if (aluno.getFoto() != null) {
            this.carregarFoto(aluno.getFoto());
        }

        this.aluno = aluno;
    }

    public void carregarFoto(String localFoto) {
        Bitmap imagemFoto = BitmapFactory.decodeFile(localFoto);

        Bitmap imagemFotoReduzida = Bitmap.createScaledBitmap(imagemFoto, 100, 100, true);
        aluno.setFoto(localFoto);

        this.imageViewFoto.setImageBitmap(imagemFotoReduzida);
    }

    public ImageView getFoto() {
        return this.imageViewFoto;
    }

}
