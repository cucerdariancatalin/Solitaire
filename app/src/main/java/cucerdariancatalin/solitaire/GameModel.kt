package cucerdariancatalin.solitaire

object GameModel {
    val deck = Deck()
    val wastePile: MutableList<Card> = mutableListOf()
    val foundationPiles: Array<FoundationPile> = arrayOf(FoundationPile(spades), FoundationPile(hearts),
        FoundationPile(diamonds), FoundationPile(clubs))
    val tableauPiles = Array(7, { TableauPile() })

    fun resetGame() {
        wastePile.clear()
        foundationPiles.forEach { it.reset() }
        deck.reset()
        tableauPiles.forEachIndexed { i, tableauPile ->
            val cardsInPile: MutableList<Card> = Array(i + 1, { deck.drawCard() }).toMutableList()
            tableauPiles[i] = TableauPile(cardsInPile)
        }
    }

    fun onDeckTap() {
        if (deck.cardsInDeck.size > 0) {
            val card = deck.drawCard()
            card.faceUp = true
            wastePile.add(card)
        } else {
            deck.cardsInDeck = wastePile.toMutableList()
            wastePile.clear()
        }
        if(checkWon()){
            GamePresenter.gameWon()
        }
    }

    fun onWastePileTap() {
        if (wastePile.size > 0) {
            val card = wastePile.last()
            foundationPiles.forEach {
                if (playCard(card)) {
                    wastePile.remove(card)
                    return
                }
            }
        }
        if(checkWon()){
            GamePresenter.gameWon()
        }
    }

    fun onFoundationPileTap(foundationIndex: Int) {
        val foundationPile = foundationPiles[foundationIndex]
        if (foundationPile.cards.size > 0) {
            val card = foundationPile.cards.last()
            if (playCard(card)) foundationPile.removeCard(card)
        }
        if(checkWon()){
            GamePresenter.gameWon()
        }
    }

    fun onTableauTap(tableauIndex: Int, cardIndex: Int) {
        val tableauPile = tableauPiles[tableauIndex]
        if (tableauPile.cards.size > 0) {
            if (tableauPile.cards[cardIndex].faceUp) {
                val cards = tableauPile.cards.subList(cardIndex, tableauPile.cards.lastIndex + 1)
                if (playCards(cards)) {
                    tableauPile.removeCards(cardIndex)
                }
            }
        }

        if(checkWon()){
            GamePresenter.gameWon()
        }
    }

    private fun checkWon() : Boolean {
        if (foundationPiles[0].cards.size == 13 &&
            foundationPiles[1].cards.size == 13 &&
            foundationPiles[2].cards.size == 13 &&
            foundationPiles[3].cards.size == 13) return true
        return false
    }

    private fun playCards(cards: MutableList<Card>): Boolean {
        if (cards.size == 1) return playCard(cards.first())
        else {
            tableauPiles.forEach {
                if (it.addCard(cards)) return true
            }

            tableauPiles.forEach {
                if(it.addCardWhenEmptyPile(cards)) return true
            }
        }
        return false
    }

    private fun playCard(card: Card): Boolean {
        foundationPiles.forEach {
            if (it.addCard(card)) return true
        }
        tableauPiles.forEach {
            if (it.addCard(mutableListOf(card))) return true
        }
        tableauPiles.forEach {
            if (it.addCardWhenEmptyPile(mutableListOf(card))) return true
        }
        return false
    }

    fun debugPrint() {
//        GameModel.onDeckTap()
        var p = ""
        p += "\n"
        var firstLine = if (wastePile.isNotEmpty()) "${wastePile.last()}" else "___"
        firstLine.padEnd(18)
        foundationPiles.forEach {
            firstLine += if (it.cards.isNotEmpty()) "${it.cards.last()}" else "___"
            firstLine += "   "
        }
//        Log.d("tag", firstLine)
//        Log.d("tag", "\n")
        p += firstLine
        p += "\n"
        for (i in 0..12) {
            var row = ""
            tableauPiles.forEach {
                row += if (it.cards.size > i) "${it.cards[i]}" else "   "
                row += "   "
            }
//            Log.d("tag", row)
            p += "\n"
            p += row
        }
        Log.d("tag", p)

    }
}