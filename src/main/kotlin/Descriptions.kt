import org.hamcrest.Matcher
import org.hamcrest.StringDescription

object Descriptions {

}


/**
 * Get the description for a matcher
 */
val Matcher<*>.description
	get() = StringDescription().let {
		this.describeTo(it)
		it.toString()
	}