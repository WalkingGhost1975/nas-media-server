package de.gisdesign.nas.media.domain.audio;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.Validate;

/**
 * Valid MP3 genres.
 */
public enum Genre {

    BLUES("Blues"),
    CLASSIC_ROCK("Classic Rock"),
    COUNTRY("Country"),
    DANCE("Dance"),
    DISCO("Disco"),
    FUNK("Funk"),
    GRUNGE("Grunge"),
    HIP_HOP("Hip-Hop"),
    JAZZ("Jazz"),
    METAL("Metal"),
    NEW_AGE("New Age"),
    OLDIES("Oldies"),
    OTHER("Other"),
    POP("Pop"),
    R_AND_B("R&B (Rhythem and Blues)"),
    RAP("Rap"),
    REGGAE("Reggae"),
    ROCK("Rock"),
    TECHNO("Techno"),
    INDUSTRIAL("Industrial"),
    ALTERNATIVE("Alternative"),
    SKA("Ska"),
    DEATH_METAL("Death Metal"),
    PRANKS("Pranks"),
    SOUNDTRACK("Soundtrack"),
    EURO_TECHNO("Euro-Techno"),
    AMBIENT("Ambient"),
    TRIP_HOP("Trip-Hop"),
    VOCAL("Vocal"),
    JAZZ_FUNK("Jazz+Funk"),
    FUSION("Fusion"),
    TRANCE("Trance"),
    CLASSICAL("Classical"),
    INSTRUMENTAL("Instrumental"),
    ACID("Acid"),
    HOUSE("House"),
    GAME("Game"),
    SOUND_CLIP("Sound Clip"),
    GOSPEL("Gospel"),
    NOISE("Noise"),
    ALTERNATIVE_ROCK("Alternative Rock"),
    BASS("Bass"),
    SOUL("Soul"),
    PUNK("Punk"),
    SPACE("Space"),
    MEDITATIVE("Meditative"),
    INSTRUMENTAL_POP("Instrumental Pop"),
    INSTRUMENTAL_ROCK("Instrumental Rock"),
    ETHNIC("Ethnic"),
    GOTHIC("Gothic"),
    DARKWAVE("Darkwave"),
    TECHNO_INDUSTRIAL("Techno-Industrial"),
    ELECTRONIC("Electronic"),
    POP_FOLK("Pop-Folk"),
    EURODANCE("Eurodance"),
    DREAM("Dream"),
    SOUTHERN_ROCK("Southern Rock"),
    COMEDY("Comedy"),
    CULT("Cult"),
    GANGSTA("Gangsta"),
    TOP_40("Top 40"),
    CHRISTIAN_RAP("Christian Rap"),
    POP_FUNK("Pop/Funk"),
    JUNGLE("Jungle"),
    NATIVE_AMERICAN("Native American"),
    CABARET("Cabaret"),
    NEW_WAVE("New Wave"),
    PSYCHADELIC("Psychadelic"),
    RAVE("Rave"),
    SHOWTUNES("Showtunes"),
    TRAILER("Trailer"),
    LO_FI("Lo-Fi"),
    TRIBAL("Tribal"),
    ACID_PUNK("Acid Punk"),
    ACID_JAZZ("Acid Jazz"),
    POLKA("Polka"),
    RETRO("Retro"),
    MUSICAL("Musical"),
    ROCK_N_ROLL("Rock & Roll"),
    HARD_ROCK("Hard Rock"),
    FOLK("Folk"),
    FOLK_ROCK("Folk-Rock"),
    NATIONAL_FOLK("National Folk"),
    SWING("Swing"),
    FAST_FUSION("Fast Fusion"),
    BEBOB("Bebob"),
    LATIN("Latin"),
    REVIVAL("Revival"),
    CELTIC("Celtic"),
    BLUEGRASS("Bluegrass"),
    AVANTGARDE("Avantgarde"),
    GOTHIC_ROCK("Gothic Rock"),
    PROGRESSIVE_ROCK("Progressive Rock"),
    PSYCHEDELIC_ROCK("Psychedelic Rock"),
    SYMPHONIC_ROCK("Symphonic Rock"),
    SLOW_ROCK("Slow Rock"),
    BIG_BAND("Big Band"),
    CHORUS("Chorus"),
    EASY_LISTENING("Easy Listening"),
    ACOUSTIC("Acoustic"),
    HUMOUR("Humour"),
    SPEECH("Speech"),
    CHANSON("Chanson"),
    OPERA("Opera"),
    CHAMBER_MUSIC("Chamber Music"),
    SONATA("Sonata"),
    SYMPHONY("Symphony"),
    BOOTY_BRASS("Booty Brass"),
    PRIMUS("Primus"),
    PORN_GROOVE("Porn Groove"),
    SATIRE("Satire"),
    SLOW_JAM("Slow Jam"),
    CLUB("Club"),
    TANGO("Tango"),
    SAMBA("Samba"),
    FOLKLORE("Folklore"),
    BALLAD("Ballad"),
    POWER_BALLAD("Power Ballad"),
    RHYTMIC_SOUL("Rhytmic Soul"),
    FREESTYLE("Freestyle"),
    DUET("Duet"),
    PUNK_ROCK("Punk Rock"),
    DRUM_SOLO("Drum Solo"),
    A_CAPELA("A Capela"),
    EURO_HOUSE("Euro-House"),
    DANCE_HALL("Dance Hall"),
    GOA("Goa"),
    DRUM_AND_BASS("Drum & Bass"),
    CLUB_HOUSE("Club-House"),
    HARDCORE("Hardcore"),
    TERROR("Terror"),
    INDIE("Indie"),
    BRITPOP("British Pop"),
    NEGERPUNK("Negerpunk"),
    POLSK_PUNK("Polsk Punk"),
    BEAT("Beat"),
    CHRISTIAN_GANGSTA("Christian Gangsta"),
    HEAVY_METAL("Heavy Metal"),
    BLACK_METAL("Black Metal"),
    CROSSOVER("Crossover"),
    CONTEMPORARY_C("Contemporary C"),
    CHRISTIAN_ROCK("Christian Rock"),
    MERENGUE("Merengue"),
    SALSA("Salsa"),
    THRASH_METAL("Thrash Metal"),
    ANIME("Anime"),
    JPOP("JPop"),
    SYNTH_POP("SynthPop"),
    REMIX("Remix"),
    COVER("Cover");

    private static final Pattern PARSING_PATTERN = Pattern.compile("\\(([0-9]{1,3})\\)");

    // data members
    private String name;

    /**
     * constructor.
     *
     * @param name name of the genre.
     */
    private Genre(String name) {
        this.name = name;
    }

    /**
     * converts an integral value to its corresponding genre enum.
     *
     * @return a genre enum corresponding to the integral value.
     * @param genre integral value to be converted to a genre enum.
     * @throws IllegalArgumentException if the value is not a valid genre.
     */
    public static Genre getGenre(byte genre) throws IllegalArgumentException {
        for (Genre g : Genre.values()) {
            if (genre == g.ordinal()) {
                return g;
            }
        }
        throw new IllegalArgumentException("Invalid genre " + genre + ".");
    }

    /**
     * Converts music type in format <code>([ordinal])</code> into the Genre enum value..
     * @return a genre enum corresponding to the integral value.
     * @param musicType Music type string.
     * @throws IllegalArgumentException if the value is not a valid genre.
     */
    public static String getGenreName(String musicType) throws IllegalArgumentException {
        Validate.notNull(musicType, "MusicType is null.");
        String genreName = musicType;
        Matcher matcher = PARSING_PATTERN.matcher(musicType);
        if (matcher.find()) {
            String genreId = matcher.group(1);
            try {
                Byte genreByte = Byte.parseByte(genreId);
                genreName = getGenre(genreByte).getName();
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Invalid Genre String [" + musicType + "].");
            }
        }
        return genreName;
    }

    /**
     * return the name of the genre.
     *
     * @return thename of the genre.
     */
    public String getName() {
        return name;
    }

    /**
     * gets a string representation of the genre enum.
     *
     * @return a string representation of the genre enum.
     */
    @Override
    public String toString() {
        return "[" + getName() + "]";
    }
}
