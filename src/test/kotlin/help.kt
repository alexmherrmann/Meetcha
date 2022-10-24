import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.StringDescription
import org.junit.jupiter.api.Named
import org.junit.jupiter.params.provider.Arguments

/**
 * A class to represent some matches
 */
class ExpectedMatch<T> constructor (
    val matcher: Matcher<T>,
    val fixture: T,
    val expected: Boolean,
    val expectedDescription: Matcher<String>? = null
): Arguments, Named<ExpectedMatch<T>> {

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
    ): this(matcher, fixture, expected, Matchers.equalTo(expectedDescription))

    /**
     * When there's no mismatch, no description is expected
     */
    constructor(
        matcher: Matcher<T>,
        fixture: T,
        expected: Boolean,
    ): this(matcher, fixture, expected, Matchers.emptyOrNullString())

    override fun get(): Array<Any> = arrayOf(this)
}