package lucascb.appaluno;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

public class CadastroActivity extends Activity {
    private Button botao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        this.botao = (Button) this.findViewById(R.id.buttonCadastrar);
        this.botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Log.i("CADASTRO_ALUNO", "Formulario encerrado");
            }
        });
    }

}
