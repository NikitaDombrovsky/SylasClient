package example.com.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        staticResources("/images", "news")
        get("/news") {
            call.respondText(buildRssXml(DataLists.rssNewsList.shuffled()), ContentType.Application.Xml)
        }
        get("/events") {
            call.respond(DataLists.rssEventList)
        }
    }
}

fun buildRssXml(list: List<Rss.RssNewsItem>): String {
    var xmlString = """
        <?xml version="1.0" encoding="UTF-8" ?>
        <rss version="2.0">
            <channel>
    """.trimIndent()
    list.forEach {
        xmlString += """
               
                    <item>
                        <title>${it.title}</title>
                        <date>${it.date}</date>
                        <description>${it.description}</description>
                        <image>${it.imgLink}</image>
                    </item> 
            """.trimIndent()
        
    }

    xmlString += """
            
            </channel>
        </rss>
    """.trimIndent()
    return xmlString
}
