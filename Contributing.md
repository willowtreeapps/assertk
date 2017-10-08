# Contributing

## What to Contribute

Want to contribute, but not sure how? Here are some ideas to get you started.

1. **Write unit tests.** Right now not all assertions are under test. The
   easiest thing to do is to take one of them and write a few tests.
   `src/test/kotlin/test/assertk` houses all of the current unit tests. Unit
   tests are run with [Spek](http://spekframework.org/) and assertions are done
   with [AssertJ](http://joel-costigliola.github.io/assertj/).
2. **Add a new assertion.** If you have an idea for a new assertion that's
   general enough, you can add it. Assertions are grouped under their type in
   `src/main/kotlin/assertk/assertions/`. Make sure you write unit test and docs 
   for them.
3. **Improve a failure message.** Some failure messages can be better. Create a
   PR if you have something better. Feel free to first create an issue if you
   want feedback or a discussion on better wording.  Unless there is a good
   reason, failure messages should be in the form `"expeted:${show(foo)} but
   was:${show(bar)}"`.
4. **Suggest a new feature.** Have a new cool feature idea or want to work on
   one that's already suggested? First create/comment on an issue to get
   feedback and figure out a good design. We will then assign you on the issue.
   Then hack away and create a pull request.

## Creating the Pull Request

To contribute, fork our project on GitHub, then submit a pull request to our
`master` branch.

## Before Submitting

1. Make sure you unit test your changes.
2. If you added new assertions, please be sure to add them to the main project.
3. If you update anything that would add breaking changes to older versions of
   assertk be sure to declare that in your Pull Request description.
4. Be sure to document your code. This includes adding in kotlin doc style
   comments for those methods that are publicly accessible and also adding
   those methods to their respective `README.md` document. Any consumer-facing
   changes should be added to the [Unreleased] section of the `CHANGELOG.md`.

---

By submitting a pull request, you represent that you have the right to license
your contribution to WillowTree and the community, and agree by submitting the
patch that your contributions are licensed under the [MIT License](LICENSE).
