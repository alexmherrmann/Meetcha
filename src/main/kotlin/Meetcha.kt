import org.hamcrest.Matcher
import java.util.*

object Meetcha {

    /**
     * Ensure the optional has a value
     */
    fun <T> optionalIsPresent(): Matcher<Optional<T>> = OptionalIsPresent()

    /**
     * Ensure the optional has a value and that value matches something
     */
    fun <T> optionalMatches(matcher: Matcher<T>): Matcher<Optional<T>> = OptionalIsPresent(matcher)
}