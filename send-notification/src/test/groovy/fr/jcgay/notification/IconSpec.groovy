package fr.jcgay.notification

import spock.lang.Specification


class IconSpec extends Specification {

    def "should create an icon"() {

        setup:
        def content = new URL('file:/test.png')

        when:
        def result = Icon.create(content, 'id')

        then:
        result.content() == content
        result.id() == 'id'
    }

    def "should write image in temp folder when url protocol is not file"() {

        setup:
        def icon = Icon.create(getClass().getResource("/image/1px${extension}"), 'img')

        when:
        def result = icon.asPath()

        then:
        new File(result) == new File("${System.getProperty('java.io.tmpdir')}/send-notifications-icons/img${extension}")

        where:
        extension << ['.png', '.gif']
    }
}
