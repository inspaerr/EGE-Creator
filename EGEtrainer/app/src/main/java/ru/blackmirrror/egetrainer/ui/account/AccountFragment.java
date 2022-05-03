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

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


import ru.blackmirrror.egetrainer.Models.User;

public class AccountFragment extends Fragment {

    private AccountViewModel accountViewModel;
    private FragmentAccountBinding binding;

    TextView textViewEmail, textViewPassword, textViewFirstName, textViewLastName, textViewName;

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
        textViewEmail = root.findViewById(R.id.textViewEmail);
        textViewPassword = root.findViewById(R.id.textViewPassword);
        logout_btn = root.findViewById(R.id.logout);
       // textViewFirstName = root.findViewById(R.id.textViewFirstName);
        // textViewLastName = root.findViewById(R.id.textViewLastName);
        textViewName = root.findViewById(R.id.textViewName);

        // Получаем юзера и его поля итд
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference users = database.getReference("Users");
        DatabaseReference uidRef = users.child(uid);
        uidRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            textViewEmail.setText(user.getEmail());
                            textViewName.setText(user.getFirstName() + " " + user.getLastName());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


        /* final TextView textView = binding.textAccount;
        accountViewModel.getText().

                observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        textView.setText(s);
                    }
                });*/
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);

        //sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String email = sharedPreferences.getString(KEY_EMAIL, null);
        String password = sharedPreferences.getString(KEY_PASSWORD, null);

        if (email != null || password != null) {
            textViewEmail.setText("Email: " + email);
            textViewPassword.setText("Password: " + password);
        }


        //textViewFirstName.setText(user.getFirstName());

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