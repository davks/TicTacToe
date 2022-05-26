fun main() {
    val ttt = TicTacToe()

    do {
        ttt.play()
        val continuePlay = ttt.playAgain()
    } while (continuePlay)
}