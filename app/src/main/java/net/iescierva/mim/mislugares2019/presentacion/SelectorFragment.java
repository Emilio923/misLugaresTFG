package net.iescierva.mim.mislugares2019.presentacion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.iescierva.mim.mislugares2019.Aplicacion;
import net.iescierva.mim.mislugares2019.R;
import net.iescierva.mim.mislugares2019.casos_uso.CasosUsoLugar;
import net.iescierva.mim.mislugares2019.datos.AdaptadorLugaresBD;
import net.iescierva.mim.mislugares2019.datos.LugaresBD;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectorFragment extends Fragment {
    private LugaresBD lugares;
    private AdaptadorLugaresBD adaptador;
    private CasosUsoLugar usoLugar;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflador,
                             ViewGroup contenedor,
                             Bundle savedInstanceState) {
        View vista = inflador.inflate(R.layout.fragment_selector, contenedor, false);
        recyclerView = vista.findViewById(R.id.recycler_view);
        return vista;
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        lugares = ((Aplicacion) getActivity().getApplication()).getLugares();
        adaptador = ((Aplicacion) getActivity().getApplication()).getAdaptador();
        usoLugar = new CasosUsoLugar(getActivity(), this, lugares, adaptador);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adaptador);
        adaptador.setOnItemClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int pos = (Integer)(v.getTag());
                usoLugar.mostrar(pos);
            }
        });
    }
}