package ru.blackmirrror.egetrainer.ui.account;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ru.blackmirrror.egetrainer.R;
import ru.blackmirrror.egetrainer.TempActivity;
import ru.blackmirrror.egetrainer.databinding.FragmentAccountBinding;

public class AccountFragment extends Fragment {

    private AccountViewModel accountViewModel;
    private FragmentAccountBinding binding;

    TextView textViewEmail, textViewPassword;
    Button logout_btn;
    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_EMAIL = "name";
    private static final String KEY_PASSWORD = "email";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textAccount;
        accountViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        textViewEmail = root.findViewById(R.id.textViewEmail);
        textViewPassword = root.findViewById(R.id.textViewPassword);
        logout_btn = root.findViewById(R.id.logout);

        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);

        //sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String email = sharedPreferences.getString(KEY_EMAIL, null);
        String password = sharedPreferences.getString(KEY_PASSWORD, null);

        if (email != null || password != null){
            textViewEmail.setText("Email: "+ email);
            textViewPassword.setText("Password: "+ password);
        }

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(getActivity(), "Log out successfully", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}