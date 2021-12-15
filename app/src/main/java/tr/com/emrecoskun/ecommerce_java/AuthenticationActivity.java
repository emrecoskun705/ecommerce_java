package tr.com.emrecoskun.ecommerce_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import tr.com.emrecoskun.ecommerce_java.ui.LoginFragment;
import tr.com.emrecoskun.ecommerce_java.ui.SignupFragment;

public class AuthenticationActivity extends AppCompatActivity {

    private boolean loginPage = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        if (loginPage){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.container, LoginFragment.newInstance(),"login");
            ft.commit();
        }else{
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.container, SignupFragment.newInstance());
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}