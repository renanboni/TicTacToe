package com.example.renanboni.tictactoe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.renanboni.tictactoe.engine.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), TicTacToeListener, TicTacToeView.SquarePressedListener {

    private lateinit var ticTacToe: TicTacToe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ticTacToe = TicTacToe()
        ticTacToe.setTicTacToeListener(this)
        ticTacToeView.squarePressListener = this

        resetButton.setOnClickListener { resetGame() }
    }

    override fun gameEndsWithATie() {
        information.visibility = View.VISIBLE
        information.text = "Game ends in a draw"
        resetButton.visibility = View.VISIBLE
        ticTacToeView.isEnabled = false
    }

    override fun movedAt(x: Int, y: Int, move: Int) {
        if(move == BoardState.MOVE_X) {
            ticTacToeView.drawXAtPosition(x, y)
        } else {
            ticTacToeView.drawOAtPosition(x, y)
        }
    }

    override fun gameWonBy(boardPlayer: BoardPlayer, winCoords: Array<SquareCoordinates>) {
        information.visibility = View.VISIBLE
        information.text = "Winner is ${if (boardPlayer.move == BoardState.MOVE_X) "X" else "O"}"
        ticTacToeView.animateWin(winCoords[0].i, winCoords[0].j, winCoords[2].i, winCoords[2].j)
        ticTacToeView.isEnabled = false
        resetButton.visibility = View.VISIBLE
    }

    override fun onSquarePressed(i: Int, j: Int) {
        ticTacToe.moveAt(i, j)
    }

    private fun resetGame() {
        ticTacToeView.reset()
        ticTacToeView.isEnabled = true
        information.visibility = View.GONE
        resetButton.visibility = View.GONE
    }
}
