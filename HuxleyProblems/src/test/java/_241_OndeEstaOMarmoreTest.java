import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.Random;

public class _241_OndeEstaOMarmoreTest {

	private static final String basePath = "./src/test/resources/_241_OndeEstaOMarmore/";
	private static ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	private static String[] in, out;
	private static NelsonOracle oracle;
	private static final int seed = 343;
	private static Random random = new Random();

	@BeforeAll
	static void setupAll() throws IOException, InterruptedException {
		oracle = new NelsonOracle(basePath + "_241_OndeEstaOMarmore.cpp");
		random.setSeed(seed);
		in = new File(basePath + "in/").list();
		out = new File(basePath + "out/").list();
		Arrays.sort(in); Arrays.sort(out);
		System.setOut(new PrintStream(outputStream));
	}

	@BeforeEach
	void resetOutput() { outputStream.reset(); }

	@RepeatedTest(3)
	void repeatedTest(RepetitionInfo repetitionInfo) throws IOException {
		int i = repetitionInfo.getCurrentRepetition() - 1;
		String expected = InOutReader.getStringFromFile(basePath + "out/" + out[i]);
		System.setIn(new FileInputStream(basePath + "in/" + in[i]));
		final String myAnswer = assertTimeoutPreemptively(Duration.ofMillis(6000), () -> {
			_241_OndeEstaOMarmore.HuxleyCode.main(null);
			return(InOutReader.uniformString(outputStream.toString()));
		});
		assertEquals(expected, myAnswer, "Failing " + in[i] + " test case");
	}

	private void generateInput() throws IOException {
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(NelsonOracle.in));
		int limit = (int) 1e4;
		Integer testCases = random.nextInt(2) + 1;
		for (int t = 0; t < testCases; t ++) {

			Integer n = random.nextInt(limit - 1) + 1, q = random.nextInt(limit - 1) + 1;
			bufferedWriter.write(n.toString() + " " + q.toString() + "\n");
			for (int i = 0; i < q + n; i ++)
				bufferedWriter.write(Integer.toString(random.nextInt(limit)) + "\n");
		}
		bufferedWriter.write("0 0\n");
		bufferedWriter.close();
	}

	@RepeatedTest(10)
	void randomTest(RepetitionInfo repetitionInfo) throws IOException, InterruptedException {

		generateInput();
		final String oracleAnswer = oracle.getAnswer();

		System.setIn(new FileInputStream(NelsonOracle.in));
		final String myAnswer = assertTimeoutPreemptively(Duration.ofMillis(2000), () -> {
			_241_OndeEstaOMarmore.HuxleyCode.main(null);
			return(InOutReader.uniformString(outputStream.toString()));
		});

		String input = InOutReader.getStringFromFile(NelsonOracle.in);

		assertEquals(oracleAnswer, myAnswer, "Failed test case " + repetitionInfo.getCurrentRepetition() + " of input");
	}
}
