package Rusile.client.util;

public enum LanguagesEnum {

    RUSSIAN("Русский", "ru"),
    ICELANDIC("Icelandic", "is"),
    SVENSKA("Svenska", "sv"),
    SPANISH("Español", "es");

    private final String name;
    private final String languageName;

    LanguagesEnum(String name, String languageName) {
        this.name = name;
        this.languageName = languageName;
    }

    public String getName() {
        return name;
    }

    public String getLanguageName() {
        return languageName;
    }

    @Override
    public String toString() {
        return name;
    }
}
