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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.justinlee.drawmatic.Drawmatic;
import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.MainContract;
import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.constants.Constants;
import com.justinlee.drawmatic.objects.OnlineSettings;
import com.justinlee.drawmatic.objects.Player;
import com.justinlee.drawmatic.online.OnlineContract;
import com.justinlee.drawmatic.online.OnlineFragment;
import com.justinlee.drawmatic.online.OnlinePresenter;
import com.justinlee.drawmatic.online_cereate_room.CreateRoomContract;
import com.justinlee.drawmatic.online_cereate_room.CreateRoomFragment;
import com.justinlee.drawmatic.online_cereate_room.CreateRoomPresenter;
import com.justinlee.drawmatic.online_room_waiting.OnlineWaitingContract;
import com.justinlee.drawmatic.online_room_waiting.OnlineWaitingFragment;
import com.justinlee.drawmatic.online_room_waiting.OnlineWaitingPresenter;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;

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

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                onlinePresenter.informToShowResultRooms(transformDocumentSnapshotToRoomsList(documentSnapshot));
            }
        });
    }

    private ArrayList<OnlineSettings> transformDocumentSnapshotToRoomsList(DocumentSnapshot documentSnapshot) {
        OnlineSettings onlineSettings = documentSnapshot.toObject(OnlineSettings.class);
        ArrayList<OnlineSettings> onlineSettingsList = new ArrayList<>();
        onlineSettingsList.add(onlineSettings);

        return onlineSettingsList;
    }

    public void joinRoom(final OnlineFragment onlineFragment, final OnlineSettings onlineSettings, final OnlinePresenter onlinePresenter, final Player joiningPlayer) {
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

                    HashMap<String, Object> player = new HashMap<>();
                    player.put("playerName", joiningPlayer.getPlayerName());
                    player.put("playerId", joiningPlayer.getPlayerId());
//                    player.put("playerOrder", newCurrentNumPlayers);
                    player.put("playerType", Constants.PlayerType.PARTICIPANT);

                    transaction.update(roomToJoinRef, "players", FieldValue.arrayUnion(player));
                }
                return null;
            }
        })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        onlinePresenter.informToTransToOnlineWaitingPage(onlineSettings);
//                ((MainContract.View) onlineFragment.getActivity()).hideLoadingUi();
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


    public void leaveRoom(final OnlineWaitingFragment onlineWaitingFragment, final OnlineSettings onlineSettings, final Player leavingPlayer) {
        if (leavingPlayer.getPlayerType() == Constants.PlayerType.ROOM_MASTER) {
            deleteRoomWhenRoomMasterLeaves(onlineWaitingFragment, onlineSettings, leavingPlayer);

        } else {
            final DocumentReference roomToLeaveRef = Drawmatic.getmFirebaseDb().collection("rooms").document(onlineSettings.getRoomName());

            Drawmatic.getmFirebaseDb().runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentSnapshot snapshot = transaction.get(roomToLeaveRef);
                    OnlineSettings mostCurrentOnlineSettings = snapshot.toObject(OnlineSettings.class);

                    // if room players maxed out, then player cannot join the game

                    double newCurrentNumPlayers = mostCurrentOnlineSettings.getCurrentNumPlayers() - 1;
                    transaction.update(roomToLeaveRef, "currentNumPlayers", newCurrentNumPlayers);

                    HashMap<String, Object> player = new HashMap<>();
                    player.put("playerName", leavingPlayer.getPlayerName());
                    player.put("playerId", leavingPlayer.getPlayerId());
//                    player.put("playerOrder", newCurrentNumPlayers);
                    player.put("playerType", leavingPlayer.getPlayerType());

                    transaction.update(roomToLeaveRef, "players", FieldValue.arrayRemove(player));

                    return null;
                }
            })
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            ((MainActivity) onlineWaitingFragment.getActivity()).getMainPresenter().transToOnlinePage();
                            ((MainContract.View) onlineWaitingFragment.getActivity()).hideLoadingUi();
                            Log.d(TAG, "Transaction success!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ((MainContract.View) onlineWaitingFragment.getActivity()).hideLoadingUi();
                            Log.w(TAG, "Transaction failure.", e);
                        }
                    });
        }
    }

    public void deleteRoomWhenRoomMasterLeaves(final OnlineWaitingFragment onlineWaitingFragment, final OnlineSettings onlineSettings, final Player leavingPlayer) {
        Drawmatic.getmFirebaseDb().collection("rooms").document(onlineSettings.getRoomName()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ((MainActivity) onlineWaitingFragment.getActivity()).getMainPresenter().transToOnlinePage();
                        ((MainActivity) onlineWaitingFragment.getActivity()).hideLoadingUi();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ((MainActivity) onlineWaitingFragment.getActivity()).hideLoadingUi();
                    }
                });
    }


    /**
     * *********************************************************************************
     * Start Game and Room Status Syncing
     * **********************************************************************************
     * @param onlineSettings
     */

    public void startOnlineGame(final OnlineWaitingFragment onlineWaitingFragment, final OnlineSettings onlineSettings) {
        WriteBatch batch = Drawmatic.getmFirebaseDb().batch();

        DocumentReference roomRef = Drawmatic.getmFirebaseDb().collection("rooms").document(onlineSettings.getRoomName());
        batch.update(roomRef, "inGame", true);


        HashMap<String, Object> playersMap = new HashMap<>();

        for(int i = 0; i < onlineSettings.getPlayers().size(); i++) {
            playersMap.put("playerOrder", i+1);
            DocumentReference drawingsRef = roomRef.collection("drawings").document(onlineSettings.getPlayers().get(i).getPlayerId());
            batch.set(drawingsRef, playersMap);
        }


        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ((MainContract.View) onlineWaitingFragment.getActivity()).hideLoadingUi();
            }
        });



























//
//        final DocumentReference roomRef = Drawmatic.getmFirebaseDb().collection("rooms").document(onlineSettings.getRoomName());
//
//        Drawmatic.getmFirebaseDb().runTransaction(new Transaction.Function<Void>() {
//            @Override
//            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
//                DocumentSnapshot snapshot = transaction.get(roomRef);
//                OnlineSettings mostCurrentOnlineSettings = snapshot.toObject(OnlineSettings.class);
//
//                // change inGame status to true
//                transaction.update(roomRef, "inGame", true);
//                HashMap<String, Object> playersMap = new HashMap<>();
//
//                for(int i = 0; i < mostCurrentOnlineSettings.getPlayers().size(); i++) {
//                    playersMap.put("playerOrder", i+1);
//                }
//
//                DocumentReference drawingsRef = roomRef.collection("drawings").document().set(playersMap);
//
//
//
//                return null;
//            }
//        })
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        ((MainActivity) onlineWaitingFragment.getActivity()).getMainPresenter().transToOnlinePage();
//                        ((MainContract.View) onlineWaitingFragment.getActivity()).hideLoadingUi();
//                        Log.d(TAG, "Transaction success!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        ((MainContract.View) onlineWaitingFragment.getActivity()).hideLoadingUi();
//                        Log.w(TAG, "Transaction failure.", e);
//                    }
//                });
//
//
//        ((MainActivity) fragment.getActivity()).getMainPresenter().transToSetTopicPage(mOnlineSettings.getGameMode(), new OnlineGame(mOnlineSettings));
    }


    public void syncRoomStatus(final OnlineWaitingContract.View onlineWaitingView, final OnlineWaitingPresenter onlineWaitingPresenter, OnlineSettings onlineSettings) {
        DocumentReference docRef = Drawmatic.getmFirebaseDb().collection("rooms").document(onlineSettings.getRoomName());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        onlineWaitingPresenter.syncOnlineNewRoomStatus(transformDocumentSnapshotToRoomsList(document));
                        ((MainContract.View) ((OnlineWaitingFragment) onlineWaitingView).getActivity()).hideLoadingUi();
                    } else {
                        Snackbar.make(((OnlineWaitingFragment) (onlineWaitingPresenter.getOnlineWaitingView())).getActivity().findViewById(R.id.fragment_container_main), "Room does not exist", Snackbar.LENGTH_SHORT).show();
                        // TODO room was deleted by room master, player needs to leave the room
                    }
                } else {
                    Snackbar.make(((OnlineWaitingFragment) (onlineWaitingPresenter.getOnlineWaitingView())).getActivity().findViewById(R.id.fragment_container_main), "Something went Wrong, please try again", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                ArrayList<OnlineSettings> onlineSettingsList = transformDocumentSnapshotToRoomsList(documentSnapshot);
                OnlineSettings onlineSettings = onlineSettingsList.get(0);

                if (onlineSettings.isInGame()) {
                    onlineWaitingPresenter.startPlayingOnline((OnlineWaitingFragment) onlineWaitingView);
                } else {
                    onlineWaitingPresenter.syncOnlineNewRoomStatus(transformDocumentSnapshotToRoomsList(documentSnapshot));
                }
            }
        });
    }
}
