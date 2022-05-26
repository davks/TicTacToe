class TicTacToe(private var fieldLength: Int = 3) {
    private var cells: String
    private var board: Array<Array<Char>>
    private var coordinates: Coordinates = Coordinates(0, 0)
    private var errorMessage = ""
    private var currentPlayer = X
    private var endGame = false
    private var playerWin: Char? = null
    private var winX = 0
    private var winO = 0
    private var draw = 0
    private var rowCompletX: String
    private var rowCompletO: String

    init {
        if (fieldLength < 3) fieldLength = 3
        cells = setCells()
        board = convertToBoard()
        rowCompletX = setRowComplet("X")
        rowCompletO = setRowComplet("O")
    }

    companion object {
        private const val X = 'X'
        private const val O = 'O'
    }

    fun play() {
        showBoard()

        while (!endGame) {
            addCoordinates()
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
                checkBoard()
                checkFullBoard()
            }
        }
        println(errorMessage)
    }

    private fun convertToBoard(): Array<Array<Char>> {
        var board = arrayOf<Array<Char>>()
        var cur = 0

        for (i in 0 until fieldLength) {
            var row = arrayOf<Char>()
            for (j in 0 until fieldLength) {
                row += cells[cur]
                cur++
            }
            board += row
        }

        return board
    }

    private fun showBoard() {
        var rows = "${setLine()}\n"
        for (i in 0 until fieldLength) {
            var row = ""
            for (j in 0 until fieldLength) {
                row += "${board[i][j]} "
            }
            rows += "| ${row}|\n"
        }
        rows += setLine()

        println(rows)
    }

    private fun addCoordinates() {
        coordinates = Coordinates(0, 0)

        print("Enter coordinates ($currentPlayer): ")
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
            coordinates.x !in 1..fieldLength || coordinates.y !in 1..fieldLength ->
                "Coordinates should be from 1 to $fieldLength!"
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

    private fun checkFullBoard() {
        var fullBoard = 0
        for (i in 0 until fieldLength) {
            for (j in 0 until fieldLength) {
                if (board[i][j] != ' ') {
                    fullBoard++
                }
            }
        }
        if (fullBoard == fieldLength * fieldLength) {
            endGame = true
        }
    }

    private fun setScore() {
        if (playerWin != null) {
            if (playerWin == X) winX++ else winO++
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

        println("------------")
        println("Player X = $winX")
        println("Player O = $winO")
        println("Draw     = $draw")
        println("------------")
    }

    private fun changePlayer() {
        currentPlayer = if (currentPlayer == X) O else X
    }

    private fun clean() {
        cells = setCells()
        board = convertToBoard()
        coordinates = Coordinates(0, 0)
        errorMessage = ""
        currentPlayer = X
        endGame = false
        playerWin = null
    }

    private fun checkBoard() {
        checkBoardHorizontally()
        checkBoardVertically()
        checkBoardDiagonallyXY()
        checkBoardDiagonallyX()
        checkBoardDiagonallyY()
    }

    private fun checkBoardHorizontally() {
        for (i in 0 until fieldLength) {
            var row = ""
            for (j in 0 until fieldLength) { // row
                row += board[i][j]
            }
            setWinner(row)
        }
    }

    private fun checkBoardVertically() {
        for (i in 0 until fieldLength) {
            var row = ""
            for (j in 0 until fieldLength) { // row
                row += board[j][i]
            }
            setWinner(row)
        }
    }

    /**
     * Check diagonal from top/left to right
     */
    private fun checkBoardDiagonallyXY() {
        for (k in 0 until fieldLength) {
            var diagonalX = ""
            var diagonalY = ""
            for (i in 0 until fieldLength) {
                for (j in i + k until fieldLength step fieldLength) {
                    diagonalX += "${board[i][j]}"
                    diagonalY += "${board[j][i]}"
                }
            }
            setWinner(diagonalX)
            setWinner(diagonalY)
        }
    }

    /**
     * Check diagonal from top/right to bottom
     */
    private fun checkBoardDiagonallyY() {
        for (k in 0 until fieldLength) {
            var diagonal = ""
            for (i in 0 + k until fieldLength) {
                for (j in fieldLength - 1 - i + k downTo 0 step fieldLength) {
                    diagonal += "${board[i][j]}"
                }
            }
            setWinner(diagonal)
        }
    }

    /**
     * Check diagonal from top/right to left
     */
    private fun checkBoardDiagonallyX() {
        for (k in 0 until fieldLength) {
            var diagonal = ""
            for (i in 0 until fieldLength) {
                for (j in fieldLength - 1 - k - i  downTo 0 step fieldLength) {
                    diagonal += "${board[i][j]}"
                }
            }
            setWinner(diagonal)
        }
    }

    private fun setWinner(str: String) {
        if (str.contains(rowCompletX)) {
            playerWin = X
        }
        if (str.contains(rowCompletO)) {
            playerWin = O
        }

        if (playerWin != null) {
            endGame = true
        }
    }

    private fun setCells(): String {
        var cells = ""
        for (i in 1 .. fieldLength * fieldLength) {
            cells = cells.plus(' ')
        }
        return cells
    }

    private fun setLine(): String {
        var line = ""
        for (i in 1..fieldLength * 2 + 3) {
            line = line.plus('-')
        }
        return line
    }

    private fun setRowComplet(str: String): String {
        val rowComplet = if(fieldLength >= 5) 5 else 3
        var line = ""
        for (i in 1..rowComplet) {
            line += str
        }
        return line
    }

    /**
     * Old function only 3x3
     */
//    private fun checkResult() {
//        for (i in 0 until FIELD) {
//            // Check vertical
//            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][2] != ' ') {
//                playerWin = board[i][0]
//            }
//            // Check horizontal
//            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[2][i] != ' ') {
//                playerWin = board[0][i]
//            }
//        }
//        // Check diagonal
//        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[2][2] != ' ') {
//            playerWin = board[0][0]
//        }
//        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[2][0] != ' ') {
//            playerWin = board[0][2]
//        }
//        if (playerWin != null) {
//            endGame = true
//        }
//    }
}