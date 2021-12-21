package tr.com.emrecoskun.ecommerce_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class ChangeEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        EditText currentEmailText = (EditText) findViewById(R.id.currentEmail);
        EditText newEmailText = (EditText) findViewById(R.id.newEmail);
        EditText passwordText = (EditText) findViewById(R.id.emailConfirmPassword);

        Button changePasswordButton = (Button) findViewById(R.id.changeEmailButton);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentEmail = currentEmailText.getText().toString();
                String newEmail = newEmailText.getText().toString();
                String password = passwordText.getText().toString();

                String email = currentUser.getEmail();

                if(currentEmail.equals("") || newEmail.equals("") || password.equals("")) {
                    Toast.makeText(ChangeEmailActivity.this, "Please fill empty fields.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!email.equals(currentEmail)) {
                    Toast.makeText(ChangeEmailActivity.this, "Your email does not match.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get auth credentials from the user for re-authentication
                AuthCredential credential = EmailAuthProvider
                        .getCredential(email, password);

                currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        currentUser.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(ChangeEmailActivity.this, "Your email is changed.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ChangeEmailActivity.this, MainActivity.class);
                                    // prevent going back to this activity
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(ChangeEmailActivity.this, "Your email is not changed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

            }
        });


        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.back);

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}