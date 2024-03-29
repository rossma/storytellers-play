package models

import com.github.tototoshi.slick.JdbcJodaSupport._
import org.joda.time.DateTime
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag

case class Series(id: Option[Long], name: String, description: Option[String] = None, created: DateTime)

class SeriesTbl(tag: Tag) extends Table[Series](tag, "SERIES") {
  
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME", O.NotNull)
  def description = column[String]("DESCRIPTION", O.Nullable)
  def created = column[DateTime]("CREATION_DATE", O.NotNull)
  def * = (id.?, name, description.?, created) <> (Series.tupled, Series.unapply _)
}

object SeriesDAO {
  val seriesQ = TableQuery[SeriesTbl]

  def findById(id: Long)(implicit s: Session): Option[Series] =
    seriesQ.filter(_.id === id).firstOption

  def count(implicit s: Session): Int =
    Query(seriesQ.length).first

  def insert(series: Series)(implicit s: Session) {
    seriesQ.insert(series)
  }

  /**
   * Insert a new series by ignoring auto inc column
   */
  def forceInsert(series: Series)(implicit s: Session) {
    seriesQ.forceInsert(series)
  }

  def list(implicit s: Session) = {
    seriesQ.list
  }

  def deleteAll(implicit s: Session) {
    seriesQ.delete
  }
}
