package org.semanticweb.owlapi.functional.parser;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CustomTokenizerTest {
    static Logger logger = LoggerFactory.getLogger(CustomTokenizer.class);

    @Test
    public void testParseStringLiteral() throws Exception {
        validateTokenizationOfString("\"hello world\"");
        validateTokenizationOfString("\"hello \\\" world \"");
        validateTokenizationOfString("\"hello \\\\ world\"");
        validateTokenizationOfString("\"hello\\\\\"" + "\"world\"");
    }

    @Test
    public void testParseFullURI() throws Exception {
        // Good
        validateTokenizationOfString("<http://www.unc.edu/onto#foo>");
        // Bad
        validateTokenizationOfString("<http://www.unc.edu/onto#foo");

    }

    @Test
    public void testTokenizeGeneOntology() throws Exception {
        String fileName = "/Users/ses/ontologies/GO/go.ofn";
        Reader in = new BufferedReader(new FileReader(fileName));
        Reader in2 = new BufferedReader(new FileReader(fileName));
        validateTokenization(in, in2);
    }

    private void validateTokenizationOfString(String text) {
        try (StringReader reader1 = new StringReader(text);
             StringReader reader2 = new StringReader(text);) {
            validateTokenization(reader1, reader2);
        }
    }

    private void validateTokenizationOfResource(String resourceName) throws IOException {
        try (Reader reader1 = new InputStreamReader(getClass().getResourceAsStream(resourceName));
             Reader reader2 = new InputStreamReader(getClass().getResourceAsStream(resourceName))) {
            validateTokenization(reader1, reader2);
        }
    }

    private void validateTokenization(Reader reader1, Reader reader2) {
        CustomTokenizer customTokenizer = createCustomTokenizer(reader1);
        OWLFunctionalSyntaxParserTokenManager tokenManager = createTokenManager(reader2);
        while (true) {
            Token customToken = customTokenizer.getNextToken();
            Token generatedToken = tokenManager.getNextToken();
            assertTokensMatch(generatedToken, customToken);
            // If the end of file occurs in the middle of a token, the existing lexical grammar creates an error token,
            // then keeps going.  None of the production rules match the error, so this will  quickly lead to a parse
            // exception when called from the generated parser;  the tokenizer just keeps on chugging, as it doesn't
            // know that the error is a lexical error.
            // With the newest versions of JavaCC, tokenization errors are treated as exceptions, rather than can't
            // happen errors, however the generated code doesn't compile with a custom lexer.
            if (generatedToken.kind == OWLFunctionalSyntaxParserConstants.EOF ||
                    generatedToken.kind == OWLFunctionalSyntaxParser.ERROR) {
                return;
            }
        }
    }

    private CustomTokenizer createCustomTokenizer(String s) {
        StringReader reader = new StringReader(s);
        return createCustomTokenizer(reader);
    }

    private CustomTokenizer createCustomTokenizer(Reader reader) {
        return new CustomTokenizer(reader);
    }


    private OWLFunctionalSyntaxParserTokenManager createTokenManager(String s) {
        StringReader reader = new StringReader(s);
        return createTokenManager(reader);
    }

    private OWLFunctionalSyntaxParserTokenManager createTokenManager(Reader reader) {
        return new OWLFunctionalSyntaxParserTokenManager(new SimpleCharStream(reader));
    }

    private void assertTokensMatch(Token expected, Token actual) {
        try {
            assertNotNull(expected);
            assertNotNull(actual);
            assertEquals(expected.kind, actual.kind);
            assertEquals(expected.image, actual.image);
            assertEquals(expected.getValue(), actual.getValue());
        } catch (AssertionError e) {
            String expected1 = OWLFunctionalSyntaxParserConstants.tokenImage[expected.kind];
            String actual1 = OWLFunctionalSyntaxParserConstants.tokenImage[actual.kind];
            logger.error("token match fail: expected " + expected1 + ", actual " + actual1);
            throw e;
        }
    }


}