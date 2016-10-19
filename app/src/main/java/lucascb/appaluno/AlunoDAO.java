package lucascb.appaluno;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by lucas on 19/10/16.
 */

public class AlunoDAO extends SQLiteOpenHelper {
    private static final int VERSAO = 1;
    private static final String TABELA = "Aluno";
    private static final String DATABASE = "MPAAlunos";
    private static final String TAG = "CADASTRO_ALUNO";

    public AlunoDAO(Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String ddl = "CREATE TABLE " + this.TABELA + "( " + "id INTEGER PRIMARY KEY, " +
                "nome TEXT, email TEXT, foto TEXT, nota REAL)";
        db.execSQL(ddl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + this.TABELA;
        db.execSQL(sql);
        this.onCreate(db);
    }

    public void cadastrarAluno(Aluno aluno) {
        ContentValues values = new ContentValues();

        values.put("nome", aluno.getNome());
        values.put("telefone", aluno.getTelefone());
        values.put("endereco", aluno.getEndereco());
        values.put("site", aluno.getSite());
        values.put("email", aluno.getEmail());
        values.put("foto", aluno.getFoto());
        values.put("nota", aluno.getNota());

        this.getWritableDatabase().insert(this.TABELA, null, values);
        Log.i(this.TAG, "Aluno cadastrado: " + aluno.getNome());
    }

}
