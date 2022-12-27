package com.alexmherrmann.Meetcha

import Descriptions
import description
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeDiagnosingMatcher

/**
 *
 * @param only ONE matcher must match ONE element in the list, means the matchers must have the same size as the item being matched
 * @param inOrder The matchers must match in order
 */
internal class BetterListContains<E>(
	val matchers: Collection<Matcher<out E>>,
	val only: Boolean = true,
	val inOrder: Boolean = false,
) : TypeSafeDiagnosingMatcher<Collection<E>>() {
	override fun describeTo(description: Description) {
		val inOrderDescription = if (inOrder) " in order" else ""
		val onlyDescription = if (only) " ONLY" else ""
		description.appendText("A collection containing$onlyDescription$inOrderDescription: ")
			.appendList("[", ",", "]", matchers)
	}


	override fun matchesSafely(item: Collection<E>?, mismatchDescription: Description): Boolean {
		if (item == null) {
			mismatchDescription.appendText("passed collection was null")
			return false
		}

		if (only) {
			if (item.size != matchers.size) {
				mismatchDescription.appendText("expected size ${matchers.size} but got ${item.size}")
				return false;
			}

			if (inOrder) {
				for ((index, pair) in matchers.zip(item).withIndex()) {
					val (matcher, real) = pair
					if (matcher.matches(real)) {
						continue
					} else {
						mismatchDescription.appendText("item $index did not match ")

						matcher.describeMismatch(real, mismatchDescription)
						return false
					}
				}
				return true;
			} else {
				// Find all matchers that are mapped
				val matched = matchers.map { matcher ->
					matcher to item.any(matcher::matches)
				}
				val firstBad = matched.firstOrNull { !it.second }
				if (firstBad != null) {
					mismatchDescription.appendText("matcher failed: ").appendDescriptionOf(firstBad.first)
					return false
				}

				return true;

			}
		} else {
			if (inOrder) {
				var last: IndexedValue<Matcher<*>>? = null
				var lastFound: IndexedValue<E>? = null
				for (pair in matchers.withIndex()) {
					val (matcherIndex, matcher) = pair
					val firstFound = item.withIndex().first { matcher.matches(it.value) }

					// Is it AFTER or the SAME as the last found match
					if (firstFound.index < (lastFound?.index ?: -1)) {
						mismatchDescription
							.appendText("matcher (${matcherIndex}: ${matcher.description}) is out of order at (${firstFound.index}: ${firstFound.value}); it was found after matcher (${last?.index}: ${last?.value?.description}) matched (${lastFound?.index}: ${lastFound?.value})")

						return false;
					}

					last = pair
					lastFound = firstFound
				}

				return true
			} else {
				val firstBroken = matchers
					.withIndex()
					.firstOrNull { (_, m) ->
						item.firstOrNull { m.matches(it) } == null
					}

				if (firstBroken != null) {
					mismatchDescription
						.appendText("matcher ${firstBroken.index + 1} couldn't find a match: ")
						.appendDescriptionOf(firstBroken.value)
						.appendText(" in: [${item.joinToString(",")}]")

					return false;
				}

				return true
			}
		}

	}

}