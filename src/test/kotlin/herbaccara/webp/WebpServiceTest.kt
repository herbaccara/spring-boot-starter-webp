package herbaccara.webp

import org.junit.jupiter.api.Test
import java.io.File

class WebpServiceTest {

    @Test
    fun compress() {
        val webpService = WebpService(
            File("src/main/resources/libwebp-0.4.1-windows-x64/bin/cwebp.exe")
        )

        webpService.compress(
            File("src/test/resources/sample/works-doesnt-work.jpg"),
            File("src/test/resources/sample/works-doesnt-work.webp"),
        )
    }
}