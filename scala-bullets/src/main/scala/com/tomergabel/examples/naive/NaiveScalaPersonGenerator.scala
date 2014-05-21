package com.tomergabel.examples.naive

import scala.collection.mutable
import scala.util.Random


/**
 * Created by tomer on 5/20/14.
 *
 * Naive reimplementation in Scala, first step
 */
object NaiveScalaPersonGenerator {

  private val names: List[ String ] = List( "Jeffrey", "Walter", "Donald" )
  private val surnames: List[ String ] = List( "Lebowsky", "Sobchak", "Kerabatsos" )
  private val random: Random = new Random
  val MINIMUM_HUMAN_AGE: Int = 0
  val MAXIMUM_HUMAN_AGE: Int = 125
  val MINIMUM_TODDLER_AGE: Int = 1
  val MAXIMUM_TODDLER_AGE: Int = 3

  def generatePerson( minAge: Int, maxAge: Int ): Person = {
    val name: String = names( random.nextInt( names.size ) )
    val surname: String = surnames( random.nextInt( surnames.size ) )
    val age: Int = random.nextInt( maxAge - minAge + 1 ) + minAge
    return new Person( name, surname, age )
  }

  def generatePerson: Person = {
    return generatePerson( MINIMUM_HUMAN_AGE, MAXIMUM_HUMAN_AGE )
  }

  def generatePeople( count: Int, minAge: Int, maxAge: Int ): List[ Person ] = {
    val people: mutable.ListBuffer[ Person ] = mutable.ListBuffer.empty
    for ( i <- 0 until count ) {
      people.append( generatePerson( minAge, maxAge ) )
    }
    return people.result()
  }

  def generatePeople( count: Int ): List[ Person ] = {
    return generatePeople( count, MINIMUM_HUMAN_AGE, MAXIMUM_HUMAN_AGE )
  }

  def generateToddler: Person = {
    return generatePerson( MINIMUM_TODDLER_AGE, MAXIMUM_TODDLER_AGE )
  }

  def generateToddlers( count: Int ): List[ Person ] = {
    return generatePeople( count, MINIMUM_TODDLER_AGE, MAXIMUM_TODDLER_AGE )
  }

  def main( args: Array[ String ] ) {
    println( "Five people:" )
    for ( person <- generatePeople( 5 ) )
      println( person.toString )
    println( "Two toddlers:" )
    for ( toddler <- generateToddlers( 2 ) )
      println( toddler.toString )
  }
}