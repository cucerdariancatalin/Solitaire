package cucerdariancatalin.solitaire

object Log { // mock Log util for testing
    fun d(tag: String, msg: String): Int {
        println("DEBUG: $tag: $msg");
        return 0;
    }
}