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

    public class OnlineGameMode {
        public static final int ONLINE_NORMAL = 1000;
        public static final int ONLINE_TEAM = 1100;
        public static final int OFFLINE_ORIGINAL = 1200;
        public static final int OFFLINE_TEAM = 1300;
    }

    public class PlayerType {
        public static final int ROOM_MASTER = 2000;
        public static final int PARTICIPANT = 2100;
    }
}
