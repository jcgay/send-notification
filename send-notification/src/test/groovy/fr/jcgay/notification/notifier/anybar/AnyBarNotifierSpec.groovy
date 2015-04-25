package fr.jcgay.notification.notifier.anybar
import fr.jcgay.notification.Application
import fr.jcgay.notification.IconFileWriter
import fr.jcgay.notification.Notification
import fr.jcgay.notification.TestIcon
import spock.lang.Specification

class AnyBarNotifierSpec extends Specification {

    DatagramSocket socket
    IconFileWriter iconWriter
    Application application
    AnyBarNotifier notifier

    def setup() {
        socket = Mock(DatagramSocket)
        iconWriter = Mock(IconFileWriter)
        application = Application.builder('id', 'name', TestIcon.application()).build()
        notifier = new AnyBarNotifier(application, AnyBarConfiguration.byDefault(), socket, iconWriter)
    }

    def 'should configure timeout when application timeout is not the default one at notifier initialization'() {
        given:
        def application = Application.builder('id', 'name', TestIcon.application())
            .withTimeout(3L)
            .build()
        def notifierWithTimeout = new AnyBarNotifier(application, AnyBarConfiguration.byDefault(), socket, iconWriter)

        when:
        notifierWithTimeout.init()

        then:
        1 * socket.setSoTimeout(3)
    }

    def 'should write application icon when initiating notifier'() {
        when:
        notifier.init()

        then:
        1 * iconWriter.write(application.icon())
    }

    def 'should send application icon to AnyBar when notifier is initializing'() {
        DatagramPacket capturedPacket

        when:
        notifier.init()

        then:
        1 * socket.send(_) >> { arguments -> capturedPacket = arguments[0] as DatagramPacket}
        capturedPacket.port == AnyBarConfiguration.byDefault().port();
        capturedPacket.data == application.icon().id().bytes
        capturedPacket.length == application.icon().id().length()
        capturedPacket.address == InetAddress.getByName(AnyBarConfiguration.byDefault().host())
    }

    def 'should write notification icon when sending a notification'() {
        given:
        def notification = Notification.builder('title', 'message', TestIcon.ok()).build()

        when:
        notifier.send(notification)

        then:
        1 * iconWriter.write(notification.icon())
    }

    def 'should send notification icon to AnyBar when sending a notification'() {
        DatagramPacket capturedPacket

        given:
        def notification = Notification.builder('title', 'message', TestIcon.ok()).build()

        when:
        notifier.send(notification)

        then:
        1 * socket.send(_) >> { arguments -> capturedPacket = arguments[0] as DatagramPacket}
        capturedPacket.port == AnyBarConfiguration.byDefault().port();
        capturedPacket.data == notification.icon().id().bytes
        capturedPacket.length == notification.icon().id().length()
        capturedPacket.address == InetAddress.getByName(AnyBarConfiguration.byDefault().host())
    }

    def 'should close socket when notifier is closed'() {
        when:
        notifier.close()

        then:
        1 * socket.close()
    }
}
