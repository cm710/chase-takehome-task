# Notes

I have made several improvements to the code, specifically making the relationship between showings, movies and theatres safer so that the the compile time routine and syntax will not allow a showing to belong to an invalid theater, or a movie to belong to an invalid showing. As a result, getting the show sequence is done in a safer maner and so is the discount calculation.

I have discarded the `LocalDateProvider` singleton as its only function was to call the static `DateTime.now()` method. It may be useful if extended, but right now it is not - as a result I have excluded it from use.

I have added relevant tests to the `Movie`, `Reservation` and `Theater` files, so that I can cover elements that have interactability between the classes - e.g. making discounts references information from the theater, showing and movie. This was the basis of deciding which tests to add. A possible improvement would be to install a fuzzer test framework and actively run various functions with random arguments, to test their reliability - however, I could not find a standard library that was easy to implement here.

Lastly, I have made several improvements to handling invalid arguments for various methods.

# ORIGINAL INSTRUCTIONS
# Introduction

This is a poorly written application, and we're expecting the candidate to greatly improve this code base.

## Instructions
* **Consider this to be your project! Feel free to make any changes**
* There are several deliberate design, code quality and test issues in the current code, they should be identified and resolved
* Implement the "New Requirements" below
* Keep it mind that code quality is very important
* Focus on testing, and feel free to bring in any testing strategies/frameworks you'd like to implement
* You're welcome to spend as much time as you like, however, we're expecting that this should take no more than 2 hours

## `movie-theater`

### Current Features
* Customer can make a reservation for the movie
  * And, system can calculate the ticket fee for customer's reservation
* Theater have a following discount rules
  * 20% discount for the special movie
  * $3 discount for the movie showing 1st of the day
  * $2 discount for the movie showing 2nd of the day
* System can display movie schedule with simple text format

## New Requirements
* New discount rules; In addition to current rules
  * Any movies showing starting between 11AM ~ 4pm, you'll get 25% discount
  * Any movies showing on 7th, you'll get 1$ discount
  * The discount amount applied only one if met multiple rules; biggest amount one
* We want to print the movie schedule with simple text & json format