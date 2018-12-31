package com.example.renanboni.tictactoe.engine

import com.example.renanboni.tictactoe.engine.BoardState.SPACE

enum class BoardPlayer(move: Int) {

    PLAYER_X(BoardState.MOVE_X), PLAYER_O(BoardState.MOVE_O);

    var move = SPACE

    init {
        this.move = move
    }
}
