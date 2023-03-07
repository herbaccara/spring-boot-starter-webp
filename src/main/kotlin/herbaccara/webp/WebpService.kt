package herbaccara.webp

import java.io.File
import java.io.IOException
import java.time.Duration
import java.util.concurrent.TimeUnit

class WebpService(
    private val cwebpPath: File
) {

    // https://developers.google.com/speed/webp/docs/cwebp?hl=ko
    @JvmOverloads
    fun compress(
        source: File,
        dest: File,
        q: Double = 75.0,
        m: Int = 4,
        lossless: Boolean = true,
        timeout: Duration = Duration.ofMinutes(5)
    ) {
        if (q !in 0.0..100.0) throw IllegalArgumentException("q 범위는 0 부터 100")
        if (m !in 0..6) throw IllegalArgumentException("m 범위는 0 부터 6")

        val extension = source.extension
        // TODO: 확장자 체크

        val commands = listOfNotNull(
            cwebpPath.absolutePath,
            "-m",
            m.toString(),
            "-q",
            q.toString(),
            if (lossless) "-lossless" else null,
            source.absolutePath,
            "-o",
            dest.absolutePath
        )

        val stdout = File.createTempFile("stdout", "webp")

        val process = ProcessBuilder(commands).apply {
            redirectErrorStream(true)
            redirectOutput(stdout)
        }.start()

        try {
            process.waitFor(timeout.toMillis(), TimeUnit.MILLISECONDS)
            val exitStatus = process.exitValue()
            if (exitStatus != 0) {
                throw IOException(stdout.readText())
            }
        } catch (e: InterruptedException) {
            throw IOException(e)
        } finally {
            process.destroy()
            if (process.isAlive) {
                process.destroyForcibly()
            }
            stdout.delete()
        }
    }

    // https://developers.google.com/speed/webp/docs/dwebp?hl=ko
    fun decompress(source: File, dest: File) {

    }
}