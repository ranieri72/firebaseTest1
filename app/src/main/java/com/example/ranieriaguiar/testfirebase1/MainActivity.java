package com.example.ranieriaguiar.testfirebase1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ranieriaguiar.testfirebase1.fragments.TestDialogFragment;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.progBar)
    ProgressBar bar;

    private Handler handler = new Handler();
    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
    }

    @OnClick({R.id.report_bug, R.id.create_email, R.id.login_email, R.id.alert_dialog_basic, R.id.alert_dialog_list, R.id.alert_dialog_custom, R.id.alert_dialog_fragment, R.id.progress_bar_thread, R.id.maps})
    void onItemClicked(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.report_bug:
                FirebaseCrash.report(new Exception("My first Android non-fatal error"));
                break;
            case R.id.create_email:
                it = new Intent(MainActivity.this, CreateEmailActivity.class);
                startActivity(it);
                break;
            case R.id.login_email:
                it = new Intent(MainActivity.this, LoginEmailActivity.class);
                startActivity(it);
                break;
            case R.id.alert_dialog_basic:
                alertDialogBasic();
                break;
            case R.id.alert_dialog_list:
                alertDialogList();
                break;
            case R.id.alert_dialog_custom:
                alertDialogCustom();
                break;
            case R.id.alert_dialog_fragment:
                alertDialogFragment();
                break;
            case R.id.progress_bar_thread:
                progressBarThread();
                break;
            case R.id.maps:
                it = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(it);
                break;
        }
    }

    private void alertDialogBasic() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Titulo");
        builder.setMessage("Qualifique este software");
        builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(MainActivity.this, "positivo=" + arg1, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Negativo", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(MainActivity.this, "negativo=" + arg1, Toast.LENGTH_SHORT).show();
            }
        });
        alerta = builder.create();
        alerta.show();
    }

    private void alertDialogList() {
        CharSequence[] charSequences = new CharSequence[]{"Filmes", "Dormir", "Sair"};
        final boolean[] checados = new boolean[charSequences.length];

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("O que você gosta?");
        builder.setMultiChoiceItems(charSequences, checados, new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
                checados[arg1] = arg2;
            }
        });

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                StringBuilder texto = new StringBuilder("Checados: ");
                for (boolean ch : checados) {
                    texto.append(ch).append("; ");
                }
                Toast.makeText(MainActivity.this, texto.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        alerta = builder.create();
        alerta.show();
    }

    private void alertDialogCustom() {
        LayoutInflater li = getLayoutInflater();

        View view = li.inflate(R.layout.alert_dialog_custom, null);
        view.findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Toast.makeText(MainActivity.this, "alerta.dismiss()", Toast.LENGTH_SHORT).show();
                alerta.dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Titulo");
        builder.setView(view);
        alerta = builder.create();
        alerta.show();
    }

    private void alertDialogFragment() {
        TestDialogFragment testDialogFragment = new TestDialogFragment();
        testDialogFragment.onCreateDialog(this).show();
    }

    private void progressBarThread() {
        new Thread(new Runnable() {

            int i = 0;
            int progressStatus = 0;

            public void run() {
                while (progressStatus < 100) {
                    progressStatus += doWork();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        public void run() {
                            bar.setProgress(progressStatus);
                            i++;
                        }
                    });
                }
            }

            private int doWork() {

                return i * 3;
            }
        }).start();
    }
}