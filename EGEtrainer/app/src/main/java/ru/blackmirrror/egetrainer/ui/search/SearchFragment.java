package ru.blackmirrror.egetrainer.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import ru.blackmirrror.egetrainer.Questions2Activity;
import ru.blackmirrror.egetrainer.R;
import ru.blackmirrror.egetrainer.SearchNewActivity;
import ru.blackmirrror.egetrainer.databinding.FragmentSearchBinding;

public class SearchFragment extends Fragment implements View.OnClickListener{

    private SearchViewModel searchViewModel;
    private FragmentSearchBinding binding;

    Button mathBase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSearch;
        searchViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        
        mathBase = root.findViewById(R.id.button2);
        mathBase.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), Questions2Activity.class);
        switch (v.getId()) {
            case (R.id.button2):{
                intent.putExtra("sub", "mathBase");
            }
            default:{
                intent.putExtra("sub", "mathBase");
            }
        }
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}