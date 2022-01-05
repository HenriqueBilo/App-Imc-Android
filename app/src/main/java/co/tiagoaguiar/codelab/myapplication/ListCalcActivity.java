package co.tiagoaguiar.codelab.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListCalcActivity extends AppCompatActivity {

    private RecyclerView rvListCalc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_calc);

        Bundle extras = getIntent().getExtras();

        rvListCalc = findViewById(R.id.rv_list);

        if (extras != null) {
            String type = extras.getString("type");

            new Thread(() -> {
                List<Register> registers = SqlHelper.getInstance(this).getRegisterBy(type);

                runOnUiThread(() -> {
                    Log.d("Teste", registers.toString());
                    ListCalcAdapter adapter = new ListCalcAdapter(registers);
                    rvListCalc.setLayoutManager(new LinearLayoutManager(this));
                    rvListCalc.setAdapter(adapter);

                });
            }).start();


        }
    }

    private class ListCalcAdapter extends RecyclerView.Adapter<ListCalcAdapter.ListCalcViewHolder> {

        private List<Register> registers;

        public ListCalcAdapter(List<Register> registers) {
            this.registers = registers;
        }

        @NonNull
        @Override
        public ListCalcViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ListCalcViewHolder(getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ListCalcViewHolder holder, int position) {
            Register data = registers.get(position);
            holder.bind(data);
        }

        @Override
        public int getItemCount() {
            return registers.size();
        }

        private class ListCalcViewHolder extends RecyclerView.ViewHolder {

            public ListCalcViewHolder(@NonNull View itemView) {
                super(itemView);
            }

            public void bind(Register data){
                String formatted = "";
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("pt", "BR"));
                    Date dateSaved = sdf.parse(data.createdDate);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("pt", "BR"));
                    formatted = dateFormat.format(dateSaved);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                ((TextView) itemView).setText(
                        getString(R.string.list_response, data.response, formatted)
                );
            }
        }
    }
}
