package com.justinlee.drawmatic.firabase_operation;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.justinlee.drawmatic.Drawmatic;
import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.MainContract;
import com.justinlee.drawmatic.MainPresenter;
import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.constants.Constants;
import com.justinlee.drawmatic.in_game_drawing.DrawingContract;
import com.justinlee.drawmatic.in_game_drawing.DrawingFragment;
import com.justinlee.drawmatic.in_game_drawing.DrawingPresenter;
import com.justinlee.drawmatic.in_game_guessing.GuessingContract;
import com.justinlee.drawmatic.in_game_guessing.GuessingFragment;
import com.justinlee.drawmatic.in_game_guessing.GuessingPresenter;
import com.justinlee.drawmatic.in_game_result.GameResultContract;
import com.justinlee.drawmatic.in_game_result.GameResultFragment;
import com.justinlee.drawmatic.in_game_result.GameResultPresenter;
import com.justinlee.drawmatic.in_game_set_topic.SetTopicPresenter;
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
import com.justinlee.drawmatic.util.DrawViewToImageGenerator;
import com.justinlee.drawmatic.util.TopicDrawingRetrievingUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * This class operates firestore data with room keys as room name, which is not so convenient
 * It is replaced by OnlineGameManager and OnlineInGameManager, which creates rooms with auto generated keys
 * This class, therefore, is deprecated
 */

public class FirestoreManager {
    private static final String TAG = "justinxxxxx";

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
    public void checkForRoomExistance(final CreateRoomPresenter createRoomPresenter, final OnlineSettings onlineSettings, final CreateRoomContract.View createRoomView) {
        Drawmatic.getmFirebaseDb().collection("rooms").document(onlineSettings.getRoomName()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map map = documentSnapshot.getData();
                        if (map == null) {
//                            createRoomPresenter.createRoom(onlineSettings);
                        } else {
                            createRoomPresenter.informRoomExists(onlineSettings);
                        }
                    }
                });
    }

    public void createOnlineRoom(final CreateRoomPresenter createRoomPresenter, final OnlineSettings onlineSettings, final CreateRoomContract.View createRoomView) {
        Drawmatic.getmFirebaseDb().collection("rooms").document(onlineSettings.getRoomName()).set(onlineSettings)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //TODO
//                        createRoomPresenter.transToRoomWaitingPage(onlineSettings);
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
//                        onlinePresenter.informToShowResultRooms(transformDocumentSnapshotToRoomsList(document));
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
//                onlinePresenter.informToShowResultRooms(transformDocumentSnapshotToRoomsList(documentSnapshot));
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
//                        onlinePresenter.informToTransToOnlineWaitingPage(onlineSettings);
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
            playerLeavesGame(onlineWaitingFragment, onlineSettings, leavingPlayer);
        }
    }

    private void deleteRoomWhenRoomMasterLeaves(final OnlineWaitingFragment onlineWaitingFragment, final OnlineSettings onlineSettings, final Player leavingPlayer) {
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

    private void playerLeavesGame(final OnlineWaitingFragment onlineWaitingFragment, OnlineSettings onlineSettings, final Player leavingPlayer) {
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


    /**
     * *********************************************************************************
     * Start Game and Room Status Syncing
     * **********************************************************************************
     */

    public void syncRoomStatus(final OnlineWaitingContract.View onlineWaitingView, final OnlineWaitingPresenter onlineWaitingPresenter, OnlineSettings onlineSettings) {
        DocumentReference docRef = Drawmatic.getmFirebaseDb().collection("rooms").document(onlineSettings.getRoomName());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
//                        onlineWaitingPresenter.updateOnlineRoomStatus(transformDocumentSnapshotToRoomsList(document));
                        ((MainContract.View) mContext).hideLoadingUi();
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

                int currentPlayerType = ((MainPresenter) ((MainActivity) mContext).getMainPresenter()).getCurrentPlayer().getPlayerType();

                if (onlineSettings.isInGame()) {
//                    onlineWaitingPresenter.startPlayingOnline((OnlineWaitingFragment) onlineWaitingView);
                } else {
//                    onlineWaitingPresenter.updateOnlineRoomStatus(transformDocumentSnapshotToRoomsList(documentSnapshot));
                }
            }
        });
    }

    public void setGameStatusToInGame(final OnlineWaitingFragment onlineWaitingFragment, final OnlineSettings onlineSettings) {
        WriteBatch batch = Drawmatic.getmFirebaseDb().batch();
        Player currentPlayer = ((MainPresenter) (((MainActivity) (onlineWaitingFragment.getActivity())).getMainPresenter())).getCurrentPlayer();
        DocumentReference roomRef = Drawmatic.getmFirebaseDb().collection("rooms").document(onlineSettings.getRoomName());

        if (currentPlayer.getPlayerType() == Constants.PlayerType.ROOM_MASTER) {
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
     * In game monitoring and progress updates (Set Topic Fragment)
     * **********************************************************************************
     */
    public void monitorSetTopicProgress(final MainContract.View mainView, final SetTopicPresenter setTopicPresenter, final OnlineGame onlineGame) {
        final DocumentReference docRef = Drawmatic.getmFirebaseDb().collection("rooms").document(onlineGame.getOnlineSettings().getRoomName());

        // at beginning of the game, room master set all players current step progress to 0
        if (((MainPresenter) ((MainActivity) mainView).getMainPresenter()).getCurrentPlayer().getPlayerType() == Constants.PlayerType.ROOM_MASTER) {
            WriteBatch batch = Drawmatic.getmFirebaseDb().batch();
            HashMap<String, Object> progressMap = new HashMap<>();
            for (Player player : onlineGame.getOnlineSettings().getPlayers()) {
                DocumentReference progressRef = docRef.collection("progressOfEachStep").document(player.getPlayerId());
                progressMap.put("finishedCurrentStep", 0);
                batch.set(progressRef, progressMap);
            }
            batch.commit();
        }

        docRef.collection("progressOfEachStep").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                // count how many people finished this step, 1 means finished, 0 means not yet
                int totalProgressOfThisStep = 0;
                int targetProgress = onlineGame.getCurrentStep() * onlineGame.getOnlineSettings().getPlayers().size();

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Map playerProgressMap = documentSnapshot.getData();
                    long playerProgressOfThisStep = (long) playerProgressMap.get("finishedCurrentStep");
                    playerProgressOfThisStep = (int) playerProgressOfThisStep;

                    if (playerProgressOfThisStep == 1) {
                        totalProgressOfThisStep++;
                    }
                }

                // if the totalProgressOfThisStep == targetProgress, then it means every one finishes, so move the next step
                if (totalProgressOfThisStep == targetProgress) {
                    onlineGame.increamentCurrentStep();
                    setTopicPresenter.transToDrawingPageOnline();


                }
            }
        });


    }

    public void updateSetTopicStepProgressAndUploadTopic(final OnlineGame onlineGame, String inputTopic) {
        DocumentReference currentUserDrawingsRef = Drawmatic.getmFirebaseDb().collection("rooms").document(onlineGame.getOnlineSettings().getRoomName()).collection("drawings").document(((MainPresenter) ((MainActivity) mContext).getMainPresenter()).getCurrentPlayer().getPlayerId());

        Map map = new HashMap();
        map.put(String.valueOf(onlineGame.getCurrentStep()), inputTopic);

        currentUserDrawingsRef.set(map).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                DocumentReference currentUserProgressRef = Drawmatic.getmFirebaseDb().collection("rooms").document(onlineGame.getOnlineSettings().getRoomName()).collection("progressOfEachStep").document(((MainPresenter) ((MainActivity) mContext).getMainPresenter()).getCurrentPlayer().getPlayerId());
                Map progressMap = new HashMap();
                progressMap.put("finishedCurrentStep", onlineGame.getCurrentStep());
                currentUserProgressRef.update(progressMap);
            }
        });
    }


    /**
     * *********************************************************************************
     * In game monitoring and progress updates (Drawing Fragment)
     * **********************************************************************************
     */
    public void retrieveTopic(final DrawingContract.View drawingView, final DrawingPresenter drawingPresenter, final OnlineGame onlineGame) {
        TopicDrawingRetrievingUtil topicDrawingRetrievingUtil = new TopicDrawingRetrievingUtil(((DrawingFragment) drawingView).getActivity(), onlineGame, ((MainPresenter) ((MainActivity) mContext).getMainPresenter()).getCurrentPlayer());
        String playerIdToGetTopicOrDrawing = topicDrawingRetrievingUtil.calcPlayerIdToRetrieveTopicOrDrawing();
        final String dataNumber = String.valueOf(topicDrawingRetrievingUtil.calcItemNumberToRetrieveTopicOrDrawing());

        DocumentReference docRef = Drawmatic.getmFirebaseDb().collection("rooms").document(onlineGame.getOnlineSettings().getRoomName()).collection("drawings").document(playerIdToGetTopicOrDrawing);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map documentMap = document.getData();
                    if (document.exists()) {
                        Log.d(TAG, "onComplete: topic retrieved");
                        // setup the retrieved info to the page, and allow players to begin monitoring the progress of this step
                        // room master needs to reset all players' progress to 0
                        drawingPresenter.setTopic((String) documentMap.get(dataNumber));
                        drawingPresenter.setCurrentStep();
                        drawingPresenter.startMonitoringPlayerProgress();
                    } else {
                        Snackbar.make(((DrawingFragment) drawingView).getActivity().findViewById(R.id.fragment_container_main), "Room does not exist", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(((DrawingFragment) drawingView).getActivity().findViewById(R.id.fragment_container_main), "Something went Wrong, please try again", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    public ListenerRegistration monitorDrawingProgress(final DrawingContract.View drawingView, final DrawingPresenter drawingPresenter, final OnlineGame onlineGame) {
        Log.d(TAG, "monitorDrawingProgress: monitoring drawing progress");
        final DocumentReference docRef = Drawmatic.getmFirebaseDb().collection("rooms").document(onlineGame.getOnlineSettings().getRoomName());

        docRef.collection("progressOfEachStep").document(((MainPresenter) ((MainActivity) mContext).getMainPresenter()).getCurrentPlayer().getPlayerId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map finishedCurrentStepMap = document.getData();
                        long retrievedValue = (long) finishedCurrentStepMap.get("finishedCurrentStep");
                        int finishedCurrentStep = (int) retrievedValue;
                        Log.d(TAG, "onComplete: current step is " + onlineGame.getCurrentStep());
                        Log.d(TAG, "onComplete: retrieved step is " + finishedCurrentStep);

                        if (finishedCurrentStep == (onlineGame.getCurrentStep() - 1)) {
                            drawingPresenter.startDrawing();
                        }
                    } else {
                        Snackbar.make(((MainActivity) mContext).findViewById(R.id.fragment_container_main), "Room does not exist", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(((MainActivity) mContext).findViewById(R.id.fragment_container_main), "Something went Wrong, please try again", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        final ListenerRegistration listenerRegistration = docRef.collection("progressOfEachStep").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                // count how many people finished this step, 1 means finished, 0 means not yet
                int totalProgressOfThisStep = 0;
                int targetProgress = onlineGame.getCurrentStep() * onlineGame.getOnlineSettings().getPlayers().size();

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Map playerProgressMap = documentSnapshot.getData();
                    long playerProgressOfThisStep = (long) playerProgressMap.get("finishedCurrentStep");
                    playerProgressOfThisStep = (int) playerProgressOfThisStep;

                    if (playerProgressOfThisStep == onlineGame.getCurrentStep()) {
                        totalProgressOfThisStep += onlineGame.getCurrentStep();
                    }
                }
//
//                Log.d(TAG, "onEvent: total progress of this step " + totalProgressOfThisStep);
//                Log.d(TAG, "onEvent: target progress of this step " + targetProgress);

                // if the totalProgressOfThisStep == targetProgress, then it means every one finishes, so move the next step
                // else, it means this step has just begun, so start drawing
                if (targetProgress == totalProgressOfThisStep) {
                    onlineGame.increamentCurrentStep();
                    drawingPresenter.transToGuessingPage();
                    drawingPresenter.unregisterListener();

                }
                //TODO add condition for when -1
            }
        });

        return listenerRegistration;
    }

    public void uploadImageAndGetImageUrl(final DrawingContract.Presenter drawingPresenter, final OnlineGame onlineGame, View drawView) {
        // store the image into Storage first
        final StorageReference playerInRoomRef = Drawmatic.getStorageReference().child(onlineGame.getOnlineSettings().getRoomName()).child(((MainPresenter) ((MainActivity) mContext).getMainPresenter()).getCurrentPlayer().getPlayerId()).child(String.valueOf(onlineGame.getCurrentStep()) + ".jpg");

        byte[] data = new DrawViewToImageGenerator().generateToData(drawView);

        final UploadTask uploadTask = playerInRoomRef.putBytes(data);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return playerInRoomRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()) {
                    Uri downloadUrl = task.getResult();
                    drawingPresenter.updateDrawingStepProgressAndUploadImageUrl(downloadUrl.toString());
                }
                // TODO hand error
            }
        });
    }

    public void updateDrawingStepProgressAndUploadImageUrl(DrawingContract.Presenter drawingPresenter, final OnlineGame onlineGame, String downloadUrl) {
        DocumentReference currentUserDrawingsRef = Drawmatic.getmFirebaseDb().collection("rooms").document(onlineGame.getOnlineSettings().getRoomName()).collection("drawings").document(new TopicDrawingRetrievingUtil(mContext, onlineGame, ((MainPresenter) ((MainActivity) mContext).getMainPresenter()).getCurrentPlayer()).calcPlayerIdToRetrieveTopicOrDrawing());
        Map map = new HashMap();
        map.put(String.valueOf(onlineGame.getCurrentStep()), downloadUrl);
        currentUserDrawingsRef.update(map).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                DocumentReference currentUserProgressRef = Drawmatic.getmFirebaseDb().collection("rooms").document(onlineGame.getOnlineSettings().getRoomName()).collection("progressOfEachStep").document(((MainPresenter) ((MainActivity) mContext).getMainPresenter()).getCurrentPlayer().getPlayerId());
                Map progressMap = new HashMap();
                progressMap.put("finishedCurrentStep", onlineGame.getCurrentStep());
                currentUserProgressRef.update(progressMap);
                Log.d(TAG, "onComplete: updating drawing completed");
            }
        });
    }

    /**
     * *********************************************************************************
     * In game monitoring and progress updates (Guessing Fragment)
     * **********************************************************************************
     */
    public void retrieveDrawingAndWordCount(final GuessingContract.View guessingView, final GuessingPresenter guessingPresenter, final OnlineGame onlineGame) {
        TopicDrawingRetrievingUtil topicDrawingRetrievingUtil = new TopicDrawingRetrievingUtil(mContext, onlineGame, ((MainPresenter) ((MainActivity) mContext).getMainPresenter()).getCurrentPlayer());
        String playerIdToGetTopicOrDrawing = topicDrawingRetrievingUtil.calcPlayerIdToRetrieveTopicOrDrawing();

        final String imageUrlDataNumber = String.valueOf(topicDrawingRetrievingUtil.calcItemNumberToRetrieveTopicOrDrawing());
        final String topicDataNumber = String.valueOf(Integer.valueOf(imageUrlDataNumber) - 1);
        Log.d(TAG, "retrieveDrawingAndWordCount: player id to get: " + playerIdToGetTopicOrDrawing);
        Log.d(TAG, "retrieveDrawingAndWordCount: data number: " + imageUrlDataNumber);

        final DocumentReference docRef = Drawmatic.getmFirebaseDb().collection("rooms").document(onlineGame.getOnlineSettings().getRoomName()).collection("drawings").document(playerIdToGetTopicOrDrawing);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map documentMap = document.getData();

                    if (document.exists()) {
                        guessingPresenter.setWordCountHint(((String) documentMap.get(topicDataNumber)));
                        guessingPresenter.setDrawing((String) documentMap.get(imageUrlDataNumber));
                        guessingPresenter.setCurrentStep();
                        guessingPresenter.startMonitoringPlayerGuessingProgress();
                        Log.d(TAG, "onComplete: retrieved drawing");

                    } else {
                        Snackbar.make(((GuessingFragment) guessingView).getActivity().findViewById(R.id.fragment_container_main), "Room does not exist", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(((GuessingFragment) guessingView).getActivity().findViewById(R.id.fragment_container_main), "Something went Wrong, please try again", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }


    public ListenerRegistration monitorGuessingProgress(final GuessingContract.View guessingView, final GuessingPresenter guessingPresenter, final OnlineGame onlineGame) {
        final DocumentReference docRef = Drawmatic.getmFirebaseDb().collection("rooms").document(onlineGame.getOnlineSettings().getRoomName());

        docRef.collection("progressOfEachStep").document(((MainPresenter) ((MainActivity) mContext).getMainPresenter()).getCurrentPlayer().getPlayerId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map finishedCurrentStepMap = document.getData();
                        long finishedCurrentStep = (long) finishedCurrentStepMap.get("finishedCurrentStep");
                        finishedCurrentStep = (int) finishedCurrentStep;
                        Log.d(TAG, "onComplete: got status of progress in this step");

                        if (finishedCurrentStep == (onlineGame.getCurrentStep() - 1)) {
                            guessingPresenter.startGuessing();
                            Log.d(TAG, "onComplete: trans to guessing");
                        }
                    } else {
                        Snackbar.make(((MainActivity) mContext).findViewById(R.id.fragment_container_main), "Room does not exist", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(((MainActivity) mContext).findViewById(R.id.fragment_container_main), "Something went Wrong, please try again", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        ListenerRegistration listenerRegistration = docRef.collection("progressOfEachStep").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                // count how many people finished this step, 1 means finished, 0 means not yet
                int totalProgressOfThisStep = 0;
                int targetProgress = onlineGame.getCurrentStep() * onlineGame.getOnlineSettings().getPlayers().size();
                int maxProgressOfWholeGame = onlineGame.getTotalSteps() * onlineGame.getOnlineSettings().getPlayers().size();
                Log.d(TAG, "onEvent: \n\n");
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Map playerProgressMap = documentSnapshot.getData();
                    long playerProgressOfThisStep = (long) playerProgressMap.get("finishedCurrentStep");
                    playerProgressOfThisStep = (int) playerProgressOfThisStep;
                    Log.d(TAG, "onEvent: player progress" + playerProgressOfThisStep);
                    Log.d(TAG, "onEvent: current step" + onlineGame.getCurrentStep());
                    if (playerProgressOfThisStep == onlineGame.getCurrentStep()) {
                        totalProgressOfThisStep += onlineGame.getCurrentStep();
                    }
                }

                // if totalProgressOfThisStep == maxProgressOfWholeGame, it means the game should end
                // if the totalProgressOfThisStep == targetProgress, then it means every one finishes, so move the next step
                if (totalProgressOfThisStep == maxProgressOfWholeGame) {
                    guessingPresenter.finishGame(onlineGame);
                    guessingPresenter.unregisterListener();

                } else if (totalProgressOfThisStep == targetProgress) {
                    onlineGame.increamentCurrentStep();
                    guessingPresenter.transToDrawingPage();
                    guessingPresenter.unregisterListener();
                }
            }
        });

        return listenerRegistration;
    }

    public void updateGuessingStepProgressAndUploadGuessing(final GuessingContract.Presenter guessingPresenter, final OnlineGame onlineGame, String guessing) {
        DocumentReference currentUserDrawingsRef = Drawmatic.getmFirebaseDb().collection("rooms").document(onlineGame.getOnlineSettings().getRoomName()).collection("drawings").document(new TopicDrawingRetrievingUtil(mContext, onlineGame, ((MainPresenter) ((MainActivity) mContext).getMainPresenter()).getCurrentPlayer()).calcPlayerIdToRetrieveTopicOrDrawing());
        Map map = new HashMap();
        map.put(String.valueOf(onlineGame.getCurrentStep()), guessing);
        currentUserDrawingsRef.update(map).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                DocumentReference currentUserProgressRef = Drawmatic.getmFirebaseDb().collection("rooms").document(onlineGame.getOnlineSettings().getRoomName()).collection("progressOfEachStep").document(((MainPresenter) ((MainActivity) mContext).getMainPresenter()).getCurrentPlayer().getPlayerId());
                Map progressMap = new HashMap();
                progressMap.put("finishedCurrentStep", onlineGame.getCurrentStep());
                Log.d(TAG, "onComplete: putting guessing up to server: " + onlineGame.getCurrentStep());

                currentUserProgressRef.update(progressMap);
            }
        });
    }



    /**
     * *********************************************************************************
     * Getting Game Results
     * **********************************************************************************
     */
    public void retrieveGameResults(final GameResultContract.View gameResultView, final GameResultPresenter gameResultPresenter, final OnlineGame onlineGame) {
        String currentPlayerId = ((MainPresenter) ((MainActivity) mContext).getMainPresenter()).getCurrentPlayer().getPlayerId();

        final DocumentReference docRef = Drawmatic.getmFirebaseDb().collection("rooms").document(onlineGame.getOnlineSettings().getRoomName()).collection("drawings").document(currentPlayerId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map documentMap = document.getData();

                    if (document.exists()) {
                        // transform to bitmap first
//                        ArrayList<String> resourceList = new ArrayList<>();
//                        for(int i = 1; i <= documentMap.size(); i++) {
//                            resourceList.add((String) documentMap.get(i));
//                        }
//                        gameResultPresenter.informToShowResults(new ResultAsBitmapsUtil(mContext, gameResultPresenter).generateFrom(resourceList));

                        // passing strings
                        ArrayList<String> resourceStringList = new ArrayList<>();
                        for (int i = 1; i <= documentMap.size(); i++) {
                            resourceStringList.add((String) documentMap.get(String.valueOf(i)));
                        }

                        gameResultPresenter.informToShowResults(resourceStringList);
                    } else {
                        Snackbar.make(((GameResultFragment) gameResultView).getActivity().findViewById(R.id.fragment_container_main), "Player data does not exist", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(((GameResultFragment) gameResultView).getActivity().findViewById(R.id.fragment_container_main), "Something went Wrong, please try again", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

}
