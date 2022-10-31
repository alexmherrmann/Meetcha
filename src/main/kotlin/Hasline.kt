import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeDiagnosingMatcher

//class Hasline(val matcher: Matcher<String>): TypeSafeDiagnosingMatcher<String>() {
//    override fun describeTo(description: Description?) {
//        TODO("Not yet implemented")
//    }
//
//    override fun matchesSafely(item: String, mismatchDescription: Description): Boolean {
//        val firstMismatch = item.lines().firstOrNull { matcher.matches(item) }
//    }
//}