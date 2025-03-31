/**
 * A tokenizer for encoding and decoding LZ77-style compression tokens in a readable format.
 * Tokens are represented as: "^distance,length^".
 */
public class ReadableTokenizer implements Tokenizer {

    //TODO: TASK 4
    public String toTokenString(int distance, int length) {
        return ("^"+distance+","+length+"^");
    }

    // TODO TASK 4
    public int[] fromTokenString(String tokenText, int index) {
        int[] result = new int[3];
        int startIndex = tokenText.indexOf('^', index);
        int endIndex = tokenText.indexOf('^', startIndex + 1);
        String token = tokenText.substring(startIndex + 1, endIndex);
        String[] parts = token.split(",");
        result[0] = Integer.parseInt(parts[0]);
        result[1] = Integer.parseInt(parts[1]);
        result[2] = endIndex - startIndex + 1;
        return result;
    }
}