/**
 * Class for performing LZ77 compression/decompression.
 */


/**
 * Class for performing compression/decompression loosely based on LZ77.
 */
public class LZLite {
    public static int MAX_WINDOW_SIZE = 65535;
    private int windowSize;
    private String slidingWindow;
    private Tokenizer tokenizer;

    //TODO: TASK 1
    public LZLite(int windowSize, boolean readable) {
        if(readable){
            this.tokenizer = new ReadableTokenizer();
        } else{
            this.tokenizer = new LeanTokenizer();
        }
        if(windowSize<=MAX_WINDOW_SIZE) {
            this.windowSize = windowSize;
        }
        this.slidingWindow = "";
    }

    //TODO: TASK 2
    public void appendToSlidingWindow(String st) {
        if((st.length()+slidingWindow.length())<=windowSize){
            slidingWindow+=st;
        }
        else{
            int numShave = (st.length()+slidingWindow.length())-windowSize;
            slidingWindow = slidingWindow.substring(numShave) + st;
        }
    }

    //TODO: TASK 3
    public String maxMatchInWindow(String input, int pos) {
        String maxMatch = "";
        for (int i = 0; i < this.slidingWindow.length(); i++) {
            String match = "";
            for (int j = 0; pos + j < input.length() && i + j < slidingWindow.length() && input.charAt(pos + j) == slidingWindow.charAt(i + j); j++) {
                match += input.charAt(pos + j);
            }
            if (match.length() > maxMatch.length()) {
                maxMatch = match;
            }
        }
        return maxMatch;
    }

    //TODO: TASK 5
    public String zip(String input) {
        String compressed = "";
        for(int i = 0; i < input.length();) {
            String match = maxMatchInWindow(input,i);
            int d = slidingWindow.length() - slidingWindow.lastIndexOf(match);
            if(match.length()>0 && tokenizer.toTokenString(d,match.length()).length()<match.length()){
                    compressed+=tokenizer.toTokenString(d,match.length());
                    appendToSlidingWindow(input.substring(i, i + match.length()));
                    i+=match.length();
            } else {
                compressed += input.charAt(i);
                appendToSlidingWindow(String.valueOf(input.charAt(i)));
                i++;
            }
        }
        return compressed;
    }

    //TODO: TASK 6
    public static String zipFileName(String fileName) {
        if (fileName.length() > 4 && fileName.substring(fileName.length() - 4).equals(".txt")) {
            return fileName.substring(0,fileName.length() - 4)+".lz77.txt";
        }
        return null;
    }

    //TODO: TASK 6
    public static String unzipFileName(String fileName) {
        if (fileName.length() > 8 && fileName.substring(fileName.length() - 9).equals(".lz77.txt")) {
            return fileName.substring(0,fileName.length() - 9)+".decompressed.txt";
        }
        return null;
    }

    //TODO: TASK 7
    public static String zipFile(String file, int windowSize, boolean readable) {
        String zName = zipFileName(file);
        String st = FileUtils.readFile(file);
        LZLite zipping = new LZLite(windowSize, readable);
        FileUtils.writeFile(zName,zipping.zip(st));
        return zName;
    }

    //TODO: TASK 8
    public String unzip(String input) {
        String decompressed = "";
        for (int i = 0; i < input.length(); ) {
            if (input.charAt(i) == '^') {
                int[] token = tokenizer.fromTokenString(input, i);
                int distance = token[0];
                int length = token[1];
                int tokenLength = token[2];

                int startIndex = decompressed.length() - distance;
                for (int j = 0; j < length; j++) {
                    decompressed += decompressed.charAt(startIndex + j);
                }
                i += tokenLength;
            } else {
                decompressed += input.charAt(i);
                i++;
            }
        }
        return decompressed;
    }

    //TODO: TASK 9
    public static String unzipFile(String file, int windowSize, boolean readable) {
        String zName = unzipFileName(file);
        String st = FileUtils.readFile(file);
        LZLite zipping = new LZLite(windowSize, readable);
        FileUtils.writeFile(zName,zipping.unzip(st));
        return zName;
    }

    //TODO: TASK 9
    public static void main(String[] args) {
        String zipFileName = zipFile("test_files/genesis.txt",
                MAX_WINDOW_SIZE, false);
        String unzipFile = unzipFile(zipFileName,
                MAX_WINDOW_SIZE, false);
        System.out.println("Unzip to " + unzipFile +" completed!");
    }


    // DON'T DELETE THE GETTERS! THEY ARE REQUIRED FOR TESTING
    public int getWindowSize() {
        return windowSize;
    }

    public String getSlidingWindow() {
        return slidingWindow;
    }

    public Tokenizer getTokenizer() {
        return tokenizer;
    }
}
