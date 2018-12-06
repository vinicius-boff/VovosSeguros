package com.example.vinicius.vovoseguro;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

//tela usada para responsavel cadastrar usuario da pulseira
public class TelaCadIdoso extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    private FirebaseAuth mAuth;

    private FirebaseUser user1 ;

    private Spinner spinnerSexo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cad_idoso);
        mAuth = FirebaseAuth.getInstance();

        user1 = mAuth.getCurrentUser();

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("responsibleName", user1.getDisplayName());
        user.put("responsibleEmail", user1.getEmail());
//        user.put("responsibleFoto", user1.getPhotoUrl());

// Add a new document with a generated ID
        db.collection("users")
                .document(user1.getUid())
                .set(user) .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("", "DocumentSnapshot successfully written!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "Error writing document", e);
                    }
                });

        spinnerSexo = (Spinner) findViewById(R.id.spinner_sexo);

        String[] lsSexo = getResources().getStringArray(R.array.lista_sexo);

        spinnerSexo.setAdapter(new ArrayAdapter<String>(this, R.layout.textview, lsSexo));

    }





    public void onClick(View view) {
        if(view.getId() == R.id.bt_next1){

            EditText textNome = (EditText) findViewById(R.id.et_nomeidoso);
            String nomeIdoso = textNome.getText().toString();

            EditText textNasc = (EditText) findViewById(R.id.et_dateidoso);
            String nacsIdoso = textNasc.getText().toString();

            EditText textPeso = (EditText) findViewById(R.id.et_pesoidoso);
            String pesoIdoso = textPeso.getText().toString();

            EditText textAltura = (EditText) findViewById(R.id.et_alturaidoso);
            String alturaIdoso = textAltura.getText().toString();

            String sexo = spinnerSexo.getSelectedItem().toString();



            Map<String, Object> user = new HashMap<>();
            user.put("idosoName", nomeIdoso);
            user.put("idosoNasc", nacsIdoso);
            user.put("idosoPeso", pesoIdoso);
            user.put("idosoAltura", alturaIdoso);
            user.put("sexoIdoso", sexo);


            // Add a new document with a generated ID
            db.collection("users")
                    .document(user1.getUid())
                    .update(user) .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("", "DocumentSnapshot successfully written!");
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("", "Error writing document", e);
                        }
                    });

            Intent it = new Intent (this, TelaCadIdoso2.class);
            startActivity(it);

        }
    }
}
