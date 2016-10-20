package lucascb.appaluno;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 19/10/16.
 */

public class AlunoDAO extends SQLiteOpenHelper {
    private static final int VERSAO = 4;
    private static final String TABELA = "Aluno";
    private static final String DATABASE = "MPAAlunos";
    private static final String TAG = "CADASTRO_ALUNO";

    public AlunoDAO(Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String ddl = "CREATE TABLE " + TABELA + " (id INTEGER PRIMARY KEY, nome TEXT, " +
                "telefone TEXT, endereco TEXT, site TEXT, email TEXT, foto TEXT, nota INTEGER);";
        db.execSQL(ddl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABELA;
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

        this.getWritableDatabase().insert(TABELA, null, values);
        Log.i(TAG, "Aluno cadastrado: " + aluno.getNome());
    }

    public List<Aluno> getAlunos() {
        List<Aluno> lista = new ArrayList<>();
        String sql = "SELECT * FROM Aluno ORDER BY nome";
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);

        try {
            while (cursor.moveToNext()) {
                Aluno aluno = new Aluno();

                aluno.setId(cursor.getInt(0));
                aluno.setNome(cursor.getString(1));
                aluno.setTelefone(cursor.getString(2));
                aluno.setEndereco(cursor.getString(3));
                aluno.setSite(cursor.getString(4));
                aluno.setEmail(cursor.getString(5));
                aluno.setFoto(cursor.getString(6));
                aluno.setNota(cursor.getInt(7));

                lista.add(aluno);
            }
        }
        catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
        finally {
            cursor.close();
        }

        return lista;
    }

    public void alterarAluno(Aluno aluno) {
        ContentValues values = new ContentValues();

        values.put("nome", aluno.getNome());
        values.put("telefone", aluno.getTelefone());
        values.put("endereco", aluno.getEndereco());
        values.put("site", aluno.getSite());
        values.put("email", aluno.getEmail());
        values.put("foto", aluno.getFoto());
        values.put("nota", aluno.getNota());

        String[] args = { Integer.valueOf(aluno.getId()).toString() };
        this.getWritableDatabase().update(TABELA, values, "id=?", args);
        Log.i(TAG, "Aluno alterado: " + aluno.getNome());
    }

    public void excluirAluno(Aluno aluno) {
        String[] args = { Integer.valueOf(aluno.getId()).toString() };
        getWritableDatabase().delete(TABELA, "id=?", args);
        Log.i(TAG, "Aluno excluido: " + aluno.getNome());
    }

}
