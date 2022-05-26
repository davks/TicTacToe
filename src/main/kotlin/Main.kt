fun main() {
    val ttt = TicTacToe(7)

    do {
        ttt.play()
        val continuePlay = ttt.playAgain()
    } while (continuePlay)
}