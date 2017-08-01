package com.example.ranieriaguiar.testfirebase1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateEmailActivity extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty(messageResId = R.string.branco)
    @Email(messageResId = R.string.email_falho)
    @BindView(R.id.email)
    EditText mEmail;

    @Password(min = 6, scheme = Password.Scheme.ALPHA_NUMERIC, messageResId = R.string.senha_falha)
    @BindView(R.id.senha)
    EditText mSenha;

    @ConfirmPassword(messageResId = R.string.senha2_falha)
    @BindView(R.id.senha2)
    EditText mSenha2;

    private Validator validator;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_email);
        setTitle(getResources().getString(R.string.criar_email));
        ButterKnife.bind(this);

        validator = new Validator(this);
        validator.setValidationListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.v("signed_in:", user.getUid());
                } else {
                    // User is signed out
                    Log.v("signed_out", "");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @OnClick({R.id.login})
    void onItemClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                validator.validate();
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {
        mAuth.createUserWithEmailAndPassword(mEmail.getText().toString(), mSenha.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateEmailActivity.this, getResources().getText(R.string.sucesso), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CreateEmailActivity.this, R.string.falha,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}