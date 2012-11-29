package lila
package ai
package stockfish

import chess.{ Game, Move }
import game.DbGame
import analyse.Analysis

import scalaz.effects._
import scala.concurrent.Future
import play.api.Play.current
import play.api.libs.concurrent._
import play.api.libs.concurrent.execution.Implicits._

final class Ai(server: Server) extends lila.ai.Ai with Stockfish {

  import model._

  def play(dbGame: DbGame, pgn: String, initialFen: Option[String]): Future[Valid[(Game, Move)]] =
    server.play(pgn, initialFen, dbGame.aiLevel | 1) map { validMove ⇒
      validMove flatMap { applyMove(dbGame, pgn, _) }
    }

  def analyse(pgn: String, initialFen: Option[String]): Future[Valid[Analysis]] =
    server.analyse(pgn, initialFen)

  private implicit val executor = Akka.system.dispatcher
}
