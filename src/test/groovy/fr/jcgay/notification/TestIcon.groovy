package fr.jcgay.notification


class TestIcon {

    static Icon ok() {
        Icon.create(TestIcon.class.getResource("/image/dialog-clean.png"), "ok");
    }
}
