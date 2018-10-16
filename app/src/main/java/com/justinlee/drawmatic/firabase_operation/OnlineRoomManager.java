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
import com.google.firebase.firestore.ListenerRegistration;
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
import com.justinlee.drawmatic.play.PlayContract;
import com.justinlee.drawmatic.play.PlayFragment;
import com.justinlee.drawmatic.play.PlayPresenter;
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
    private Player mCurrentPlayer;

    public OnlineRoomManager(Context context) {
        mContext = context;
        mFirebaseDb = Drawmatic.getmFirebaseDb();
        mCurrentPlayer = ((MainPresenter) (((MainActivity) mContext).getMainPresenter())).getCurrentPlayer();
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

    /**
     * *********************************************************************************
     * Player operations
     * **********************************************************************************
     */
    public void searchForRoom(final PlayFragment playFragment, final PlayContract.Presenter playPresenter, final String inputString) {
        CollectionReference collecRef = Drawmatic.getmFirebaseDb().collection("rooms");

        collecRef
//            .whereGreaterThan("roomName", inputString)
//            .whereEqualTo("roomName", inputString)
//            .whereGreaterThan("timeStamp", getDateTimeWithinThreeHours())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot qurySnapshot = task.getResult();
                        if (!qurySnapshot.isEmpty()) {
                            playPresenter.informToShowResultRooms(transformQuerySnapshotToRoomsList(inputString, qurySnapshot));
                        }
//                        else {
//                            playPresenter.informToShowNoRoomsResultFoundMessage();
//                        }
                    } else {
                        Snackbar.make(playFragment.getActivity().findViewById(R.id.fragment_container_main), "Something went Wrong, please try again", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });

        collecRef
//            .whereGreaterThan("roomName", inputString)
//            .whereEqualTo("roomName", inputString)
//            .whereGreaterThan("timeStamp", getDateTimeWithinThreeHours())
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    playPresenter.informToShowResultRooms(transformQuerySnapshotToRoomsList(inputString, queryDocumentSnapshots));
                }
            });
    }


    public void joinRoom(final PlayFragment playFragment, final OnlineGame onlineGame, final PlayPresenter playPresenter, final Player joiningPlayer) {
        final DocumentReference roomToJoinRef = Drawmatic.getmFirebaseDb().collection("rooms").document(onlineGame.getRoomId());

        Drawmatic.getmFirebaseDb()
                .runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(roomToJoinRef);
                        OnlineSettings mostCurrentOnlineSettings = snapshot.toObject(OnlineSettings.class);

                        // if room players maxed out, then player cannot join the game
                        if (mostCurrentOnlineSettings.getPlayers().size() >= mostCurrentOnlineSettings.getMaxPlayers()) {
                            Snackbar.make(playFragment.getActivity().findViewById(R.id.fragment_container_main), "players maxed out", Snackbar.LENGTH_SHORT).show();
                        } else {
//                            double newCurrentNumPlayers = mostCurrentOnlineSettings.getCurrentNumPlayers() + 1;
//                            transaction.update(roomToJoinRef, "currentNumPlayers", newCurrentNumPlayers);

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
                        playPresenter.informToTransToOnlineWaitingPage(onlineGame);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ((MainContract.View) playFragment.getActivity()).hideLoadingUi();
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
                        ((MainActivity) mContext).getMainPresenter().transToPlayPage();
                        ((MainActivity) mContext).hideLoadingUi();
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
        final DocumentReference roomToLeaveRef =
                Drawmatic.getmFirebaseDb()
                .collection("rooms")
                .document(roomId);

        Drawmatic.getmFirebaseDb()
                .runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(roomToLeaveRef);
                        OnlineSettings mostCurrentOnlineSettings = snapshot.toObject(OnlineSettings.class);

                        // if room players maxed out, then player cannot join the game

//                        double newCurrentNumPlayers = mostCurrentOnlineSettings.getCurrentNumPlayers() - 1;
//                        transaction.update(roomToLeaveRef, "currentNumPlayers", newCurrentNumPlayers);

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
                        ((MainActivity) onlineWaitingFragment.getActivity()).getMainPresenter().transToPlayPage();
                        ((MainContract.View) onlineWaitingFragment.getActivity()).hideLoadingUi();
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
     * *********************************************************************************
     */
    public ListenerRegistration syncRoomStatus(final OnlineWaitingContract.View onlineWaitingView, final OnlineWaitingPresenter onlineWaitingPresenter, OnlineGame onlineGame) {
            DocumentReference docRef = Drawmatic.getmFirebaseDb()
                    .collection("rooms")
                    .document(onlineGame.getRoomId());

            docRef
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    onlineWaitingPresenter.updateOnlineRoomStatus(transformDocumentSnapshotToOnlineGameObject(document));

                                } else {
                                    Snackbar.make(((OnlineWaitingFragment) (onlineWaitingPresenter.getOnlineWaitingView())).getActivity().findViewById(R.id.fragment_container_main), "Room does not exist", Snackbar.LENGTH_SHORT).show();
                                    // TODO room was deleted by room master, player needs to leave the room
                                }

                            } else {
                                Snackbar.make(((OnlineWaitingFragment) (onlineWaitingPresenter.getOnlineWaitingView())).getActivity().findViewById(R.id.fragment_container_main), "Something went Wrong, please try again", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });

            ListenerRegistration listenerRegistration = docRef
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w(TAG, "Listen failed.", e);
                                return;
                            }


                            OnlineGame onlineGame = null;
                            try{
                                onlineGame = transformDocumentSnapshotToOnlineGameObject(documentSnapshot).get(0);
                            } catch (NullPointerException exception) {
                                onlineWaitingPresenter.informToTransToOnlinePage();
                                onlineWaitingPresenter.informToShowRoomClosedMessage();
                            }


                            if(onlineGame != null) {
                                if (onlineGame.getOnlineSettings().isInGame()) {
                                    onlineWaitingPresenter.startPlayingOnline();
                                } else {
                                    onlineWaitingPresenter.updateOnlineRoomStatus(transformDocumentSnapshotToOnlineGameObject(documentSnapshot));
                                }
                            }
                        }
                    });

        return listenerRegistration;
    }

    public ListenerRegistration syncRoomStatusWhileInGame(final MainContract.View mainView, final MainContract.Presenter mainPresenter, final OnlineGame onlineGame) {
        DocumentReference docRef = Drawmatic.getmFirebaseDb()
                .collection("rooms")
                .document(onlineGame.getRoomId());

        ListenerRegistration listenerRegistration = docRef
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        try{
                            transformDocumentSnapshotToOnlineGameObject(documentSnapshot).get(0);
                        } catch (NullPointerException exception) {
                            new OnlineInGameManager((MainActivity) mainView).leaveRoomAndDeleteDataWhileInGame(onlineGame);
                        }
                    }
                });

        return listenerRegistration;
    }

    public void setGameStatusToInGame(final OnlineWaitingPresenter onlineWaitingPresenter, final OnlineGame onlineGame) {
        if (mCurrentPlayer.getPlayerType() == Constants.PlayerType.ROOM_MASTER) {
            WriteBatch batch = Drawmatic.getmFirebaseDb().batch();
            DocumentReference roomRef = Drawmatic.getmFirebaseDb()
                    .collection("rooms")
                    .document(onlineGame.getRoomId());
            batch.update(roomRef, "inGame", true);
            batch.commit();
        }
    }

    /**
     * *********************************************************************************
     * Helper Methods
     * *********************************************************************************
     */
    private ArrayList<OnlineGame> transformDocumentSnapshotToOnlineGameObject(DocumentSnapshot documentSnapshot) throws NullPointerException {
        OnlineSettings onlineSettings = documentSnapshot.toObject(OnlineSettings.class);
        OnlineGame onlineGame = new OnlineGame(documentSnapshot.getId(), onlineSettings);

        ArrayList<OnlineGame> onlineGamesList = new ArrayList<>();
        onlineGamesList.add(onlineGame);

        return onlineGamesList;
    }

    private ArrayList<OnlineGame> transformQuerySnapshotToRoomsList(String inputQueryString, QuerySnapshot querySnapshot) {
        List<DocumentSnapshot> documentSnapshots = querySnapshot.getDocuments();
        ArrayList<OnlineGame> onlineGamesList = new ArrayList<>();
        for(DocumentSnapshot documentSnapshot : documentSnapshots) {
            OnlineSettings onlineSettings = documentSnapshot.toObject(OnlineSettings.class);
            if(onlineSettings != null) {
                OnlineGame onlineGame = new OnlineGame(documentSnapshot.getId(), documentSnapshot.toObject(OnlineSettings.class));
                if(onlineGame.getOnlineSettings().getRoomName().contains(inputQueryString)) {
                    onlineGamesList.add(onlineGame);
                }
            }
        }

        return onlineGamesList;
    }

    private long getDateTimeWithinThreeHours() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        long threeHours = 3* 60 * 1000;

        return currentTime - threeHours;
    }
}
