package com.dmarcotte.handlebars.parsing;

import com.dmarcotte.handlebars.config.PropertiesComponentStub;
import com.dmarcotte.handlebars.util.HbTestUtils;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.ide.util.PropertiesComponentImpl;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.testFramework.ParsingTestCase;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusFactory;
import org.picocontainer.MutablePicoContainer;

/**
 * ParsingTestCase test are created by placing a MyTestName.hbs file in the test/data/parsing directory with the syntax
 * you would like to validate, and a MyTestName.txt file representing the expected Psi structure.
 *
 * You then create a test of the following form to validate the parser (note how the test name corresponds
 * to the name of the .hbs and .txt files above):
 *
 *<pre>{@code
 *public void testMyTestName() { doTest(true); }
 *}</pre>
 *
 * <b>TIP:</b> if you create a .hbs file without a .txt file, the test will autogenerate .txt file from the current
 * parser. So, in practice when creating parser tests, you create the .hbs file, run the test, validate that
 * the .txt represents the desired Psi structure, then call it a day.
 */
public abstract class HbParserTest extends ParsingTestCase {

    @SuppressWarnings("UnusedDeclaration") // TODO odd... StdFileTypes is not initialized on time if it's not forward declared.  Figure out what's up and remove this hack.
    private static LanguageFileType html = StdFileTypes.HTML;

    public HbParserTest() {
        super("parser", "hbs", new HbParseDefinition());
    }

    @Override
    protected String getTestDataPath() {
        return HbTestUtils.BASE_TEST_DATA_PATH;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        final MutablePicoContainer appContainer = getApplication().getPicoContainer();
        appContainer.registerComponentInstance(PropertiesComponent.class.getName(),
                new PropertiesComponentStub());
    }
}
