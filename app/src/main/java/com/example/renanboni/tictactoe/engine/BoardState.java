package com.example.renanboni.tictactoe.engine;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({BoardState.SPACE, BoardState.MOVE_X, BoardState.MOVE_O})
public @interface BoardState {
    int SPACE = 0;
    int MOVE_X = 1;
    int MOVE_O = 2;
}
