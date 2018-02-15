package easv.fb1;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "FB1";

    EditText m_etName, m_etPhone;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_etName = findViewById(R.id.etName);
        m_etPhone = findViewById(R.id.etPhone);
    }

    ListenerRegistration registration;
    @Override
    protected void onStart()
    {
        super.onStart();
        registration = db.collection("friends").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                       @Override
                                                       public void onEvent(QuerySnapshot snapshot, FirebaseFirestoreException e) {

                                                           m_friends.clear();
                                                           for (DocumentSnapshot document : snapshot.getDocuments()) {
                                                               Log.d(TAG, document.getId() + " => " + document.getData());
                                                               m_friends.add(new BEFriend(document.getData()));
                                                           }
                                                           updateFriend();

                                                       }
                                                   }
        );

    }

    @Override
    protected void onStop()
    {
        super.onStop();
        registration.remove();
    }

    public void onClickSave(View v)
    {
        String name = m_etName.getText().toString();
        String phone = m_etPhone.getText().toString();
        BEFriend f = new BEFriend(name, phone);

        // Add a new document with a generated ID
        db.collection("friends")
                .add(f.toMap())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });


    }

    ArrayList<BEFriend> m_friends = new ArrayList<>();

    public void onClickFetch(View v)
    {
        m_friends.clear();
        db.collection("friends")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                m_friends.add(new BEFriend(document.getData()));
                            }
                            updateFriend();
                        } else {

                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    void updateFriend()
    {
        TextView tv = findViewById(R.id.tvFriends);
        String all = "" + m_friends.size() + ":\n";
        for (BEFriend bf : m_friends)
        {
            all += "" + bf.getName() + " (" + bf.getPhone() + ")\n";
        }
        tv.setText(all);
    }
}
