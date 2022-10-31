package collection

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeDiagnosingMatcher

/**
 * @param only ONE matcher must match ONE element in the list, means the matchers must have the same size as the item being matched
 * @param inOrder The matchers must match in order, ignored if only is not set
 */
internal class BetterListContains<E>(
    val matchers: Collection<Matcher<E>>,
    val only: Boolean = true,
    val inOrder: Boolean = false,
) : TypeSafeDiagnosingMatcher<Collection<E>>() {
    override fun describeTo(description: Description) {
        val inOrderDescription = if (inOrder) " in order" else ""
        val onlyDescription = if (only) " ONLY" else ""
        description
            .appendText("A collection containing$onlyDescription: ")
            .appendList("[", ",", "]", matchers)
    }


    override fun matchesSafely(item: Collection<E>?, mismatchDescription: Description): Boolean {
        if(item == null) {
            mismatchDescription.appendText("passed collection was null")
            return false
        }

        if(only) {
            if(item.size != matchers.size) {
                mismatchDescription.appendText("expected size ${matchers.size} but got ${item.size}")
                return false;
            }

            if(inOrder) {
                for((index, pair) in matchers.zip(item).withIndex()) {
                    val (matcher, real) = pair
                    if(matcher.matches(real)) {
                        continue
                    } else {
                        mismatchDescription
                            .appendText("item $index did not match ")

                        matcher.describeMismatch(real, mismatchDescription)
                        return false
                    }
                }
                return true;
            } else {
                val matched = matchers.map { matcher ->
                    matcher to item.any(matcher::matches)
                }
                val firstBad = matched.firstOrNull { it.second }
                if(firstBad != null) {
                    mismatchDescription
                        .appendText("matcher failed: ")
                        .appendDescriptionOf(firstBad.first)
                    return false
                }

                return true;

            }
        } else {
            TODO("Not implemented yet")
        }

    }

}