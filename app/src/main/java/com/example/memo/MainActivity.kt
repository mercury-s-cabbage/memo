package com.example.memo
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.memo.R
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val cards = mutableListOf<ImageView>()

    private val openedCards = mutableListOf<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val cardWidth = 200
        val cardHeight = 300

        val images = listOf(
            R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4,
            R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4,
            R.drawable.img5, R.drawable.img6, R.drawable.img7, R.drawable.img8,
            R.drawable.img5, R.drawable.img6, R.drawable.img7, R.drawable.img8,
        ).shuffled() // Перемешиваем порядок изображений

        // Создаем массив строк (горизонтальных контейнеров)
        val rows = Array(4) {
            LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        }


        for (i in 0 until 16) {
            val card = ImageView(this).apply {
                setImageResource(R.drawable.back)
                layoutParams = LinearLayout.LayoutParams(cardWidth, cardHeight).apply {
                    marginEnd = 10
                }
                tag = images[i]
                setOnClickListener(cardClickListener)
            }
            cards.add(card)
            rows[i / 4].addView(card)
        }


        rows.forEach { mainLayout.addView(it) }


        setContentView(mainLayout)
    }

    private val cardClickListener = View.OnClickListener { view ->
        val card = view as ImageView


        if (openedCards.contains(card) || openedCards.size == 2) return@OnClickListener


        card.setImageResource(card.tag as Int)
        openedCards.add(card)


        if (openedCards.size == 2) {
            GlobalScope.launch(Dispatchers.Main) {
                delay(1000)
                checkMatch()
            }
        }
    }


    private fun checkMatch() {

        if (openedCards[0].tag == openedCards[1].tag) {
            openedCards.forEach {
                it.visibility = View.INVISIBLE
                it.isClickable = false
            }
        } else {

            openedCards.forEach { it.setImageResource(R.drawable.back) }
        }
        openedCards.clear()


        if (cards.all { it.visibility == View.INVISIBLE }) {
            Toast.makeText(this, "You Win!", Toast.LENGTH_SHORT).show()
        }
    }
}