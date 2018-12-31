package com.example.renanboni.tictactoe.engine

import android.support.annotation.IntRange
import com.example.renanboni.tictactoe.engine.BoardState.*
import java.lang.IllegalArgumentException


/**
 * -----------------
 * (0,0) (0,1) (0,2)
 * (1,0) (1,1) (1,2)
 * (2,0) (2,1) (2,2)
 * ------------------
 */

class TicTacToe {

    companion object {
        const val BOARD_ROW = 3
        const val BOARD_COLUMN = 3
    }

    private var numberOfMoves = 0
    private var ticTacToeListener: TicTacToeListener? = null

    private var board: Array<Array<Int>>
    private var playerToMove = BoardPlayer.PLAYER_X

    init {
        board = Array(BOARD_ROW) {Array(BOARD_COLUMN) {0}}
        playerToMove = BoardPlayer.PLAYER_X
        numberOfMoves = 0
    }

    fun setTicTacToeListener(listener: TicTacToeListener) {
        this.ticTacToeListener = listener
    }

    @BoardState
    fun getMovieAt(x: Int, y: Int): Int {
        return if(board[x][y] == SPACE) {
            SPACE
        } else if(board[x][y] == MOVE_O) {
            MOVE_O
        } else {
            MOVE_X
        }
    }

    fun moveAt(@IntRange(from = 0, to = 2) x: Int, @IntRange(from = 0, to = 2) y: Int): Boolean {
        if(x < 0 || x > BOARD_ROW - 1 || y < 0 || y > BOARD_COLUMN - 1) {
            throw IllegalArgumentException("Coordinates $x and $y are not valid. ")
        }

        if(!isValidMove(x, y)) {
            return false
        }

        numberOfMoves++
        ticTacToeListener?.movedAt(x, y, playerToMove.move)

        board[x][y] = playerToMove.move

        val won = hasWon(x, y, playerToMove)

        if(won.first) {
            ticTacToeListener?.gameWonBy(playerToMove, won.second)
        } else if(numberOfMoves == BOARD_COLUMN * BOARD_ROW) {
            ticTacToeListener?.gameEndsWithATie()
        }

        changeTurnToNextPlayer()
        return true
    }

    fun changeTurnToNextPlayer() {
        playerToMove = if(playerToMove == BoardPlayer.PLAYER_X) {
            BoardPlayer.PLAYER_O
        } else {
            BoardPlayer.PLAYER_X
        }
    }

    private fun checkDiagonals(x: Int, y: Int, move: Int, winCoordinates: Array<SquareCoordinates>): Boolean {
        if((board[0][0] == move && board[1][1] == move && board[2][2] == move)) {
            winCoordinates[0] = SquareCoordinates(0, 0)
            winCoordinates[1] = SquareCoordinates(1, 1)
            winCoordinates[2] = SquareCoordinates(2, 2)
            return true
        } else if((board[0][2] == move && board[1][1] == move && board[2][0] == move)) {
            winCoordinates[0] = SquareCoordinates(0, 2)
            winCoordinates[1] = SquareCoordinates(1, 1)
            winCoordinates[2] = SquareCoordinates(2, 0)
            return true
        }
        return false
    }

    private fun checkColumn(x: Int, y: Int, moveToCheck: Int, winCoordinates: Array<SquareCoordinates>): Boolean {
        for(i in 0..BOARD_ROW) {
            if(board[i][y] != moveToCheck) {
                return false
            }
        }

        for(i in 0..winCoordinates.size) {
            winCoordinates[i] = SquareCoordinates(i, y)
        }

        return true
    }

    private fun checkRow(x: Int, y: Int, moveToCheck: Int, winCoordinates: Array<SquareCoordinates>): Boolean {
        for(i in 0..BOARD_COLUMN) {
            if(board[x][i] != moveToCheck) {
                return false
            }
        }

        for(i in 0..winCoordinates.size) {
            winCoordinates[i] = SquareCoordinates(x, i)
        }

        return true
    }

    private fun hasWon(x: Int, y: Int, playerToMove: BoardPlayer): Pair<Boolean, Array<SquareCoordinates>> {
        val winCoordinates: Array<SquareCoordinates> = Array(3) { SquareCoordinates(0, 0) }

        val hasWon = checkRow(x, y, playerToMove.move, winCoordinates) || checkColumn(x, y, playerToMove.move, winCoordinates)
            || checkDiagonals(x, y, playerToMove.move, winCoordinates)

        return Pair(hasWon, winCoordinates)
    }


    fun isValidMove(x: Int, y: Int) = board[x][y] == SPACE
}