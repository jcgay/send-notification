package fr.jcgay.notification.notifier.notify

import spock.lang.Specification
import spock.lang.Unroll

import static dorkbox.notify.Pos.*

class NotifyConfigurationSpec extends Specification {

    @Unroll
    def "should build default configuration when properties are [#empty]"() {
        when:
        def result = NotifyConfiguration.create(empty)

        then:
        result == NotifyConfiguration.byDefault()

        where:
        empty << [null, new Properties()]
    }

    @Unroll
    def "should build user configuration with position [#position]"() {
        when:
        def result = NotifyConfiguration.create(
            ['notifier.notify.position': position.name()] as Properties
        )

        then:
        result.position() == position
        result.withDarkStyle() == NotifyConfiguration.byDefault().withDarkStyle()

        where:
        position << [TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, CENTER]
    }

    @Unroll
    def "should build user configuration with dark style ? [#style]"() {
        when:
        def result = NotifyConfiguration.create(
            ['notifier.notify.darkstyle': style as String] as Properties
        )

        then:
        result.position() == NotifyConfiguration.byDefault().position()
        result.withDarkStyle() == style

        where:
        style << [true, false]
    }

    def "fail when position is not valid"() {
        when:
        NotifyConfiguration.create(
            ['notifier.notify.position': 'unknown'] as Properties
        )

        then:
        thrown(IllegalArgumentException)
    }

    def "get lighter style when darkstyle is not a boolean"() {
        when:
        def result = NotifyConfiguration.create(
            ['notifier.notify.darkstyle': 'maybe'] as Properties
        )

        then:
        !result.withDarkStyle()
    }
}
