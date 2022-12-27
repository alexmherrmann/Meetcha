import com.alexmherrmann.Meetcha.Meetcha.optionalIsPresent
import com.alexmherrmann.Meetcha.Meetcha.optionalMatches
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.*
import org.hamcrest.StringDescription
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.*
import java.util.stream.Stream


class OptionalTest {
	class PresentArgs : ArgumentsProvider {
		override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
			listOf<ExpectedMatch<*>>(
				ExpectedMatch(
					optionalIsPresent(),
					Optional.of("yep"),
					true
				),
				ExpectedMatch(
					optionalMatches(equalTo("yep")),
					Optional.of("yep"),
					true
				),

				ExpectedMatch(
					optionalIsPresent(),
					Optional.empty<Int>(),
					false, "expected value"
				),
				ExpectedMatch(
					not(optionalIsPresent()),
					Optional.empty<Int>(),
					true
				),
			).stream()

	}

	@ParameterizedTest
	@ArgumentsSource(PresentArgs::class)
	fun isPresent(test: ExpectedMatch<Optional<String>>) {
		MatcherAssert.assertThat(test.matcher.matches(test.fixture), equalTo(test.expected))

		if (!test.expected) {
			// Do we expect a description?
			if (test.expectedDescription != null) {
				val description = StringDescription()
				test.matcher.describeMismatch(test.fixture, description)

				MatcherAssert.assertThat(description.toString(), test.expectedDescription)
			}
		}

	}
}