package co.tiagoaguiar.codelab.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class TmbActivity extends AppCompatActivity {

    private EditText editHeight;
    private EditText editWeight;
    private EditText editAge;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmb);

        editHeight = findViewById(R.id.edit_tmb_height);
        editWeight = findViewById(R.id.edit_tmb_weight);
        editAge = findViewById(R.id.edit_tmb_age);
        spinner = findViewById(R.id.spinner_tmb_lifestyle);

        Button btnSend = findViewById(R.id.btn_tmb_send);
        btnSend.setOnClickListener(view -> {
            if (!validate()) {
                Toast.makeText(TmbActivity.this, R.string.fields_message, Toast.LENGTH_LONG).show();
                return;
            }

            String sHeight = editHeight.getText().toString();
            String sWeight = editWeight.getText().toString();
            String sAge = editAge.getText().toString();

            int iHeight = Integer.parseInt(sHeight);
            int iWeight = Integer.parseInt(sWeight);
            int iAge = Integer.parseInt(sAge);

            double result = calculateTmb(iHeight, iWeight, iAge);
            double tmbResponseId = tmbResponse(result);

            AlertDialog dialog = new AlertDialog.Builder(TmbActivity.this)
                    .setTitle(getString(R.string.tmb_response, result))
                    .setMessage(getString(R.string.tmb_response, tmbResponseId))
                    .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {

                    })
                    .setNegativeButton(R.string.save, ((dialogInterface, i) -> {

                        SqlHelper sqlHelper = SqlHelper.getInstance(TmbActivity.this);

                        new Thread(() -> {
                            int updateId = 0;

                            // verifica se tem ID vindo da tela anterior quando é UPDATE
                            if (getIntent().getExtras() != null)
                                updateId = getIntent().getExtras().getInt("updateId", 0);

                            long calcId;

                            // verifica se é update ou create
                            if (updateId > 0) {
                                calcId = sqlHelper.updateItem("tmb", tmbResponseId, updateId);
                            } else {
                                calcId = sqlHelper.addItem("tmb", tmbResponseId);
                            }

                            //Retorna para Thread Principal
                            runOnUiThread(() -> {
                                if (calcId > 0) {
                                    Toast.makeText(TmbActivity.this, R.string.calc_saved, Toast.LENGTH_SHORT).show();
                                    openListCalcActivity();
                                }

                            });
                        }).start();

                    }))
                    .create();

            dialog.show();

        });

        //Esconde o teclado
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //Da hide nos dois pois não sabemos qual input o usuário clicou
        imm.hideSoftInputFromWindow(editWeight.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editHeight.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editAge.getWindowToken(), 0);

    }

    //Para abrir o menu superior direito
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //Para atribuir eventos (click) aos itens do menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_list){
            openListCalcActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Realiza a chamada da activity que lista os dados do banco
    private void openListCalcActivity() {
        Intent intent = new Intent(TmbActivity.this, ListCalcActivity.class);
        intent.putExtra("type", "tmb");
        startActivity(intent);
    }

    private double calculateTmb(int height, int weight, int age) {
        return 66 + (weight * 13.8) + (5 * height) - (6.8 * age);
    }

    private double tmbResponse(double tmb){
        int index = spinner.getSelectedItemPosition();
        switch(index){
            case 0: return tmb * 1.2;
            case 1: return tmb * 1.375;
            case 2: return tmb * 1.55;
            case 3: return tmb * 1.725;
            case 4: return tmb * 1.9;
            default:
                return 0;
        }
    }

    private boolean validate() {
        //Se ambos inputs não começarem com 0 e não forem vazios, passa na validação (true)
        //Caso contrário, retorna false
        return (!editHeight.getText().toString().startsWith("0")
                && !editWeight.getText().toString().startsWith("0")
                && !editAge.getText().toString().startsWith("0")
                && !editHeight.getText().toString().isEmpty()
                && !editWeight.getText().toString().isEmpty()
                && !editAge.getText().toString().isEmpty());
    }
}