package potatoe.code;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CodeAssemblerTest {

    private CodeAssembler classUnderTest;

    @Mock
    private File fileMock;

    @Before
    public void setUp() {
        classUnderTest = new CodeAssembler(fileMock);
    }

    private void checkLineOutput(final byte[] actual, final byte[] expectedBytes) {
        assertThat(actual.length, is(expectedBytes.length));
        for (int i=0; i<actual.length; i++) {
            assertThat(actual[i], is(expectedBytes[i]));
        }
    }

    @Test
    public void testAssembleMoveWithImm8() {
        final CodeAssemblerOutput output = classUnderTest.assembleLine("MOV areg,255");
        assertThat(output.getErrors().isEmpty(), is(true));
        assertThat(output.getWarnings().isEmpty(), is(true));

        final byte[] expectedBytes = new byte[] { 0b00000000, (byte) 0b11111111 };
        checkLineOutput(output.getAssembledBytes(), expectedBytes);
    }

    @Test
    public void testAssembleMoveWithBiggerImm8() {
        final CodeAssemblerOutput output = classUnderTest.assembleLine("MOV areg,256");
        assertThat(output.getErrors().isEmpty(), is(true));
        assertThat(output.getWarnings().isEmpty(), is(false));
        assertThat(output.getWarnings().get(0).getMessage(), equalTo("Value bigger than one byte. Cropped to fit: 256 -> 0"));

        final byte[] expectedBytes = new byte[] { 0b00000000, (byte) 0b00000000 };
        checkLineOutput(output.getAssembledBytes(), expectedBytes);
    }

}
