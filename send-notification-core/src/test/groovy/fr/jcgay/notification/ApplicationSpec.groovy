package fr.jcgay.notification

import spock.lang.Specification


class ApplicationSpec extends Specification {

    def "should build an application"() {
        when:
        def icon = Icon.create(new URL('file:/icon.png'), 'icon')
        def application = Application.builder('id', 'name', icon).timeout(1).build()

        then:
        application.id() == 'id'
        application.name() == 'name'
        application.timeout() == 1
        application.icon() == icon
    }
}
