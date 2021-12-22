package co.tiagoaguiar.codelab.myapplication;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ImcActivity extends AppCompatActivity {

    private EditText editHeight;
    private EditText editWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc);

        editHeight = findViewById(R.id.edit_imc_height);
        editWeight = findViewById(R.id.edit_imc_weight);

        Button btnSend = findViewById(R.id.btn_imc_send);

        //Declarando click com lambda:
        //btnSend.setOnClickListener( (view) -> {
        //});

        btnSend.setOnClickListener(view -> {
            if(!validate()){
                Toast.makeText(ImcActivity.this, R.string.fields_message, Toast.LENGTH_LONG).show();
                return;
            }

            String sHeight = editHeight.getText().toString();
            String sWeight = editWeight.getText().toString();

            int iHeight = Integer.parseInt(sHeight);
            int iWeight = Integer.parseInt(sWeight);

            double result = calculateImc(iHeight, iWeight);
            Log.d("TESTE", "resultado: " + result);

            int imcResponseId = imcResponse(result);

            AlertDialog dialog = new AlertDialog.Builder(ImcActivity.this)
                    .setTitle(getString(R.string.imc_response, result))
                    .setMessage(imcResponseId)
                    .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {


                    })
                    .create();

            dialog.show();

            //Esconde o teclado
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //Da hide nos dois pois não sabemos qual input o usuário clicou
            imm.hideSoftInputFromWindow(editWeight.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(editHeight.getWindowToken(), 0);
        });
    }

    @StringRes //Obriga a retornar um inteiro de resource
    private int imcResponse(double imc){
        if(imc < 15)
            return R.string.imc_severely_low_weight;
        else if (imc < 16)
            return R.string.imc_very_low_weight;
        else if(imc < 18.5)
            return R.string.imc_low_weight;
        else if(imc < 25)
            return R.string.normal;
        else if(imc < 30)
            return R.string.imc_high_weight;
        else if(imc < 35)
            return R.string.imc_so_high_weight;
        else if(imc < 40)
            return R.string.imc_severely_high_weight;
        else
            return R.string.imc_extreme_weight;

    }

    private double calculateImc(int height, int weight){
        // peso / (altura * altura)
        return weight / ( ((double) height / 100) * ((double) height / 100) ); //Divide por 100
    }

    private boolean validate() {
        //Se ambos inputs não começarem com 0 e não forem vazios, passa na validação (true)
        //Caso contrário, retorna false
        return (!editHeight.getText().toString().startsWith("0")
                && !editWeight.getText().toString().startsWith("0")
                && !editHeight.getText().toString().isEmpty()
                && !editWeight.getText().toString().isEmpty());
    }
}