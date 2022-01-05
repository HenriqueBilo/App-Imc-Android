package co.tiagoaguiar.codelab.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SqlHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "fitness_Tracker.db";
    private static final int DB_VERSION = 1;

    //Para criar apenas um objeto dessa classe  - Padrão SINGLETON (é um design pattern)
    private static SqlHelper INSTANCE;

    static SqlHelper getInstance(Context context){
        if(INSTANCE == null)
            INSTANCE = new SqlHelper(context);
        return INSTANCE;
    }

    private SqlHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //Entra aqui toda vez que o arquivo .db não existir
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE calc (id INTEGER primary key, type_calc TEXT, res DECIMAL, created_date DATETIME)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    long addItem(String type, double response){
        SQLiteDatabase db = getWritableDatabase(); //Para escrever no banco de dados

        long calcId = 0;
        try{
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("type_calc", type);
            values.put("res", response);

            //Pegar a data atual em um formato mais amigável
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("pt", "BR"));
            String now = sdf.format(new Date());

            values.put("created_date", now);
            calcId = db.insertOrThrow("calc", null, values); //Insere ou gera exceção - INSERT INTO calc (column)

            db.setTransactionSuccessful(); //Efetiva a operação

        }catch (Exception e){
            Log.e("SQLite", e.getMessage(), e);
        } finally{
            if(db.isOpen())
                db.endTransaction();
        }

        return calcId;
    }
}
