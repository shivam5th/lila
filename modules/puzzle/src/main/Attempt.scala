package lila.puzzle

import org.joda.time.DateTime

case class Attempt(
    id: String, // userId/puzzleId
    puzzleId: PuzzleId,
    userId: String,
    date: DateTime,
    win: Boolean,
    hints: Int,
    retries: Int,
    time: Int, // seconds
    puzzleRating: Int,
    puzzleRatingDiff: Int,
    userRating: Int,
    userRatingDiff: Int,
    vote: Option[Boolean]) {

  def userPostRating = userRating + userRatingDiff

  def puzzlePostRating = puzzleRating + puzzleRatingDiff
}

object Attempt {

  def makeId(puzzleId: PuzzleId, userId: String) = s"$puzzleId/$userId"

  object BSONFields {
    val id = "_id"
    val puzzleId = "p"
    val userId = "u"
    val date = "d"
    val win = "w"
    val hints = "h"
    val retries = "r"
    val time = "t"
    val puzzleRating = "pr"
    val puzzleRatingDiff = "pd"
    val userRating = "ur"
    val userRatingDiff = "ud"
    val vote = "v"
  }

  import reactivemongo.bson._
  import lila.db.BSON
  import BSON.BSONJodaDateTimeHandler
  implicit val attemptBSONHandler = new BSON[Attempt] {

    import BSONFields._

    def reads(r: BSON.Reader): Attempt = Attempt(
      id = r str id,
      puzzleId = r int puzzleId,
      userId = r str userId,
      date = r.get[DateTime](date),
      win = r bool win,
      hints = r intD hints,
      retries = r intD retries,
      time = r int time,
      puzzleRating = r int puzzleRating,
      puzzleRatingDiff = r int puzzleRatingDiff,
      userRating = r int userRating,
      userRatingDiff = r int userRatingDiff,
      vote = r boolO vote)

    def writes(w: BSON.Writer, o: Attempt) = BSONDocument(
      id -> o.id,
      puzzleId -> o.puzzleId,
      userId -> o.userId,
      date -> o.date,
      win -> o.win,
      hints -> w.intO(o.hints),
      retries -> w.intO(o.retries),
      time -> w.int(o.time),
      puzzleRating -> w.int(o.puzzleRating),
      puzzleRatingDiff -> w.int(o.puzzleRatingDiff),
      userRating -> w.int(o.userRating),
      userRatingDiff -> w.int(o.userRatingDiff),
      vote -> o.vote)
  }
}
