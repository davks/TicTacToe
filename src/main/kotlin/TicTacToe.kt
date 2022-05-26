class TicTacToe {
    private var cells: String
    private var board: Array<Array<Char>>
    private var coordinates: Coordinates = Coordinates(0, 0)
    private var errorMessage = ""
    private var currentPlayer = 'X'
    private var endGame = false
    private var playerWin: Char? = null
    private var winX = 0
    private var winO = 0
    private var draw = 0

    init {
        cells = setCells()
        board = convertToBoard()
    }

    companion object {
        private const val FIELD = 3
    }

    fun play() {
        showBoard()

        while (!endGame) {
            enterCoordinates()
            move()
        }
        setScore()
        showScore()
    }

    fun playAgain(): Boolean {
        print("\nDo you want play again? (y/n): ")
        val res = readln()
        return if (res == "y") {
            clean()
            true
        } else {
            false
        }
    }

    private fun move() {
        if (checkCoordinates()) {
            if (editBoard()) {
                changePlayer()
                showBoard()
                checkResult()
                checkFullBoard()
            }
        }
        println(errorMessage)
    }

    private fun setCells(): String {
        var cells = ""
        for (i in 1 .. FIELD * FIELD) {
            cells = cells.plus(' ')
        }
        return cells
    }

    private fun setLine(): String {
        var line = ""
        for (i in 1..FIELD * 2 + 3) {
            line = line.plus('-')
        }
        return line
    }

    private fun convertToBoard(): Array<Array<Char>> {
        var board = arrayOf<Array<Char>>()
        for (i in 0 until FIELD) {
            var row = arrayOf<Char>()
            for (j in 0 until FIELD) {
                row += cells[i * j]
            }
            board += row
        }

        return board
    }

    private fun showBoard() {
        var rows = "${setLine()}\n"
        for (i in 0 until FIELD) {
            var row = ""
            for (j in 0 until FIELD) {
                row += "${board[i][j]} "
            }
            rows += "| ${row}|\n"
        }
        rows += setLine()

        println(rows)
    }

    private fun enterCoordinates() {
        coordinates = Coordinates(0, 0)

        print("Enter coordinates: ")
        val coordinatesStr = readln().trim()
        if (coordinatesStr.contains(" ")) {
            val (yStr, xStr) = coordinatesStr.split(" ")
            val y = yStr.toIntOrNull()
            val x = xStr.toIntOrNull()

            if (x != null && y != null) {
                coordinates = Coordinates(x,  y)
            }
        }
    }

    private fun checkCoordinates(): Boolean {
        errorMessage = when {
            coordinates.x == 0 && coordinates.y == 0 ->
                "You should enter numbers!"
            coordinates.x !in 1..FIELD || coordinates.y !in 1..FIELD ->
                "Coordinates should be from 1 to $FIELD!"
            else -> ""
        }

        return errorMessage == ""
    }

    private fun editBoard(): Boolean {
        var addPlayer = false
        if (board[coordinates.y - 1][coordinates.x - 1] == ' ') {
            board[coordinates.y - 1][coordinates.x - 1] = currentPlayer
            addPlayer = true
        }

        if (!addPlayer) {
            errorMessage = "This cell is occupied! Choose another one!"
        }

        return addPlayer
    }

    private fun checkResult() {
        for (i in 0 until FIELD) {
            // Check vertical
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][2] != ' ') {
                playerWin = board[i][0]
            }
            // Check horizontal
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[2][i] != ' ') {
                playerWin = board[0][i]
            }
        }
        // Check diagonal
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[2][2] != ' ') {
            playerWin = board[0][0]
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[2][0] != ' ') {
            playerWin = board[0][2]
        }
        if (playerWin != null) {
            endGame = true
        }
    }

    private fun checkFullBoard() {
        var fullBoard = 0
        for (i in 0 until FIELD) {
            for (j in 0 until FIELD) {
                if (board[i][j] != ' ') {
                    fullBoard++
                }
            }
        }
        if (fullBoard == FIELD * FIELD) {
            endGame = true
        }
    }

    private fun setScore() {
        if (playerWin != null) {
            if (playerWin == 'X') winX++ else winO++
        } else {
            draw++
        }
    }

    private fun showScore() {
        if (playerWin != null) {
            println("$playerWin wins\n")
        } else {
            println("Draw\n")
        }

        println("Score:")
        println("------------")
        println("Player X = $winX")
        println("Player O = $winO")
        println("Draw     = $draw")
    }

    private fun changePlayer() {
        currentPlayer = if (currentPlayer == 'X') 'O' else 'X'
    }

    private fun clean() {
        cells = setCells()
        board = convertToBoard()
        coordinates = Coordinates(0, 0)
        errorMessage = ""
        currentPlayer = 'X'
        endGame = false
        playerWin = null
    }
}