package com.example.renanboni.tictactoe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.renanboni.tictactoe.engine.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), TicTacToeListener, TicTacToeView.SquarePressedListener {

    private lateinit var ticTacToe: TicTacToe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ticTacToe = TicTacToe()
        ticTacToe.setTicTacToeListener(this)
        tictactoeview.squarePressListener = this


    }

    override fun gameEndsWithATie() {

    }

    override fun movedAt(x: Int, y: Int, move: Int) {
        if(move == BoardState.MOVE_X) {
            tictactoeview.drawXAtPosition(x, y)
        } else {
            tictactoeview.drawOAtPosition(x, y)
        }
    }

    override fun gameWonBy(boardPlayer: BoardPlayer, winPoints: Array<SquareCoordinates>) {

    }

    override fun onSquarePressed(i: Int, j: Int) {
        ticTacToe.moveAt(i, j)
    }
}
