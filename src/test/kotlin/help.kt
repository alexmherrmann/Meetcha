import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.hamcrest.StringDescription
import org.junit.jupiter.api.Named
import org.junit.jupiter.params.provider.Arguments
import kotlin.collections.Collection

/**
 * A class to represent some matches
 */
class ExpectedMatch<T> constructor(
	val matcher: Matcher<T>,
	val fixture: T,
	val expected: Boolean,
	val expectedDescription: Matcher<String>? = null
) : Arguments, Named<ExpectedMatch<T>> {

	override fun toString(): String {
		val description = StringDescription().run {
			matcher.describeTo(this)
			this.toString()
		}

		val expectedMismatch = expectedDescription?.let {
			StringDescription().let { str ->
				it.describeMismatch(fixture, str)
				" $str"
			}
		}

		return "$fixture: $description is ${expected}${expectedMismatch ?: ""}"
	}

	override fun getName(): String = toString()

	override fun getPayload(): ExpectedMatch<T> = this

	/**
	 * When you're guaranteed a string
	 */
	constructor(
		matcher: Matcher<T>,
		fixture: T,
		expected: Boolean,
		expectedDescription: String
	) : this(matcher, fixture, expected, Matchers.equalTo(expectedDescription))

	/**
	 * When there's no mismatch, no description is expected
	 */
	constructor(
		matcher: Matcher<T>,
		fixture: T,
		expected: Boolean,
	) : this(matcher, fixture, expected, Matchers.emptyOrNullString())

	override fun get(): Array<Any> = arrayOf(this)
}

object MatcherTester {
	/**
	 * Does this matcher match the input
	 */
	fun <T> matches(input: T, matches: Matcher<T>): Unit = MatcherAssert.assertThat(input, matches)

	/**
	 * Ensure input DOESN'T match and that the mismatch description is what we expect
	 */
	fun <T> doesntMatch(input: T, notMatches: Matcher<T>, expectThisMismatch: Matcher<String>) {
		if (notMatches.matches(input)) {
			val description = StringDescription().let {
				notMatches.describeTo(it)
				it.toString()
			}
			throw AssertionError("matcher:\n${description}\nshould not match input:\n${input.toString()}")
		} else {
			val mismatch = StringDescription().let {
				notMatches.describeMismatch(input, it)
				it.toString()
			}

			MatcherAssert.assertThat(mismatch, expectThisMismatch)
		}
	}

	/**
	 * Ensure input DOESN'T match and that the mismatch description is what we expect
	 */
	fun <T> doesntMatch(input: T, notMatches: Matcher<T>, expectMismatch: String) =
		MatcherTester.doesntMatch(input, notMatches, Matchers.equalTo(expectMismatch))
}

/**
 * Get a list of matchers that are just equalTo(ITEMS)
 */
val <T> Iterable<T>.eqs: Collection<Matcher<T>>
	get() = this.map { Matchers.equalTo(it) }

val <T> T.eq: Matcher<T>
	get() = Matchers.equalTo(this)
