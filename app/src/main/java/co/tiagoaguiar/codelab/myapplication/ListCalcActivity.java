package co.tiagoaguiar.codelab.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

    private class ListCalcAdapter extends RecyclerView.Adapter<ListCalcAdapter.ListCalcViewHolder> implements OnAdapterItemClickListener{

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

            // listener para ouvir evento de click e de long-click (segurar touch)
            holder.bind(data, this);
        }

        @Override
        public int getItemCount() {
            return registers.size();
        }

        @Override
        public void onClick(int id, String type) {
            switch(type){
                case "imc":
                    Intent intentImc = new Intent(ListCalcActivity.this, ImcActivity.class);
                    intentImc.putExtra("updateId", id);
                    startActivity(intentImc);
                    break;
                case "tmb":
                    Intent intentTmb = new Intent(ListCalcActivity.this, TmbActivity.class);
                    intentTmb.putExtra("updateId", id);
                    startActivity(intentTmb);
                    break;
            }
        }

        @Override
        public void onLongClick(int position, String type, int id) {
            AlertDialog dialog = new AlertDialog.Builder(ListCalcActivity.this)
                    .setTitle("Exclusão de registro")
                    .setMessage("Deseja excluir esse registro?") //getString(R.string.tmb_response, tmbResponseId)
                    .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {

                        new Thread(() -> {
                            Boolean deleteItem = SqlHelper.getInstance(ListCalcActivity.this).removeItem(id);

                            //Retorna para Thread Principal
                            runOnUiThread(() -> {
                                if (deleteItem) {
                                    Toast.makeText(ListCalcActivity.this, R.string.calc_removed, Toast.LENGTH_SHORT).show();
                                    registers.remove(position);
                                    notifyDataSetChanged();
                                }

                            });
                        }).start();

                    })
                    .setNegativeButton(R.string.cancel, ((dialogInterface, i) -> {

                    })).create();

            dialog.show();
        }

        private class ListCalcViewHolder extends RecyclerView.ViewHolder {

            public ListCalcViewHolder(@NonNull View itemView) {
                super(itemView);
            }

            public void bind(Register data, final OnAdapterItemClickListener onItemListener){
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

                //onClick para efetuar a alteração de registros
                itemView.setOnClickListener(view -> {
                    onItemListener.onClick(data.id, data.type);
                });

                //Long click para efetuar a exclusão de registros
                itemView.setOnLongClickListener(view -> {
                    onItemListener.onLongClick(getAdapterPosition(), data.type, data.id);
                    return false;
                });

            }
        }
    }
}
