import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeDiagnosingMatcher
import java.util.*

internal class OptionalIsPresent<T>(val matcher: Matcher<T>? = null) : TypeSafeDiagnosingMatcher<Optional<T>>() {

    override fun describeTo(description: Description?) {
        if (matcher == null) {
            description?.appendText("an optional with a value")
        } else {
            description?.appendText("an optional with a value matching ")?.appendDescriptionOf(matcher)
        }
    }

    override fun matchesSafely(item: Optional<T>, mismatchDescription: Description): Boolean {
        if (item.isPresent) {
            if(matcher == null) {
                return true
            } else {
                val matched = matcher.matches(item.get())

                if(matched) {
                    return true
                }

                mismatchDescription
                    .appendText("expected value matching ")
                    .appendDescriptionOf(matcher)
            }
        }

        mismatchDescription.appendText("expected value")
        return false

    }
}
