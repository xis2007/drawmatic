package com.justinlee.drawmatic.firabase_operation;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
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
import com.justinlee.drawmatic.objects.Player;
import com.justinlee.drawmatic.util.DrawViewToImageGenerator;
import com.justinlee.drawmatic.util.TopicDrawingRetrievingUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class OnlineInGameManager {
    private static final String TAG = "onlineRoomManagerrrrrr";

    private Context mContext;
    private FirebaseFirestore mFirebaseDb;
    private Player mCurrentPlayer;

    public OnlineInGameManager(Context context) {
        mContext = context;
        mFirebaseDb = Drawmatic.getmFirebaseDb();
        mCurrentPlayer = ((MainPresenter) ((MainActivity) mContext).getMainPresenter()).getCurrentPlayer();
    }

    /**
     * *********************************************************************************
     * In game monitoring and progress updates (Set Topic Fragment)
     * **********************************************************************************
     */
    public void monitorSetTopicProgress(final MainContract.View mainView, final SetTopicPresenter setTopicPresenter, final OnlineGame onlineGame) {
        final DocumentReference docRef = Drawmatic.getmFirebaseDb()
                .collection("rooms")
                .document(onlineGame.getRoomId());

        // at beginning of the game, room master set all players current step progress to 0
        if (mCurrentPlayer.getPlayerType() == Constants.PlayerType.ROOM_MASTER) {
            WriteBatch batch = Drawmatic.getmFirebaseDb().batch();

            HashMap<String, Object> progressMap = new HashMap<>();
            for (Player player : onlineGame.getOnlineSettings().getPlayers()) {
                DocumentReference progressRef = docRef
                        .collection("progressOfEachStep")
                        .document(player.getPlayerId());
                progressMap.put("finishedCurrentStep", 0);
                batch.set(progressRef, progressMap);
            }

            batch.commit();
        }

        docRef
            .collection("progressOfEachStep")
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        DocumentReference currentUserDrawingsRef = Drawmatic.getmFirebaseDb()
                .collection("rooms")
                .document(onlineGame.getRoomId())
                .collection("drawings")
                .document(mCurrentPlayer.getPlayerId());

        Map<String, String> map = new HashMap();
        map.put(String.valueOf(onlineGame.getCurrentStep()), inputTopic);

        currentUserDrawingsRef
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        DocumentReference currentUserProgressRef = Drawmatic.getmFirebaseDb()
                                .collection("rooms")
                                .document(onlineGame.getRoomId())
                                .collection("progressOfEachStep")
                                .document(mCurrentPlayer.getPlayerId());
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
        TopicDrawingRetrievingUtil topicDrawingRetrievingUtil = new TopicDrawingRetrievingUtil(((DrawingFragment) drawingView).getActivity(), onlineGame, mCurrentPlayer);
        String playerIdToGetTopicOrDrawing = topicDrawingRetrievingUtil.calcPlayerIdToRetrieveTopicOrDrawing();
        final String dataNumber = String.valueOf(topicDrawingRetrievingUtil.calcItemNumberToRetrieveTopicOrDrawing());

        DocumentReference docRef = Drawmatic.getmFirebaseDb()
                .collection("rooms")
                .document(onlineGame.getRoomId())
                .collection("drawings")
                .document(playerIdToGetTopicOrDrawing);

        docRef
            .get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        Map documentMap = document.getData();
                        if (document.exists()) {
                            // setup the retrieved info to the page, and allow players to begin monitoring the progress of this step
                            // room master needs to reset all players' progress to 0
                            drawingPresenter.setTopic((String) documentMap.get(dataNumber));
                            drawingPresenter.setCurrentStep();
                            drawingPresenter.setPreviousPlayer();
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
        final DocumentReference docRef = Drawmatic.getmFirebaseDb()
                .collection("rooms")
                .document(onlineGame.getRoomId());

        docRef
            .collection("progressOfEachStep")
            .document(mCurrentPlayer.getPlayerId())
            .get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map finishedCurrentStepMap = document.getData();
                            long retrievedValue = (long) finishedCurrentStepMap.get("finishedCurrentStep");
                            int finishedCurrentStep = (int) retrievedValue;

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

        final ListenerRegistration listenerRegistration = docRef
                .collection("progressOfEachStep")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        final StorageReference playerInRoomRef = Drawmatic.getStorageReference()
                .child(onlineGame.getRoomId())
                .child(mCurrentPlayer.getPlayerId())
                .child(String.valueOf(onlineGame.getCurrentStep()) + ".jpg");

        byte[] data = new DrawViewToImageGenerator().generateToData(drawView);

        final UploadTask uploadTask = playerInRoomRef.putBytes(data);

        Task<Uri> urlTask = uploadTask
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return playerInRoomRef.getDownloadUrl();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUrl = task.getResult();
                            drawingPresenter.updateDrawingStepProgressAndUploadImageUrl(downloadUrl.toString());
                            drawingPresenter.saveUrlToOnlineGameObject(downloadUrl.toString());
                        }
                        // TODO handle error
                    }
                });
    }

    public void updateDrawingStepProgressAndUploadImageUrl(DrawingContract.Presenter drawingPresenter, final OnlineGame onlineGame, String downloadUrl) {
        DocumentReference currentUserDrawingsRef = Drawmatic.getmFirebaseDb()
                .collection("rooms")
                .document(onlineGame.getRoomId())
                .collection("drawings")
                .document(new TopicDrawingRetrievingUtil(mContext, onlineGame, mCurrentPlayer).calcPlayerIdToRetrieveTopicOrDrawing());

        Map map = new HashMap();
        map.put(String.valueOf(onlineGame.getCurrentStep()), downloadUrl);

        currentUserDrawingsRef
                .update(map)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        DocumentReference currentUserProgressRef = Drawmatic.getmFirebaseDb()
                                .collection("rooms")
                                .document(onlineGame.getRoomId())
                                .collection("progressOfEachStep")
                                .document(mCurrentPlayer.getPlayerId());

                        Map progressMap = new HashMap();
                        progressMap.put("finishedCurrentStep", onlineGame.getCurrentStep());
                        currentUserProgressRef.update(progressMap);
                    }
                });
    }

    /**
     * *********************************************************************************
     * In game monitoring and progress updates (Guessing Fragment)
     * **********************************************************************************
     */
    public void retrieveDrawingAndWordCount(final GuessingContract.View guessingView, final GuessingPresenter guessingPresenter, final OnlineGame onlineGame) {
        TopicDrawingRetrievingUtil topicDrawingRetrievingUtil = new TopicDrawingRetrievingUtil(mContext, onlineGame, mCurrentPlayer);
        String playerIdToGetTopicOrDrawing = topicDrawingRetrievingUtil.calcPlayerIdToRetrieveTopicOrDrawing();

        final String imageUrlDataNumber = String.valueOf(topicDrawingRetrievingUtil.calcItemNumberToRetrieveTopicOrDrawing());
        final String topicDataNumber = String.valueOf(Integer.valueOf(imageUrlDataNumber) - 1);

        final DocumentReference docRef = Drawmatic.getmFirebaseDb()
                .collection("rooms")
                .document(onlineGame.getRoomId())
                .collection("drawings")
                .document(playerIdToGetTopicOrDrawing);

        docRef
            .get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        Map documentMap = document.getData();

                        if (document.exists()) {
                            guessingPresenter.setWordCountHint(((String) documentMap.get(topicDataNumber)));
                            guessingPresenter.setDrawing((String) documentMap.get(imageUrlDataNumber));
                            guessingPresenter.setPreviousPlayer();
                            guessingPresenter.setCurrentStep();
                            guessingPresenter.startMonitoringPlayerGuessingProgress();

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
        final DocumentReference docRef = Drawmatic.getmFirebaseDb()
                .collection("rooms")
                .document(onlineGame.getRoomId());

        docRef
            .collection("progressOfEachStep")
            .document(mCurrentPlayer.getPlayerId())
            .get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map finishedCurrentStepMap = document.getData();
                            long finishedCurrentStep = (long) finishedCurrentStepMap.get("finishedCurrentStep");
                            finishedCurrentStep = (int) finishedCurrentStep;

                            if (finishedCurrentStep == (onlineGame.getCurrentStep() - 1)) {
                                guessingPresenter.startGuessing();
                            }

                        } else {
                            Snackbar.make(((MainActivity) mContext).findViewById(R.id.fragment_container_main), "Room does not exist", Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(((MainActivity) mContext).findViewById(R.id.fragment_container_main), "Something went Wrong, please try again", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });

        ListenerRegistration listenerRegistration = docRef
                .collection("progressOfEachStep")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        // count how many people finished this step, 1 means finished, 0 means not yet
                        int totalProgressOfThisStep = 0;
                        int targetProgress = onlineGame.getCurrentStep() * onlineGame.getOnlineSettings().getPlayers().size();
                        int maxProgressOfWholeGame = onlineGame.getTotalSteps() * onlineGame.getOnlineSettings().getPlayers().size();

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Map playerProgressMap = documentSnapshot.getData();
                            long playerProgressOfThisStep = (long) playerProgressMap.get("finishedCurrentStep");
                            playerProgressOfThisStep = (int) playerProgressOfThisStep;

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
        DocumentReference currentUserDrawingsRef = Drawmatic.getmFirebaseDb()
                .collection("rooms")
                .document(onlineGame.getRoomId())
                .collection("drawings")
                .document(new TopicDrawingRetrievingUtil(mContext, onlineGame, mCurrentPlayer).calcPlayerIdToRetrieveTopicOrDrawing());

        Map map = new HashMap();
        map.put(String.valueOf(onlineGame.getCurrentStep()), guessing);

        currentUserDrawingsRef
                .update(map)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        DocumentReference currentUserProgressRef = Drawmatic.getmFirebaseDb()
                                .collection("rooms")
                                .document(onlineGame.getRoomId())
                                .collection("progressOfEachStep")
                                .document(mCurrentPlayer.getPlayerId());

                        Map progressMap = new HashMap();
                        progressMap.put("finishedCurrentStep", onlineGame.getCurrentStep());
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
        final DocumentReference docRef = Drawmatic.getmFirebaseDb()
                .collection("rooms")
                .document(onlineGame.getRoomId())
                .collection("drawings")
                .document(mCurrentPlayer.getPlayerId());

        docRef
            .get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        Map documentMap = document.getData();

                        if (document.exists()) {
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


    /**
     * *********************************************************************************
     * Deleting data when leaving game
     * **********************************************************************************
     */
    public void leaveRoomAndDeleteDataWhileInGame(OnlineGame onlineGame) {
        // delete firestore data
        Drawmatic.getmFirebaseDb()
                .collection("rooms")
                .document(onlineGame.getRoomId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ((MainActivity) mContext).getMainPresenter().transToPlayPage();
                        ((MainActivity) mContext).hideLoadingUi();
                        Snackbar.make(((MainActivity) mContext).findViewById(R.id.fragment_container_main), "Key Player Left, Game Stopped", Snackbar.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ((MainActivity) mContext).hideLoadingUi();
                    }
                });

        // delete storage data
        for (String urlString : onlineGame.getImageUrlStrings()) {
            FirebaseStorage.getInstance()
                    .getReferenceFromUrl(urlString)
                    .delete();
        }
    }

    public void deleteDataAfterResult(OnlineGame onlineGame) {
        // delete firestore data
        Drawmatic.getmFirebaseDb()
                .collection("rooms")
                .document(onlineGame.getRoomId())
                .delete();

        // delete storage data
        for (String urlString : onlineGame.getImageUrlStrings()) {
            FirebaseStorage.getInstance()
                    .getReferenceFromUrl(urlString)
                    .delete();
        }
    }
}
