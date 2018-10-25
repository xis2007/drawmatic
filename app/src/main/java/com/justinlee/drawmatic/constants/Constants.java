package com.justinlee.drawmatic.constants;

public class Constants {

    public class Login {
        public static final int LOGIN_ACTIVITY = 100;

        public static final int LOGIN_SUCCESS = 0x11;
        public static final int LOGIN_EXIT = 0x12;
    }

    public class UserData {
        public static final String SHAREPREF_USER_DATA_KEY = "USER_DATA";

        public static final String SHAREPREF_USER_NAME_KEY = "USER_NAME";
        public static final String SHAREPREF_USER_ID_KEY = "USER_ID";
    }

    public class GameMode {
        public static final int ONLINE_NORMAL = 1000;
        public static final int ONLINE_TEAM = 1100;
        public static final int OFFLINE_NORMAL = 1200;
        public static final int OFFLINE_TEAM = 1300;
    }

    public class PlayerType {
        public static final int ROOM_MASTER = 2000;
        public static final int PARTICIPANT = 2100;
    }

    public class RoomViewType {
        public static final int INITIAL_SEARCH = 100;
        public static final int NO_RESULTS = 101;
        public static final int ROOM_RESULTS = 102;
    }

    public class FragmentFlag {
        public static final String FLAG_LEAVE_APP_ALERT = "LEAVE_APP_ALERT";
        public static final String FLAG_LEAVE_GAME_ALERT = "LEAVE_GAME_ALERT";
        public static final String FLAG_INSTRUCTIONS = "INSTRUCTIONS";
        public static final String FLAG_PLAY = "PLAY";
        public static final String FLAG_SETTINGS = "SETTINGS";

        public static final String FLAG_CREATE_ROOM = "CREATE_ROOM";
        public static final String FLAG_WAITING = "WAITING";
        public static final String FLAG_SET_TOPIC = "SET_TOPIC";
        public static final String FLAG_DRAWING = "DRAWING";
        public static final String FLAG_GUESSING = "GUESSING";
        public static final String FLAG_GAME_RESULT = "GAME_RESULT";

    }

    public static final String NO_STRING = "";
    public static final String SPACE_STRING = " ";
}
