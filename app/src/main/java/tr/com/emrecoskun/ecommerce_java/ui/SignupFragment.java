package tr.com.emrecoskun.ecommerce_java.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import tr.com.emrecoskun.ecommerce_java.AuthenticationActivity;
import tr.com.emrecoskun.ecommerce_java.MainActivity;
import tr.com.emrecoskun.ecommerce_java.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {




    public SignupFragment() {
        // Required empty public constructor
    }


    public static SignupFragment newInstance() {
        SignupFragment fragment = new SignupFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        EditText email = view.findViewById(R.id.signup_email);
        EditText password = view.findViewById(R.id.signup_password);
        EditText confirmPassword =  view.findViewById(R.id.signup_confirm_password);

        view.findViewById(R.id.signupButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().equals("") || password.getText().toString().equals("") || confirmPassword.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if(password.getText().toString().equals(confirmPassword.getText().toString())) {
                        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(
                                getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                        Log.d("task", "task is running");
                                        if(task.isSuccessful()) {
                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                            // prevent going back to this activity
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.putExtra("isLoggedIn", true);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(getActivity(), "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                        );
                    } else {
                        Toast.makeText(getActivity(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}