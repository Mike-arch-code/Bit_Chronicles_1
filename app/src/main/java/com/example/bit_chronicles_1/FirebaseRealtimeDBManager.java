package com.example.bit_chronicles_1;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

public class FirebaseRealtimeDBManager {

    private DatabaseReference databaseReference;

    public FirebaseRealtimeDBManager() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference();
    }

    public void writeData(String path, Object data, @NonNull OnCompleteListener<Void> listener) {
        databaseReference.child(path).setValue(data)
                .addOnCompleteListener(listener);
    }

    public void updateData(String path, Map<String, Object> data, @NonNull OnCompleteListener<Void> listener) {
        databaseReference.child(path).updateChildren(data)
                .addOnCompleteListener(listener);
    }

    public void pushData(String path, Object data, @NonNull OnCompleteListener<Void> listener) {
        databaseReference.child(path).push().setValue(data)
                .addOnCompleteListener(listener);
    }

    public void readDataOnce(String path, @NonNull OnDataReadListener listener) {
        databaseReference.child(path).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    listener.onSuccess(snapshot);
                } else {
                    listener.onNotFound();
                }
            } else {
                listener.onFailure(task.getException());
            }
        });
    }

    public ValueEventListener addRealtimeListener(String path, @NonNull OnRealtimeDataListener listener) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    listener.onDataChange(snapshot);
                } else {
                    listener.onDataNotFound();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onCancelled(error);
            }
        };
        databaseReference.child(path).addValueEventListener(valueEventListener);
        return valueEventListener;
    }

    public void removeRealtimeListener(String path, @NonNull ValueEventListener listener) {
        databaseReference.child(path).removeEventListener(listener);
    }

    public void deleteData(String path, @NonNull OnCompleteListener<Void> listener) {
        databaseReference.child(path).removeValue()
                .addOnCompleteListener(listener);
    }

    public interface OnDataReadListener {
        void onSuccess(DataSnapshot dataSnapshot);
        void onNotFound();
        void onFailure(Exception e);
    }

    public interface OnRealtimeDataListener {
        void onDataChange(DataSnapshot dataSnapshot);
        void onDataNotFound();
        void onCancelled(DatabaseError error);
    }
}