package shpp.mentor;

public class errJsonBuilder {

    errJsonBuilder(){}
    public static String errLogPrep(String log) {
        return log.substring(0,log.length()-1)+"]}";
    }
}
