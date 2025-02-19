package example.com.plugins


sealed interface Rss {
    data class RssNewsItem(
        val title:String,
        val date:String,
        val description: String,
        val imgLink: String
    ): Rss

    data class RssEventItem(
        val title:String,
        val date:String,
        val description: String,
        val author: String
    ) : Rss
}