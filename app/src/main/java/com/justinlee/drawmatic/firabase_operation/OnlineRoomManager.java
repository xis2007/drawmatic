package com.justinlee.drawmatic.firabase_operation;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.justinlee.drawmatic.Drawmatic;
import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.MainContract;
import com.justinlee.drawmatic.MainPresenter;
import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.constants.Constants;
import com.justinlee.drawmatic.objects.OnlineGame;
import com.justinlee.drawmatic.objects.OnlineSettings;
import com.justinlee.drawmatic.objects.Player;
import com.justinlee.drawmatic.online.OnlineContract;
import com.justinlee.drawmatic.online.OnlineFragment;
import com.justinlee.drawmatic.online.OnlinePresenter;
import com.justinlee.drawmatic.online_cereate_room.CreateRoomContract;
import com.justinlee.drawmatic.online_cereate_room.CreateRoomPresenter;
import com.justinlee.drawmatic.online_room_waiting.OnlineWaitingContract;
import com.justinlee.drawmatic.online_room_waiting.OnlineWaitingFragment;
import com.justinlee.drawmatic.online_room_waiting.OnlineWaitingPresenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class OnlineRoomManager {
    private static final String TAG = "onlineRoomManagerrrrrr";

    private Context mContext;
    private FirebaseFirestore mFirebaseDb;

    public OnlineRoomManager(Context context) {
        mContext = context;
        mFirebaseDb = Drawmatic.getmFirebaseDb();
    }


    /**
     * *********************************************************************************
     * RoomMaster opertaions
     * **********************************************************************************
     */
    public void createOnlineRoom(final CreateRoomPresenter createRoomPresenter, final OnlineSettings onlineSettings, final CreateRoomContract.View createRoomView) {
        final DocumentReference newRoomReference = Drawmatic.getmFirebaseDb()
                .collection("rooms")
                .document();

        newRoomReference
                .set(onlineSettings)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        createRoomPresenter.transToRoomWaitingPage(newRoomReference.getId(), onlineSettings);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ((MainContract.View) ((Fragment) createRoomView).getActivity()).hideLoadingUi();
                    }
                });
    }


//    public void deleteRoom(final OnlineWaitingPresenter onlineWaitingPresenter, String roomId, OnlineSettings onlineSettings, final OnlineWaitingContract.View onlineWaitingView) {
//        Drawmatic.getmFirebaseDb().collection("rooms").document(roomId).delete()
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        onlineWaitingPresenter.informToTransToOnlinePage();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        ((MainContract.View) ((Fragment) onlineWaitingView).getActivity()).hideLoadingUi();
//                    }
//                });
//    }

    /**
     * *********************************************************************************
     * Player opertaions
     * **********************************************************************************
     */
    public void searchForRoom(final OnlineFragment onlineFragment, final OnlineContract.Presenter onlinePresenter, String inputString) {
        CollectionReference collecRef = Drawmatic.getmFirebaseDb().collection("rooms");

        collecRef
            .whereEqualTo("roomName", inputString)
//            .whereGreaterThan("timeStamp", getDateTimeWithinThreeHours())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot qurySnapshot = task.getResult();
                        if (!qurySnapshot.isEmpty()) {
                            onlinePresenter.informToShowResultRooms(transformQuerySnapshotToRoomsList(qurySnapshot));
                        } else {
                            Snackbar.make(onlineFragment.getActivity().findViewById(R.id.fragment_container_main), "Room does not exist", Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(onlineFragment.getActivity().findViewById(R.id.fragment_container_main), "Something went Wrong, please try again", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });

        collecRef
            .whereEqualTo("roomName", inputString)
//            .whereGreaterThan("timeStamp", getDateTimeWithinThreeHours())
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    onlinePresenter.informToShowResultRooms(transformQuerySnapshotToRoomsList(queryDocumentSnapshots));
                }
            });
    }


    public void joinRoom(final OnlineFragment onlineFragment, final OnlineGame onlineGame, final OnlinePresenter onlinePresenter, final Player joiningPlayer) {
        final DocumentReference roomToJoinRef = Drawmatic.getmFirebaseDb().collection("rooms").document(onlineGame.getRoomId());

        Drawmatic.getmFirebaseDb()
                .runTransaction(new Transaction.Function<Void>() {
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
                            player.put("playerType", Constants.PlayerType.PARTICIPANT);

                            transaction.update(roomToJoinRef, "players", FieldValue.arrayUnion(player));
                        }
                        return null;
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        onlinePresenter.informToTransToOnlineWaitingPage(onlineGame);
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


    public void leaveRoom(final OnlineWaitingFragment onlineWaitingFragment, OnlineGame onlineGame, final Player leavingPlayer) {
        if (leavingPlayer.getPlayerType() == Constants.PlayerType.ROOM_MASTER) {
            deleteRoomWhenRoomMasterLeaves(onlineWaitingFragment, onlineGame.getRoomId(), onlineGame.getOnlineSettings(), leavingPlayer);

        } else {
            playerLeavesGame(onlineWaitingFragment, onlineGame.getRoomId(), onlineGame.getOnlineSettings(), leavingPlayer);
        }
    }

    private void deleteRoomWhenRoomMasterLeaves(final OnlineWaitingFragment onlineWaitingFragment, String roomId, final OnlineSettings onlineSettings, final Player leavingPlayer) {
        Drawmatic.getmFirebaseDb().collection("rooms").document(roomId).delete()
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

    private void playerLeavesGame(final OnlineWaitingFragment onlineWaitingFragment, String roomId, OnlineSettings onlineSettings, final Player leavingPlayer) {
        final DocumentReference roomToLeaveRef = Drawmatic.getmFirebaseDb()
                                                            .collection("rooms")
                                                            .document(roomId);

        Drawmatic.getmFirebaseDb()
                .runTransaction(new Transaction.Function<Void>() {
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


    /**
     * *********************************************************************************
     * Start Game and Room Status Syncing
     * **********************************************************************************
     */
    public void syncRoomStatus(final OnlineWaitingContract.View onlineWaitingView, final OnlineWaitingPresenter onlineWaitingPresenter, OnlineGame onlineGame) {
        DocumentReference docRef = Drawmatic.getmFirebaseDb()
                .collection("rooms")
                .document(onlineGame.getRoomId());

        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                onlineWaitingPresenter.updateOnlineRoomStatus(transformDocumentSnapshotToRoomsList(document));

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

                OnlineGame onlineGame = transformDocumentSnapshotToRoomsList(documentSnapshot).get(0);
                if(onlineGame.getOnlineSettings() != null) {
                    if (onlineGame.getOnlineSettings().isInGame()) {
                        onlineWaitingPresenter.startPlayingOnline();
                    } else {
                        onlineWaitingPresenter.updateOnlineRoomStatus(transformDocumentSnapshotToRoomsList(documentSnapshot));
                    }
                }
            }
        });
    }

    public void setGameStatusToInGame(final OnlineWaitingPresenter onlineWaitingPresenter, final OnlineGame onlineGame) {
        Player currentPlayer = ((MainPresenter) (((MainActivity) mContext).getMainPresenter())).getCurrentPlayer();

        if (currentPlayer.getPlayerType() == Constants.PlayerType.ROOM_MASTER) {
            WriteBatch batch = Drawmatic.getmFirebaseDb().batch();
            DocumentReference roomRef = Drawmatic.getmFirebaseDb()
                    .collection("rooms")
                    .document(onlineGame.getRoomId());
            batch.update(roomRef, "inGame", true);
            batch.commit();
        }
    }


//    public void startOnlineGame(final OnlineWaitingFragment onlineWaitingFragment, final OnlineSettings onlineSettings) {
//
//
//
////        HashMap<String, Object> playersMap = new HashMap<>();
////        ArrayList<Player> sortedPlayers = onlineSettings.generateSortedPlayersListById();
////        playersMap.put("playerOrder", sortedPlayers.indexOf(currentPlayer) + 1);
////        DocumentReference drawingsRef = roomRef.collection("drawings").document(currentPlayer.getPlayerId());
////        batch.set(drawingsRef, playersMap);
//
//
////                .addOnCompleteListener(new OnCompleteListener<Void>() {
////            @Override
////            public void onComplete(@NonNull Task<Void> task) {
//
////            }
////        });
//    }



    /**
     * *********************************************************************************
     * Helper Methods
     * *********************************************************************************
     */
    private ArrayList<OnlineGame> transformDocumentSnapshotToRoomsList(DocumentSnapshot documentSnapshot) {
        OnlineSettings onlineSettings = documentSnapshot.toObject(OnlineSettings.class);
        OnlineGame onlineGame = new OnlineGame(documentSnapshot.getId(), onlineSettings);

        ArrayList<OnlineGame> onlineGamesList = new ArrayList<>();
        onlineGamesList.add(onlineGame);

        return onlineGamesList;
    }

    private ArrayList<OnlineGame> transformQuerySnapshotToRoomsList(QuerySnapshot querySnapshot) {
        List<DocumentSnapshot> documentSnapshots = querySnapshot.getDocuments();
        ArrayList<OnlineGame> onlineGamesList = new ArrayList<>();
        for(DocumentSnapshot documentSnapshot : documentSnapshots) {
            OnlineGame onlineGame = new OnlineGame(documentSnapshot.getId(), documentSnapshot.toObject(OnlineSettings.class));
            onlineGamesList.add(onlineGame);
        }

        return onlineGamesList;
    }

    private long getDateTimeWithinThreeHours() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        long threeHours = 3* 60 * 1000;

        return currentTime - threeHours;
    }
}
