import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;



public class Main {
    Main(String url) {

        try {
            Document doc = Jsoup.connect(url).get();
            String text = doc.body().text();
            findName(text);
        } catch (IOException e) {
            System.out.println("Something went wrong");
            e.printStackTrace();
        }
    }

    public void findName(String text) throws IOException {
        InputStream is1 = getClass().getResourceAsStream("/models/en-sent.bin");
        InputStream is2 = getClass().getResourceAsStream("/models/en-token.bin");
        InputStream is3 = getClass().getResourceAsStream("/models/en-ner-person.bin");
        SentenceModel sentenceModel = new SentenceModel(is1);
        TokenizerModel tokenizerModel = new TokenizerModel(is2);
        TokenNameFinderModel tokenNameFinderModel = new TokenNameFinderModel(is3);

        SentenceDetector sentenceDetector = new SentenceDetectorME(sentenceModel);
        String sentence[] = sentenceDetector.sentDetect(text);
        TokenizerME tokenizerME = new TokenizerME(tokenizerModel);
        NameFinderME nameFinderME = new NameFinderME(tokenNameFinderModel);

        for (int j = 0; j < sentence.length; j++) {
            String[] x = tokenizerME.tokenize(sentence[j]);


            Span[] nameSpans = nameFinderME.find(x);
            String names = Arrays.toString(Span.spansToStrings(nameSpans, x));
            if (!names.equals("[]")) {
                System.out.println("FOR SENTENCE NUMBER " + (j + 1) + " THE NAMES FROM THE WEB PAGE : " + names);
            }else System.out.println("THERE ARE NO NAMES IN "+(j+1)+". SENTENCE");
            }
    }


    public static void main(String[] args) {
        new Main(args[0]);

    }
}