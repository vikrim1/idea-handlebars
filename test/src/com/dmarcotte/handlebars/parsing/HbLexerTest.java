package com.dmarcotte.handlebars.parsing;

import com.intellij.lexer.Lexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.testFramework.PlatformLiteFixture;

import java.util.ArrayList;
import java.util.List;


public class HbLexerTest extends PlatformLiteFixture {

    Lexer _lexer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _lexer = new HbLexer();
    }

    private List<Token> lex(String str) {
        List<Token> tokens = new ArrayList<Token>();
        IElementType currentElement;

        _lexer.start(str);

        while((currentElement = _lexer.getTokenType()) != null) {
            tokens.add(new Token(currentElement, _lexer.getTokenText()));
            _lexer.advance();
        }

        return tokens;
    }

    public void testPlainMustache() {
        List<Token> tokens = lex("{{mustacheContent}}");

        assertEquals(3, tokens.size());
        int tokenIdx = -1;
        assertEquals(HbTokenTypes.OPEN, tokens.get(++tokenIdx).getElementType());
        assertEquals("{{", tokens.get(tokenIdx).getElementContent());
        assertEquals(HbTokenTypes.ID, tokens.get(++tokenIdx).getElementType());
        assertEquals("mustacheContent", tokens.get(tokenIdx).getElementContent());
        assertEquals(HbTokenTypes.CLOSE, tokens.get(++tokenIdx).getElementType());
        assertEquals("}}", tokens.get(tokenIdx).getElementContent());
    }

    public void testPlainMustacheWithContentPreamble() {
        List<Token> tokens = lex("Some content y'all {{mustacheContent}}");

        assertEquals(4, tokens.size());

        int tokenIdx = -1;
        assertEquals(HbTokenTypes.CONTENT, tokens.get(++tokenIdx).getElementType());
        assertEquals("Some content y'all ", tokens.get(tokenIdx).getElementContent());
        assertEquals(HbTokenTypes.OPEN, tokens.get(++tokenIdx).getElementType());
        assertEquals("{{", tokens.get(tokenIdx).getElementContent());
        assertEquals(HbTokenTypes.ID, tokens.get(++tokenIdx).getElementType());
        assertEquals("mustacheContent", tokens.get(tokenIdx).getElementContent());
        assertEquals(HbTokenTypes.CLOSE, tokens.get(++tokenIdx).getElementType());
        assertEquals("}}", tokens.get(tokenIdx).getElementContent());
    }

    // dm todo fix this test
    public void testNoMustaches() {
        List<Token> tokens = lex("Some content y'all ");

        assertEquals(1, tokens.size());

        int tokenIdx = -1;
        assertEquals(HbTokenTypes.CONTENT, tokens.get(++tokenIdx).getElementType());
        assertEquals("Some content y'all ", tokens.get(tokenIdx).getElementContent());
    }

    public void testPlainMustacheWithWhitespace() {
        List<Token> tokens = lex("{{\tmustacheContent }}");

        assertEquals(5, tokens.size());
        assertEquals(HbTokenTypes.OPEN, tokens.get(0).getElementType());
        assertEquals("{{", tokens.get(0).getElementContent());
        assertEquals(HbTokenTypes.WHITE_SPACE, tokens.get(1).getElementType());
        assertEquals("\t", tokens.get(1).getElementContent());
        assertEquals(HbTokenTypes.ID, tokens.get(2).getElementType());
        assertEquals("mustacheContent", tokens.get(2).getElementContent());
        assertEquals(HbTokenTypes.WHITE_SPACE, tokens.get(3).getElementType());
        assertEquals(" ", tokens.get(3).getElementContent());
        assertEquals(HbTokenTypes.CLOSE, tokens.get(4).getElementType());
        assertEquals("}}", tokens.get(4).getElementContent());
    }

    public void testComment() {
        List<Token> tokens = lex("{{! this is a comment=true }}");

        assertEquals(1, tokens.size());
        assertEquals(HbTokenTypes.COMMENT, tokens.get(0).getElementType());
        assertEquals("{{! this is a comment=true }}", tokens.get(0).getElementContent());
    }
    
    public void testContentAfterComment() {
        List<Token> tokens = lex("{{! this is a comment=true }}This here be non-Hb content!");

        int tokenIdx = -1;
        assertEquals(HbTokenTypes.COMMENT, tokens.get(++tokenIdx).getElementType());
        assertEquals("{{! this is a comment=true }}", tokens.get(tokenIdx).getElementContent());
        assertEquals(HbTokenTypes.CONTENT, tokens.get(++tokenIdx).getElementType());
        assertEquals("This here be non-Hb content!", tokens.get(tokenIdx).getElementContent());
    }

    public void testSquareBracketStuff() {
        List<Token> tokens = lex("{{test\t[what] }}");

        int tokenIdx = -1;
        assertEquals(HbTokenTypes.OPEN, tokens.get(++tokenIdx).getElementType());
        assertEquals("{{", tokens.get(tokenIdx).getElementContent());
        assertEquals(HbTokenTypes.ID, tokens.get(++tokenIdx).getElementType());
        assertEquals("test", tokens.get(tokenIdx).getElementContent());
        assertEquals(HbTokenTypes.WHITE_SPACE, tokens.get(++tokenIdx).getElementType());
        assertEquals("\t", tokens.get(tokenIdx).getElementContent());
        assertEquals(HbTokenTypes.ID, tokens.get(++tokenIdx).getElementType());
        assertEquals("[what]", tokens.get(tokenIdx).getElementContent());
        assertEquals(HbTokenTypes.WHITE_SPACE, tokens.get(++tokenIdx).getElementType());
        assertEquals(" ", tokens.get(tokenIdx).getElementContent());
        assertEquals(HbTokenTypes.CLOSE, tokens.get(++tokenIdx).getElementType());
        assertEquals("}}", tokens.get(tokenIdx).getElementContent());
    }

    public void testSeparator() {
        List<Token> tokens = lex("{{dis/connected}}");

        int tokenIdx = -1;
        assertEquals(HbTokenTypes.OPEN, tokens.get(++tokenIdx).getElementType());
        assertEquals("{{", tokens.get(tokenIdx).getElementContent());
        assertEquals(HbTokenTypes.ID, tokens.get(++tokenIdx).getElementType());
        assertEquals("dis", tokens.get(tokenIdx).getElementContent());
        assertEquals(HbTokenTypes.SEP, tokens.get(++tokenIdx).getElementType());
        assertEquals("/", tokens.get(tokenIdx).getElementContent());
        assertEquals(HbTokenTypes.ID, tokens.get(++tokenIdx).getElementType());
        assertEquals("connected", tokens.get(tokenIdx).getElementContent());
        assertEquals(HbTokenTypes.CLOSE, tokens.get(++tokenIdx).getElementType());
        assertEquals("}}", tokens.get(tokenIdx).getElementContent());
    }

    private static class Token {
        private IElementType _elementType;
        private String _elementContent;

        private Token(IElementType elementType, String elementContent) {
            _elementType = elementType;
            _elementContent = elementContent;
        }

        public IElementType getElementType() {
            return _elementType;
        }

        public String getElementContent() {
            return _elementContent;
        }
    }
}
