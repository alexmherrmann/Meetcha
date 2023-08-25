# Meetcha

Meetcha are some hamcrest matchers I felt were missing when writing unit tests.

It's got matchers for...:

* Collections
  * Includes a very configurable matcher for testing in order, etc.
  Works on general collections
  * A Mapping collection matcher. It transforms a collection and then matches on it.
  I was tired of having to .stream().map(...).collect(...) before matching on a collection.
  * An "only one" matcher. Checks that one and only one item in a list matches a given matcher.
* Optional
  * Present and value checks

## Testing
81% coverage!