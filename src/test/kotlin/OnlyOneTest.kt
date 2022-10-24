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

class OnlyOneTest {
    class PresentArgs : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
            listOf<ExpectedMatch<*>>(
                ExpectedMatch(
                    onlyOne(equalTo(4)),
                    listOf(1,2,3,4),
                    true
                ),
                ExpectedMatch(
                    onlyOne(equalTo("yep")),
                    listOf("yep", "nope", "nope"),
                    true
                ),

                ExpectedMatch(
                    onlyOne(equalTo(1)),
                    listOf(1,1,2),
                    false, "2 items instead of one for <1>"
                ),
                ExpectedMatch(
                    onlyOne(lessThan(2)),
                    listOf(1,1,2),
                    false, "2 items instead of one for a value less than <2>"
                ),
                ExpectedMatch(
                    onlyOne(greaterThan(2)),
                    listOf(1,1,2),
                    false,
                    "no items for a value greater than <2>"
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