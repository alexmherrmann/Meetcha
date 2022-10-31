import collection.BetterListContains
import collection.mappingMatcher
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.*
import org.hamcrest.MatcherAssert.*
import org.hamcrest.StringDescription
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.*
import java.util.stream.Stream

class Collection {
    class PresentArgs : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
            listOf<ExpectedMatch<*>>(


                run {
                    val good = listOf(1, 2, 3, 4)
                    ExpectedMatch(
                        BetterListContains(good.map { equalTo(it) }),
                        good,
                        true
                    )
                },

                run {
                    val diffSize = listOf(1, 2, 3, 4)
                    ExpectedMatch(
                        BetterListContains(diffSize.map { equalTo(it) }.subList(0, 3)),
                        diffSize,
                        false,
                        "expected size 3 but got 4"
                    )
                }

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