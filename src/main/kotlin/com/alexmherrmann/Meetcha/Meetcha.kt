package com.alexmherrmann.Meetcha

import org.hamcrest.Matcher
import java.util.*

/**
 * Contains the helpers
 */
object Meetcha {

	/**
	 * Ensure the optional has a value
	 */
	fun <T> optionalIsPresent(): Matcher<Optional<T>> = OptionalIsPresent()

	/**
	 * Ensure the optional has a value and that value matches something
	 */
	fun <T> optionalMatches(matcher: Matcher<T>): Matcher<Optional<T>> = OptionalIsPresent(matcher)

	/**
	 * Map an iterable from one thing to another and then test each
	 */
	fun <ORIGINAL, MAPPED> mappingMatcher(
		toCheck: Matcher<Iterable<MAPPED>>,
		transform: (ORIGINAL) -> MAPPED
	): Matcher<Iterable<ORIGINAL>> =
		Mapping(transform, toCheck)

	/**
	 * For the iterable, ensure that only ONE item in the list matches
	 */
	fun <T> onlyOne(matcher: Matcher<T>): Matcher<Iterable<T>> = OnlyOne(matcher)

	/**
	 * All items in the list match what was provided, AND is in order
	 */
	fun<T> betterContainsInOrder(matchers: Collection<Matcher<T>>): Matcher<Collection<T>> =
		BetterListContains(matchers, false, true)

	/**
	 * All items provided must exist in the list in any order
	 */
	fun <T> betterContains(matchers: Collection<Matcher<T>>): Matcher<Collection<T>> =
		BetterListContains(matchers, false, false)

	/**
	 * Does the list contain ONLY the following matchers?
	 */
	fun <T> betterContainsOnly(vararg matcher: Matcher<T>): Matcher<Collection<T>> =
		betterContainsOnly(matcher.toList())

	/**
	 * Does the list contains ONLY the following matchers
	 */
	fun <T> betterContainsOnly(matchers: Collection<Matcher<T>>): Matcher<Collection<T>> =
		BetterListContains(
			matchers,
			true,
			false
		)

}