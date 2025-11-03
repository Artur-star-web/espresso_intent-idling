package ru.kkuzmichev.simpleappforespresso.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ru.kkuzmichev.simpleappforespresso.R;
import ru.kkuzmichev.simpleappforespresso.SimpleIdlingResource;
import ru.kkuzmichev.simpleappforespresso.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;
    private RecyclerView recyclerView;
    private ArrayList<GalleryItem> itemList = new ArrayList<>();
    private FloatingActionButton fab;
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        fab.setClickable(false);

        progressBar = root.findViewById(R.id.progress_bar);
        recyclerView = root.findViewById(R.id.recycle_view);

        setupRecyclerView();
        observeData();


        startDataLoading();

        return root;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new GalleryAdapter(itemList));
    }

    private void observeData() {

        galleryViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
                recyclerView.setVisibility(isLoading ? View.INVISIBLE : View.VISIBLE);
            }
        });


        galleryViewModel.getGalleryData().observe(getViewLifecycleOwner(), data -> {
            if (data != null) {
                itemList.clear();
                itemList.addAll(data);
                recyclerView.getAdapter().notifyDataSetChanged();


                SimpleIdlingResource.decrement();
            }
        });
    }

    private void startDataLoading() {

        SimpleIdlingResource.increment();


        galleryViewModel.loadGalleryData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}