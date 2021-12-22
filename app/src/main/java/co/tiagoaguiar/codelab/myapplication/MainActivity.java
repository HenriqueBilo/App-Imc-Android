package co.tiagoaguiar.codelab.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

	//private View btnImc;
	private RecyclerView rvMain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		rvMain = findViewById(R.id.rv_main);

		//Definir o comportamento de exibição do layour da recyclerView, que podem ser:
		// mosaic
		// grid
		// linear (horizontal | vertical)
		rvMain.setLayoutManager(new LinearLayoutManager(this));

		MainAdapter adapter = new MainAdapter();
		rvMain.setAdapter(adapter);

		//btnImc = findViewById(R.id.btn_imc);

		/*btnImc.setOnClickListener(view -> {
			Intent intent = new Intent(MainActivity.this, ImcActivity.class);
			startActivity(intent);
		});*/


	}

	//
	private class MainAdapter extends RecyclerView.Adapter<MainViewHolder>{

		//Layout que vai aparecer
		@NonNull
		@Override
		public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			return new MainViewHolder(getLayoutInflater().inflate(R.layout.main_item, parent, false));
		}

		//Toda vez que a recycler view aparece na tela, ele só vai allterando o conteudo
		@Override
		public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
			holder.bind(position);
		}

		//Quantidade de itens da lista
		@Override
		public int getItemCount() {
			return 15;
		}
	}

	//View da celula que está dentro do RecyclerView
	private class MainViewHolder extends RecyclerView.ViewHolder {

		public MainViewHolder(@NonNull View itemView) {
			super(itemView);
		}

		public void bind(int position){
			TextView textTest = itemView.findViewById(R.id.textView_teste);
			textTest.setText("teste de rolagem: " + position);
		}
	}
}