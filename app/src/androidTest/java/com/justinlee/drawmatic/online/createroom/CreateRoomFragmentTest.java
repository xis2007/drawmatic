package com.justinlee.drawmatic.online.createroom;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class CreateRoomFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void createRoomSuccess() {
//        for (int i = 0; i < 10; i++) {
            onView(withId(R.id.button_new_game_play))
                    .perform(click());

            onView(withId(R.id.edittext_room_name_create_room))
                    .perform(typeText("bullseye"))
                    .perform(closeSoftKeyboard());

            onView(withId(R.id.edittext_room_name_create_room))
                    .check(matches(withText("bullseye")));


            // input with nothing for room name
            onView(withId(R.id.edittext_room_name_create_room))
                    .perform(clearText());

            onView(withId(R.id.button_next_create_room))
                    .perform(click());

            onView(withId(R.id.recyclerview_room_waiting))
                    .check(doesNotExist());

//            onView(withId(R.id.button_cancel_create_room))
//                    .perform(click());


        // input with nothing for room name
        onView(withId(R.id.edittext_room_name_create_room))
                .perform(typeText("great"));

        onView(withId(R.id.button_next_create_room))
                .perform(click());

        onView(withId(R.id.layout_item_player))
                .perform(click());



//        }
    }
}