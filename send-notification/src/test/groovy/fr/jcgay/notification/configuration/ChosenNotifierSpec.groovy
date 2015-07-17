package fr.jcgay.notification.configuration

import spock.lang.Specification


class ChosenNotifierSpec extends Specification {

    def "should create chosen notifier for a single primary notifier"() {
        when:
        def result = ChosenNotifiers.from('growl')

        then:
        result.primary() == 'growl'
        result.secondary().isEmpty()
    }

    def "should create chosen notifier for a primary and secondary notifier"() {
        when:
        def result = ChosenNotifiers.from('growl,anybar,pushbullet')

        then:
        result.primary() == 'growl'
        result.secondary() == ['anybar', 'pushbullet'] as LinkedHashSet
    }
}
