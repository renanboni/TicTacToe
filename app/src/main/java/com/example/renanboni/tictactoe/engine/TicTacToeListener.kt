package com.example.renanboni.tictactoe.engine

interface TicTacToeListener {

    fun gameEndsWithATie()

    fun movedAt(x: Int, y: Int, move: Int)

    fun gameWonBy(boardPlayer: BoardPlayer, winPoints: Array<SquareCoordinates>)
}