package com.justinlee.drawmatic.firabase_operation;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.justinlee.drawmatic.Drawmatic;
import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.MainContract;
import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.objects.OnlineSettings;
import com.justinlee.drawmatic.objects.Player;
import com.justinlee.drawmatic.online.OnlineContract;
import com.justinlee.drawmatic.online.OnlineFragment;
import com.justinlee.drawmatic.online_cereate_room.CreateRoomContract;
import com.justinlee.drawmatic.online_cereate_room.CreateRoomFragment;
import com.justinlee.drawmatic.online_cereate_room.CreateRoomPresenter;
import com.justinlee.drawmatic.online_room_waiting.OnlineWaitingContract;
import com.justinlee.drawmatic.online_room_waiting.OnlineWaitingPresenter;

import java.util.ArrayList;
import java.util.HashMap;

public class FirestoreManager {
    private static final String TAG = "justinx";

    private Context mContext;
    private FirebaseFirestore mFirebaseDb;

    public FirestoreManager(Context context) {
        mContext = context;
        mFirebaseDb = Drawmatic.getmFirebaseDb();
    }


    /**
     * *********************************************************************************
     * RoomMaster opertaions
     * **********************************************************************************
     */
    public void createOnlineRoom(final CreateRoomPresenter createRoomPresenter, final OnlineSettings onlineSettings, final CreateRoomContract.View createRoomView) {
        Drawmatic.getmFirebaseDb().collection("rooms").document(onlineSettings.getRoomName()).set(onlineSettings)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        createRoomPresenter.transToRoomWaitingPage((CreateRoomFragment) createRoomView, onlineSettings);
                        ((MainContract.View) ((Fragment) createRoomView).getActivity()).hideLoadingUi();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: failure " + e.toString());
                        ((MainContract.View) ((Fragment) createRoomView).getActivity()).hideLoadingUi();
                    }
                });
    }


    public void deleteRoom(OnlineWaitingPresenter onlineWaitingPresenter, OnlineSettings onlineSettings, final OnlineWaitingContract.View onlineWaitingView) {
        Drawmatic.getmFirebaseDb().collection("rooms").document(onlineSettings.getRoomName()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ((MainActivity) ((Fragment) onlineWaitingView).getActivity()).getMainPresenter().transToOnlinePage();
                        ((MainContract.View) ((Fragment) onlineWaitingView).getActivity()).hideLoadingUi();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ((MainContract.View) ((Fragment) onlineWaitingView).getActivity()).hideLoadingUi();
                    }
                });
    }

    /**
     * *********************************************************************************
     * Player opertaions
     * **********************************************************************************
     */
    public void searchForRoom(final OnlineFragment onlineFragment, final OnlineContract.Presenter onlinePresenter, String inputString) {
        DocumentReference docRef = Drawmatic.getmFirebaseDb().collection("rooms").document(inputString);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        onlinePresenter.informToShowResultRooms(transformDocumentSnapshotToRoomsList(document));
                    } else {
                        Snackbar.make(onlineFragment.getActivity().findViewById(R.id.fragment_container_main), "Room does not exist", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(onlineFragment.getActivity().findViewById(R.id.fragment_container_main), "Something went Wrong, please try again", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private ArrayList<OnlineSettings> transformDocumentSnapshotToRoomsList(DocumentSnapshot documentSnapshot) {
        OnlineSettings onlineSettings = documentSnapshot.toObject(OnlineSettings.class);
        ArrayList<OnlineSettings> onlineSettingsList = new ArrayList<>();
        onlineSettingsList.add(onlineSettings);

        return onlineSettingsList;
    }


    public void joinRoom(final OnlineFragment onlineFragment, final OnlineSettings onlineSettings, final Player joiningPlayer) {
        final DocumentReference roomToJoinRef = Drawmatic.getmFirebaseDb().collection("rooms").document(onlineSettings.getRoomName());

        Drawmatic.getmFirebaseDb().runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(roomToJoinRef);
                OnlineSettings mostCurrentOnlineSettings = snapshot.toObject(OnlineSettings.class);

                // if room players maxed out, then player cannot join the game
                if (mostCurrentOnlineSettings.getCurrentNumPlayers() == mostCurrentOnlineSettings.getMaxPlayers()) {
                    Snackbar.make(onlineFragment.getActivity().findViewById(R.id.fragment_container_main), "players maxed out", Snackbar.LENGTH_SHORT).show();
                } else {
                    double newCurrentNumPlayers = mostCurrentOnlineSettings.getCurrentNumPlayers() + 1;
                    transaction.update(roomToJoinRef, "currentNumPlayers", newCurrentNumPlayers);


                    HashMap<String, Object> players = new HashMap<>();
                    players.put(String.valueOf(newCurrentNumPlayers), joiningPlayer);
//                    transaction.set(roomToJoinRef, players);
//                    transaction.update(roomToJoinRef, "players", FieldValue.arrayUnion(players));
                    transaction.set(roomToJoinRef, players);
                }

                // Success
                return null;
            }
        })
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ((MainContract.View) onlineFragment.getActivity()).hideLoadingUi();
                Log.d(TAG, "Transaction success!");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ((MainContract.View) onlineFragment.getActivity()).hideLoadingUi();
                Log.w(TAG, "Transaction failure.", e);
            }
        });
    }

    public void leaveRoom() {

    }


}
