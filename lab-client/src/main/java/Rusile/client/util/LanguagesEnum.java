package Rusile.client.util;

public enum LanguagesEnum {

    ENGLISH("English", "en"),
    SLOVAK("Slovenský", "sk"),
    LITHUANIAN("Lietuviškas", "lt"),
    SPANISH("Español", "es");

    private final String name;
    private final String languageName;
    public static LanguagesEnum lg = LanguagesEnum.ENGLISH;
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
