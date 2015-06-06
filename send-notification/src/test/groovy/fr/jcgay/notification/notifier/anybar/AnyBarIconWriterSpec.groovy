package fr.jcgay.notification.notifier.anybar
import fr.jcgay.notification.TestIcon
import spock.lang.Specification

import javax.imageio.ImageIO

class AnyBarIconWriterSpec extends Specification {

    AnyBarIconWriter writer
    File destinationFolder

    def setup() {
        destinationFolder = File.createTempDir()
        writer = new AnyBarIconWriter(destinationFolder.path)
    }

    def cleanup() {
        destinationFolder.deleteOnExit()
    }

    def 'should write icons for classic and retina resolutions'() {
        when:
        writer.write(TestIcon.ok())

        then:
        def classic = new File("${destinationFolder}/ok.png")
        classic.exists()

        def classicImage = ImageIO.read(classic)
        classicImage.height == 19
        classicImage.width == 19

        and:
        def retina = new File("${destinationFolder}/ok@2x.png")
        retina.exists()

        def retinaImage = ImageIO.read(retina)
        retinaImage.height == 38
        retinaImage.width == 38
    }
}
