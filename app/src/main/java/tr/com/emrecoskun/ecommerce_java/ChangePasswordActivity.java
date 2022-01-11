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
//emre
public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        EditText currentPassword = (EditText) findViewById(R.id.currentPassword);
        EditText newPassword = (EditText) findViewById(R.id.newPassword);
        EditText newPasswordAgain = (EditText) findViewById(R.id.newPasswordAgain);

        Button changePasswordButton = (Button) findViewById(R.id.changePasswordButton);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentPasswordStr =currentPassword.getText().toString();
                String newPasswordStr =newPassword.getText().toString();
                String newPasswordAgainStr =newPasswordAgain.getText().toString();
                if(currentPasswordStr.equals("") || newPasswordStr.equals("") || newPasswordAgainStr.equals("")) {
                    Toast.makeText(ChangePasswordActivity.this, "Please fill empty fields.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!newPasswordStr.equals(newPasswordAgainStr)) {
                    Toast.makeText(ChangePasswordActivity.this, "Passwords do not match!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                String email = currentUser.getEmail();
                // create credential for checkin user
                AuthCredential credential = EmailAuthProvider.getCredential(email, currentPassword.getText().toString());

                // check user if passwords are correct change password
                currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            currentUser.updatePassword(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(ChangePasswordActivity.this, "Password is changed",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                                        // prevent going back to this activity
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(ChangePasswordActivity.this, "Please check your fields.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "Old password is not correct!",
                                    Toast.LENGTH_SHORT).show();
                        }
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