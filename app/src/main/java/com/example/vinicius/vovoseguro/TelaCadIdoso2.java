package com.example.vinicius.vovoseguro;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TelaCadIdoso2 extends AppCompatActivity {


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAuth mAuth;

    private FirebaseUser user1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cad_idoso2);

        mAuth = FirebaseAuth.getInstance();

        user1 = mAuth.getCurrentUser();
    }

    public void onClick(View view) {
        if(view.getId() == R.id.bt_next2){
            Log.w("qwert", "Error writing document");


            EditText doenca1 = (EditText) findViewById(R.id.et_doenca1);
            String doenca1Idoso = doenca1.getText().toString();

            EditText doenca2 = (EditText) findViewById(R.id.et_doenca2);
            String doenca2Idoso = doenca2.getText().toString();

            EditText doenca3 = (EditText) findViewById(R.id.et_doenca3);
            String doenca3Idoso = doenca3.getText().toString();

            EditText doenca4 = (EditText) findViewById(R.id.et_doenca4);
            String doenca4Idoso = doenca4.getText().toString();

            EditText doenca5 = (EditText) findViewById(R.id.et_doenca4);
            String doenca5Idoso = doenca5.getText().toString();


            Map<String, Object> user = new HashMap<>();
            user.put("idosoDoenca1", doenca1Idoso);
            user.put("idosoDoenca2", doenca2Idoso);
            user.put("idosoDoenca3", doenca3Idoso);
            user.put("idosoDoenca4", doenca4Idoso);
            user.put("idosoDoenca5", doenca5Idoso);

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

            Intent it = new Intent (this, TelaPrincipal.class);
            startActivity(it);

        }
    }
}
