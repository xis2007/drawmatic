package com.justinlee.drawmatic.constants;

public class FirebaseConstants {
    public static final String APP_ID_ADMOB = "ca-app-pub-3940256099942544/1033173712";
    public static final String TEST_APP_ID_ADMOB = "ca-app-pub-3940256099942544/6300978111";

    public class SharedPreferences {
        public static final String ROOMS = "Rooms";
        public static final String GAME_DATA = "Game_Data";
    }

    public class Firestore {
        public static final String COLLECTION_ROOMS = "rooms";
        public static final String COLLECTION_DRAWINGS = "drawings";
        public static final String COLLECTION_PROGRESS_EACH_STEP = "progressOfEachStep";
        public static final String COLLECTION_SETTINGS = "settings";


        public static final String DOCUMENT_FINISHED_CURRENT_STEP = "finishedCurrentStep";
        public static final String DOCUMENT_UPDATE_APP = "updateApp";

        public static final String KEY_IN_GAME = "inGame";
        public static final String KEY_PLAYER_NAME = "playerName";
        public static final String KEY_PLAYER_ID = "playerId";
        public static final String KEY_PLAYER_TYPE = "playerType";
        public static final String KEY_PLAYERS = "players";
        public static final String KEY_REQUIRED = "required";
        public static final String KEY_REQUIRED_verSION_CODE = "requiredVersionCode";
    }

    public class Storage {
        public static final String REF_ROOMS = "rooms";

    }
}
