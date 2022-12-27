package com.alexmherrmann.Meetcha

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeDiagnosingMatcher

class OnlyOne<T>(val matcher: Matcher<T>) : TypeSafeDiagnosingMatcher<Iterable<T>>() {

	override fun matchesSafely(item: Iterable<T>?, mismatchDescription: Description?): Boolean {
		var matches = 0
		for (current in item ?: emptyList()) {
			if (matcher.matches(current)) {
				matches++
			}
		}

		when {
			item == null -> mismatchDescription?.appendText("was null!")
			matches > 1 -> mismatchDescription?.appendText("$matches items instead of one for ")
				?.appendDescriptionOf(matcher)

			matches == 1 -> mismatchDescription?.appendText("1 good match for ")?.appendDescriptionOf(matcher)
			matches == 0 -> mismatchDescription?.appendText("no items for ")?.appendDescriptionOf(matcher)
			else -> throw IllegalStateException("Less than 0 matches 😱")
		}

		return matches == 1
	}

	override fun describeTo(description: Description?) {
		description?.appendText("An iterable containing only one item matching ")?.appendDescriptionOf(matcher)
	}

}
