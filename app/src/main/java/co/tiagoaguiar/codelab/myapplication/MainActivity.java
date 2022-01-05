package co.tiagoaguiar.codelab.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	//private View btnImc;
	private RecyclerView rvMain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		rvMain = findViewById(R.id.rv_main);

		List<MainItem> mainItems = new ArrayList<>();
		mainItems.add(new MainItem(1, R.drawable.ic_baseline_wb_sunny_24, R.string.label_imc, Color.GREEN));
		mainItems.add(new MainItem(2, R.drawable.ic_baseline_wine_bar_24, R.string.label_tmb, Color.YELLOW));

		//Definir o comportamento de exibição do layour da recyclerView, que podem ser:
		// mosaic
		// grid
		// linear (horizontal | vertical)
		rvMain.setLayoutManager(new GridLayoutManager(this, 2));

		MainAdapter adapter = new MainAdapter(mainItems);
		adapter.setListener(id -> {
			//Para abrir outra tela
			switch(id){
				case 1:
					startActivity(new Intent(MainActivity.this, ImcActivity.class));
					break;
				case 2:
					startActivity(new Intent(MainActivity.this, TmbActivity.class));
					break;
			}
		});
		rvMain.setAdapter(adapter);

	}

	private class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder>{

		private List<MainItem> mainItems;
		private OnItemClickListener listener;

		public MainAdapter(List<MainItem> mainItems){
			this.mainItems = mainItems;
		}

		//Layout que vai aparecer
		@NonNull
		@Override
		public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			return new MainViewHolder(getLayoutInflater().inflate(R.layout.main_item, parent, false));
		}

		//Toda vez que a recycler view aparece na tela, ele só vai allterando o conteudo
		@Override
		public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
			MainItem mainItemCurrent = mainItems.get(position);
			holder.bind(mainItemCurrent);
		}

		//Quantidade de itens da lista
		@Override
		public int getItemCount() {
			return mainItems.size();
		}

		public OnItemClickListener getListener() {
			return listener;
		}

		public void setListener(OnItemClickListener listener) {
			this.listener = listener;
		}

		//View da celula que está dentro do RecyclerView
		private class MainViewHolder extends RecyclerView.ViewHolder {

			public MainViewHolder(@NonNull View itemView) {
				super(itemView);
			}

			public void bind(MainItem item){
				TextView txtName = itemView.findViewById(R.id.item_txt_name);
				ImageView imgIcon = itemView.findViewById(R.id.item_img_icon);
				LinearLayout btnImc = (LinearLayout) itemView.findViewById(R.id.btn_imc); //Para trocar o background do container

				btnImc.setOnClickListener(view -> {
					listener.onClick(item.getId());
				});

				//Seta os valores nos respectivos campos
				txtName.setText(item.getTextStringId());
				imgIcon.setImageResource(item.getDrawableId());
				btnImc.setBackgroundColor(item.getColor());
			}
		}
	}
}