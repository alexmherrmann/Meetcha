package com.alexmherrmann.Meetcha

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.StringDescription
import org.hamcrest.TypeSafeDiagnosingMatcher

internal class Mapping<ORIGINAL, MAPPED>(
	val transform: (ORIGINAL) -> MAPPED,
	val matcher: Matcher<Iterable<MAPPED>>
) : TypeSafeDiagnosingMatcher<Iterable<ORIGINAL>>() {

	override fun describeTo(description: Description?) {
		description?.appendText("after mapping, matches ")?.appendDescriptionOf(matcher)
	}

	override fun matchesSafely(item: Iterable<ORIGINAL>, mismatchDescription: Description?): Boolean {
		val mapped = item.map(transform)
		val stringDescription = StringDescription()

		if (matcher.matches(mapped)) {
			return true
		}

		matcher.describeMismatch(mapped, stringDescription)
		mismatchDescription
			?.appendText("after mapping, did not match ")
			?.appendText(stringDescription.toString())

		return false
	}
}

