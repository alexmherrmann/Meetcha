import com.alexmherrmann.Meetcha.Meetcha
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Collection {

	@Nested
	inner class OnlyNot_InOrder {
		@Test
		fun doesMatch() = MatcherTester.matches(
			listOf(1, 2, 3, 4),
			Meetcha.betterContainsOnly(listOf(1, 2, 3, 4).eqs),
		)

		@Test
		fun doesntMatchMoreSmaller() = MatcherTester.doesntMatch(
			listOf(1, 2, 3),
			Meetcha.betterContainsOnly(listOf(1, 2, 3, 4).eqs),
			"expected size 4 but got 3"
		)

		@Test
		fun doesntMatchMoreBigger() = MatcherTester.doesntMatch(
			listOf(1, 2, 3, 4, 5),
			Meetcha.betterContainsOnly(listOf(1, 2, 3, 4).eqs),
			"expected size 4 but got 5"
		)
	}

	@Nested
	inner class NotOnly_InOrder {
		@Test
		fun doesMatch() = MatcherTester.matches(
			listOf(1, 2, 3),
			Meetcha.betterContainsInOrder(listOf(1, 2, 3).eqs)
		)

		@Test
		fun outOfOrder() = MatcherTester.doesntMatch(
			listOf(1,2,3),
			Meetcha.betterContainsInOrder(listOf(1,3,2).eqs),
			"matcher (2: <2>) is out of order at (1: 2); it was found after matcher (1: <3>) matched (2: 3)"
		)
	}

	@Nested
	inner class NotOnly_NotInOrder {
		@Test
		fun doesMatch() = MatcherTester.matches(
			listOf(1, 2, 3, 4),
			Meetcha.betterContains(listOf(1, 2, 3, 4).eqs),
		)

		@Test
		fun doesMatchSubset() {
			MatcherTester.matches(
				listOf(1, 2, 3, 4, 5),
				Meetcha.betterContains(listOf(1, 2, 3).eqs)
			)

			MatcherTester.matches(
				listOf(1, 3, 4, 5, 2),
				Meetcha.betterContains(listOf(2, 1, 3).eqs)
			)
		}

		@Test
		fun doesntMatchMissing() = MatcherTester.doesntMatch(
			listOf(1, 2, 3),
			Meetcha.betterContains(listOf(1, 2, 3, 4).eqs),
			"matcher 4 couldn't find a match: ${4.eq.description} in: [1,2,3]"
		)
	}
}