import com.alexmherrmann.Meetcha.Meetcha.mappingMatcher
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

class MappingTest {
	class PresentArgs : ArgumentsProvider {
		override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
			listOf<ExpectedMatch<*>>(
				ExpectedMatch(
					mappingMatcher(everyItem(greaterThan(5))) { it + 5 },
					listOf(1, 2, 3, 4),
					true
				),
				ExpectedMatch(
					mappingMatcher(
						everyItem(
							isA(String::class.java)
						)
					) { "$it" },
					listOf(1, 2, 3, 4),
					true
				),
				ExpectedMatch(
					mappingMatcher(
						everyItem(greaterThan(0))
					) { -it },
					listOf(-1, 0, 1),
					false,
					startsWith("after mapping, ")
				)
			).stream()

	}

	@ParameterizedTest
	@ArgumentsSource(PresentArgs::class)
	fun simple(test: ExpectedMatch<Optional<String>>) {
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