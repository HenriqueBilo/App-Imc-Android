package co.tiagoaguiar.codelab.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class ListCalcActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_calc);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            String type = extras.getString("type");
            List<Register> registers = SqlHelper.getInstance(this).getRegisterBy(type);
            Log.d("Teste", registers.toString());
        }
    }
}